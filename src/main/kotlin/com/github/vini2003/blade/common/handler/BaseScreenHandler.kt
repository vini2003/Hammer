package com.github.vini2003.blade.common.handler

import com.github.vini2003.blade.client.utilities.Instances
import com.github.vini2003.blade.common.data.Stacks
import com.github.vini2003.blade.common.utilities.Networks
import com.github.vini2003.blade.common.widget.OriginalWidgetCollection
import com.github.vini2003.blade.common.widget.base.AbstractWidget
import me.shedaniel.math.Rectangle
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.util.Identifier

abstract class BaseScreenHandler(type: ScreenHandlerType<out ScreenHandler>, syncId: Int, private val player: PlayerEntity) : ScreenHandler(type, syncId), OriginalWidgetCollection {
	override val widgets: ArrayList<AbstractWidget> = ArrayList()

	val client = player.world.isClient

	var rectangle: Rectangle = Rectangle()

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

		rectangle = Rectangle(minimumX.toInt(), minimumY.toInt(), (maximumX - minimumX).toInt(), (maximumY - minimumY).toInt())

		if (client) {
			onLayoutChangedDelegate()
		}
	}
	
	@Environment(EnvType.CLIENT)
	fun onLayoutChangedDelegate() {
		val screen = Instances.client().currentScreen as? HandledScreen<*> ?: return

		screen.x = rectangle.minX
		screen.y = rectangle.minY

		screen.backgroundWidth = rectangle.width
		screen.backgroundHeight = rectangle.height
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
					Networks.MOUSE_MOVE -> it.onMouseMoved(buffer.readFloat(), buffer.readFloat())
					Networks.MOUSE_CLICK -> it.onMouseClicked(buffer.readFloat(), buffer.readFloat(), buffer.readInt())
					Networks.MOUSE_RELEASE -> it.onMouseReleased(buffer.readFloat(), buffer.readFloat(), buffer.readInt())
					Networks.MOUSE_DRAG -> it.onMouseDragged(buffer.readFloat(), buffer.readFloat(), buffer.readInt(), buffer.readDouble(), buffer.readDouble())
					Networks.MOUSE_SCROLL -> it.onMouseScrolled(buffer.readFloat(), buffer.readFloat(), buffer.readDouble())
					Networks.KEY_PRESS -> it.onKeyPressed(buffer.readInt(), buffer.readInt(), buffer.readInt())
					Networks.KEY_RELEASE -> it.onKeyReleased(buffer.readInt(), buffer.readInt(), buffer.readInt())
					Networks.CHAR_TYPE -> it.onCharTyped(buffer.readChar(), buffer.readInt())
					Networks.FOCUS_GAIN -> it.onFocusGained()
					Networks.FOCUS_RELEASE -> it.onFocusReleased()
				}

				return@forEach
			}
		}
	}

	override fun getPlayer(): PlayerEntity {
		return player
	}

	override fun getHandler(): BaseScreenHandler {
		return this
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

	override fun onSlotClick(slotNumber: Int, button: Int, actionType: SlotActionType?, playerEntity: PlayerEntity?): ItemStack {
		return when (actionType) {
			SlotActionType.QUICK_MOVE -> {
				if (slotNumber >= 0 && slotNumber < slots.size) {
					val slot = slots[slotNumber]

					if (slot != null && !slot.stack.isEmpty && slot.canTakeItems(playerEntity)) {
						for (newSlotNumber in 0 until slots.size) {
							val newSlot = slots[newSlotNumber]
							
							if (newSlot.canInsert(slot.stack)) {
								if (newSlot.inventory !is PlayerInventory && newSlot != slot && newSlot.inventory != slot.inventory) {
									Stacks.merge(slot.stack, newSlot.stack, slot.stack.maxCount, newSlot.stack.maxCount) { stackA, stackB ->
										slot.stack = stackA
										newSlot.stack = stackB
									}
								}

								if (newSlot.inventory is PlayerInventory && newSlot != slot && newSlot.inventory != slot.inventory) {
									Stacks.merge(slot.stack, newSlot.stack, slot.stack.maxCount, newSlot.stack.maxCount) { stackA, stackB ->
										slot.stack = stackA
										newSlot.stack = stackB
									}
								}

								if (slot.stack.isEmpty) break
							}
						}
					}
				}

				return ItemStack.EMPTY
			}
			else -> super.onSlotClick(slotNumber, button, actionType, playerEntity)
		}
	}
}