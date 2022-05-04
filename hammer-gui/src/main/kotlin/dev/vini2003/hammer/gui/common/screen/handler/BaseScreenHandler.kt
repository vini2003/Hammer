package dev.vini2003.hammer.gui.common.screen.handler

import dev.vini2003.hammer.gui.common.widget.Widget
import dev.vini2003.hammer.gui.common.widget.WidgetCollection
import dev.vini2003.hammer.client.util.InstanceUtils
import dev.vini2003.hammer.common.geometry.Rectangle
import dev.vini2003.hammer.common.geometry.position.Position
import dev.vini2003.hammer.common.geometry.size.Size
import dev.vini2003.hammer.common.util.StackUtils
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType

abstract class BaseScreenHandler(type: ScreenHandlerType<out ScreenHandler>, syncId: Int, val player: PlayerEntity) : ScreenHandler(type, syncId), WidgetCollection.Handled {
	override val widgets: MutableList<Widget> = ArrayList()
	
	override val id = this.syncId

	override val client = player.world.isClient
	
	override val handler = this

	var rectangle: Rectangle = Rectangle(Position.of(0, 0), Size.of(0, 0))

	abstract fun initialize(width: Int, height: Int)

	override fun onLayoutChanged() {
		var minimumX = Float.MAX_VALUE
		var minimumY = Float.MAX_VALUE
		var maximumX = 0F
		var maximumY = 0F

		widgets.forEach {
			if (it.x < minimumX) {
				minimumX = it.x
			}
			if (it.x + it.width > maximumX) {
				maximumX = it.x + it.width
			}
			if (it.y < minimumY) {
				minimumY = it.y
			}
			if (it.y + it.height > maximumY) {
				maximumY = it.y + it.height
			}
		}

		rectangle = Rectangle(Position.of(minimumX.toInt(), minimumY.toInt()), Size.of((maximumX - minimumX).toInt(), (maximumY - minimumY).toInt()))

		if (client) {
			onLayoutChangedDelegate()
		}
	}
	
	@Environment(EnvType.CLIENT)
	fun onLayoutChangedDelegate() {
		val screen = InstanceUtils.CLIENT.currentScreen as? HandledScreen<*> ?: return

		screen.x = rectangle.position.x.toInt()
		screen.y = rectangle.position.y.toInt()

		screen.backgroundWidth = rectangle.size.width.toInt()
		screen.backgroundHeight = rectangle.size.height.toInt()
	}

	override fun add(widget: Widget) {
		widgets += widget
		
		widget.onAdded(this, this)
		
		widgets.forEach(Widget::onLayoutChanged)
	}

	override fun remove(widget: Widget) {
		widgets -= widget
		
		widget.onRemoved(this, this)
		
		widgets.forEach(Widget::onLayoutChanged)
	}

	public override fun addSlot(slot: Slot?): Slot {
		return super.addSlot(slot)
	}

	fun removeSlot(slot: Slot?) {
		val id = slot!!.id
		
		slots -= slot
		
		slots.forEach {
			if (it.id >= id) {
				--it.id
			}
		}
	}

	override fun onSlotClick(slotNumber: Int, button: Int, actionType: SlotActionType, playerEntity: PlayerEntity) {
		when (actionType) {
			SlotActionType.QUICK_MOVE -> {
				if (slotNumber >= 0 && slotNumber < slots.size) {
					val slot = slots[slotNumber]

					if (slot != null && !slot.stack.isEmpty && slot.canTakeItems(playerEntity)) {
						for (newSlotNumber in 0 until slots.size) {
							val newSlot = slots[newSlotNumber]

							if (!newSlot.stack.isEmpty) {
								if (newSlot.canInsert(slot.stack)) {
									if (newSlot != slot && newSlot.inventory != slot.inventory) {
										StackUtils.merge(slot.stack, newSlot.stack) { stackA, stackB ->
											slot.stack = stackA
											newSlot.stack = stackB
										}
									}

									if (slot.stack.isEmpty) break
								}
							}
						}

						for (newSlotNumber in 0 until slots.size) {
							val newSlot = slots[newSlotNumber]

							if (newSlot.canInsert(slot.stack)) {
								if (newSlot != slot && newSlot.inventory != slot.inventory) {
									StackUtils.merge(slot.stack, newSlot.stack) { stackA, stackB ->
										slot.stack = stackA
										newSlot.stack = stackB
									}
								}

								if (slot.stack.isEmpty) break
							}
						}
					}
				}
			}
			
			else -> super.onSlotClick(slotNumber, button, actionType, playerEntity)
		}
	}
	
	open fun tick() {
		widgets.forEach(Widget::tick)
	}
}