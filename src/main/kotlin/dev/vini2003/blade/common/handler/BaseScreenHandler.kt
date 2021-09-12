package dev.vini2003.blade.common.handler

import dev.vini2003.blade.client.util.Instances
import dev.vini2003.blade.common.collection.base.HandledWidgetCollection
import dev.vini2003.blade.common.geometry.position.Position
import dev.vini2003.blade.common.geometry.Rectangle
import dev.vini2003.blade.common.geometry.size.Size
import dev.vini2003.blade.common.util.Networks
import dev.vini2003.blade.common.util.Stacks
import dev.vini2003.blade.common.widget.AbstractWidget
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.util.Identifier
import java.util.*

abstract class BaseScreenHandler(type: ScreenHandlerType<out ScreenHandler>, syncId: Int, val player: PlayerEntity) : ScreenHandler(type, syncId), HandledWidgetCollection {
	override val widgets: ArrayList<AbstractWidget> = ArrayList()
	
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
		val screen = Instances.client.currentScreen as? HandledScreen<*> ?: return

		screen.x = rectangle.position.x.toInt()
		screen.y = rectangle.position.y.toInt()

		screen.backgroundWidth = rectangle.size.width.toInt()
		screen.backgroundHeight = rectangle.size.height.toInt()
	}

	override fun addWidget(widget: AbstractWidget) {
		widgets.add(widget)
		widget.onAdded(this, this)

		widgets.forEach { _ ->
			widget.onLayoutChanged()
		}
	}

	override fun removeWidget(widget: AbstractWidget) {
		widgets.remove(widget)
		widget.onRemoved(this, this)

		widgets.forEach { _ ->
			widget.onLayoutChanged()
		}
	}

	open fun handlePacket(id: Identifier, buf: PacketByteBuf) {
		val hash = buf.readInt()

		allWidgets.forEach {
			val buffer = PacketByteBuf(buf.copy())
			if (it.hash == hash) {
				when (id) {
					Networks.MouseMoved -> it.onMouseMoved(buffer.readFloat(), buffer.readFloat())
					Networks.MouseClicked -> it.onMouseClicked(buffer.readFloat(), buffer.readFloat(), buffer.readInt())
					Networks.MouseReleased -> it.onMouseReleased(buffer.readFloat(), buffer.readFloat(), buffer.readInt())
					Networks.MouseDragged -> it.onMouseDragged(buffer.readFloat(), buffer.readFloat(), buffer.readInt(), buffer.readDouble(), buffer.readDouble())
					Networks.MouseScrolled -> it.onMouseScrolled(buffer.readFloat(), buffer.readFloat(), buffer.readDouble())
					Networks.KeyPressed -> it.onKeyPressed(buffer.readInt(), buffer.readInt(), buffer.readInt())
					Networks.KeyReleased -> it.onKeyReleased(buffer.readInt(), buffer.readInt(), buffer.readInt())
					Networks.CharacterTyped -> it.onCharacterTyped(buffer.readChar(), buffer.readInt())
					Networks.FocusGained -> it.onFocusGained()
					Networks.FocusReleased -> it.onFocusReleased()
				}

				return
			}
		}
	}

	public override fun addSlot(slot: Slot?): Slot {
		return super.addSlot(slot)
	}

	fun removeSlot(slot: Slot?) {
		val id = slot!!.id
		slots.remove(slot)
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
										Stacks.merge(slot.stack, newSlot.stack) { stackA, stackB ->
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
									Stacks.merge(slot.stack, newSlot.stack) { stackA, stackB ->
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
		this.widgets.forEach {
			it.tick()
		}
	}
}