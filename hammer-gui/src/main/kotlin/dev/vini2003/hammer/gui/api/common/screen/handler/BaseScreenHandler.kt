/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.gui.api.common.screen.handler

import dev.vini2003.hammer.gui.api.common.widget.BaseWidget
import dev.vini2003.hammer.gui.api.common.widget.BaseWidgetCollection
import dev.vini2003.hammer.core.api.common.math.position.Position
import dev.vini2003.hammer.core.api.common.util.StackUtils
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType

/**
 * A [BaseScreenHandler] is a screen handler that holds widgets.
 */
abstract class BaseScreenHandler(
	type: ScreenHandlerType<out ScreenHandler>,
	syncId: Int,
	val player: PlayerEntity
) : ScreenHandler(type, syncId), BaseWidgetCollection.Handled {
	var rectangle: Shape = Shape.ScreenRectangle(0.0F, 0.0F)
	
	override val widgets: MutableList<BaseWidget> = mutableListOf()
	
	override val id = this.syncId

	override val client = player.world.isClient
	
	override val handler = this
	
	abstract fun initialize(width: Int, height: Int)

	override fun onLayoutChanged() {
		var minimumX = Float.MAX_VALUE
		var minimumY = Float.MAX_VALUE
		
		var maximumX = 0.0F
		var maximumY = 0.0F

		widgets.forEach { widget ->
			if (widget.x < minimumX) {
				minimumX = widget.x
			}
			
			if (widget.x + widget.width > maximumX) {
				maximumX = widget.x + widget.width
			}
			
			if (widget.y < minimumY) {
				minimumY = widget.y
			}
			
			if (widget.y + widget.height > maximumY) {
				maximumY = widget.y + widget.height
			}
		}

		rectangle = Shape.ScreenRectangle(maximumX - minimumX, maximumY - minimumY, Position(minimumX, minimumY))

		if (client) {
			onLayoutChangedDelegate()
		}
	}
	
	@Environment(EnvType.CLIENT)
	fun onLayoutChangedDelegate() {
		val client = InstanceUtils.CLIENT ?: return
		
		val screen = client.currentScreen as HandledScreen<*>? ?: return

		screen.x = rectangle.startPos.x.toInt()
		screen.y = rectangle.startPos.y.toInt()

		screen.backgroundWidth = rectangle.width.toInt()
		screen.backgroundHeight = rectangle.height.toInt()
	}

	override fun add(widget: BaseWidget) {
		widgets += widget
		
		widget.onAdded(this, this)
		
		widgets.forEach { widget ->
			widget.onLayoutChanged()
		}
	}

	override fun remove(widget: BaseWidget) {
		widgets -= widget
		
		widget.onRemoved(this, this)
		
		widgets.forEach { widget ->
			widget.onLayoutChanged()
		}
	}

	public override fun addSlot(slot: Slot?): Slot {
		return super.addSlot(slot)
	}

	fun removeSlot(slot: Slot?) {
		val id = slot!!.id
		
		slots -= slot
		
		slots.forEach { widget ->
			if (widget.id >= id) {
				--widget.id
			}
		}
	}

	override fun onSlotClick(slotNumber: Int, button: Int, actionType: SlotActionType, playerEntity: PlayerEntity) {
		when (actionType) {
			SlotActionType.QUICK_MOVE -> {
				if (slotNumber >= 0 && slotNumber < slots.size) {
					val slot = slots[slotNumber]

					if (!slot.stack.isEmpty && slot.canTakeItems(playerEntity)) {
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
		widgets.forEach { widget ->
			widget.tick()
		}
	}
}