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

package dev.vini2003.hammer.gui.api.common.widget.list.slot

import dev.vini2003.hammer.core.HC
import dev.vini2003.hammer.gui.api.common.widget.BaseWidget
import dev.vini2003.hammer.gui.api.common.widget.BaseWidgetCollection
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget
import dev.vini2003.hammer.core.api.common.math.position.Position
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import kotlin.math.max
import kotlin.math.min

/**
 * A [SlotListWidget] is a widget that holds [SlotWidget]s.
 */
open class SlotListWidget @JvmOverloads constructor(
	var inventory: Inventory = SimpleInventory(0),
	var widthInSlots: Int = 0,
	var heightInSlots: Int = 0,
	var maximumSlots: Int = 0
) : BaseWidget(), BaseWidgetCollection {
	companion object {
		@JvmField
		val STANDARD_SCROLLBAR_TEXTURE: BaseTexture = PartitionedTexture(
			HC.id("textures/widget/scrollbar.png"),
			18.0F,
			18.0F,
			0.11F,
			0.11F,
			0.11F,
			0.16F
		)
		
		@JvmField
		val STANDARD_SCROLLER_TEXTURE: BaseTexture = PartitionedTexture(
			HC.id("textures/widget/scroller.png"),
			18.0F,
			18.0F,
			0.11F,
			0.11F,
			0.11F,
			0.11F
		)
		
		val STANDARD_FOCUSED_SCROLLER_TEXTURE: BaseTexture = PartitionedTexture(
			HC.id("textures/widget/scroller_focus.png"),
			18.0F,
			18.0F,
			0.11F,
			0.11F,
			0.11F,
			0.11F
		)
	}
	
	override val widgets: MutableList<BaseWidget> = mutableListOf()
	
	protected open var row: Int = 0
	
	protected open  var scrollerHeld: Boolean = false

	protected open var updateScrollerRectangle: Boolean = false
	protected open var updateScrollbarRectangle: Boolean = false
	
	protected open var scrollerRectangleCached: Shape = Shape.ScreenRectangle(0.0F, 0.0F)
	protected open var scrollbarRectangleCached: Shape = Shape.ScreenRectangle(0.0F, 0.0F)
	
	protected open val bottomRow: Int
		get() = maximumSlots - heightInSlots
	
	protected open val totalRows: Int
		get() = inventory.size() / widthInSlots
	
	protected open val scrollerHeight: Float
		get() = min(size.height - 2.0F, heightInSlots.toFloat() / totalRows.toFloat() * (size.height - 2.0F))
	
	protected open val scrollerY: Float
		get() = max(y + 2.0F, min(y + size.height - scrollerHeight, row.toFloat() / totalRows.toFloat() * size.height + y + 1.0F))
	
	protected open val scrollerRectangle: Shape
		get() {
			return if (updateScrollerRectangle) {
				scrollerRectangleCached = Shape.ScreenRectangle(16.0F, scrollerHeight, Position(position.x + size.width - 1 - 16.0F, scrollerY - 1))
				updateScrollerRectangle = false
				
				return scrollerRectangleCached
			} else {
				scrollerRectangleCached
			}
		}

	protected open val scrollbarRectangle: Shape
		get() {
			return if (updateScrollbarRectangle) {
				scrollbarRectangleCached = Shape.ScreenRectangle(16.0F, size.height - 2.0F, Position(position.x + size.width - 1.0F - 16.0F, position.y + 1.0F))
				updateScrollbarRectangle = false
				
				return scrollbarRectangleCached
			} else {
				scrollbarRectangleCached
			}
		}
	
	var scrollbarTexture: BaseTexture = STANDARD_SCROLLBAR_TEXTURE
	
	var scrollerTexture: BaseTexture = STANDARD_SCROLLER_TEXTURE
	
	var focusedScrollerTexture: BaseTexture = STANDARD_FOCUSED_SCROLLER_TEXTURE

	override fun add(widget: BaseWidget) {
		widgets += widget
		
		if (handled != null) {
			widget.onAdded(handled!!, this)
		}
		
		widgets.forEach { widget ->
			widget.onLayoutChanged()
		}

		super.add(widget)
	}

	override fun remove(widget: BaseWidget) {
		widgets -= widget
		
		if (handled != null) {
			widget.onRemoved(handled!!, this)
		}
		
		widgets.forEach { widget ->
			widget.onLayoutChanged()
		}

		super.remove(widget)
	}

	override fun onLayoutChanged() {
		super.onLayoutChanged()

		updateScrollerRectangle = true
		updateScrollbarRectangle = true
	}

	override fun onAdded(handled: BaseWidgetCollection.Handled, immediate: BaseWidgetCollection) {
		super.onAdded(handled, immediate)

		syncMouseScrolled = true
		syncMouseClicked = true

		widthInSlots = (size.width / 18.0F - 1.0F).toInt()
		heightInSlots = (size.height / 18.0F).toInt()
		
		maximumSlots = inventory.size() / widthInSlots

		for (h in 0 until heightInSlots) {
			for (w in 0 until widthInSlots) {
				if ((inventory.size() >= h + w)) {
					val slot = SlotWidget(0 + h * widthInSlots + w, inventory)
					
					slot.position = position.offset(w * 18.0F, h * 18.0F, 0.0F)
					slot.size = Size(18.0F, 18.0F)
					
					widgets += slot
					
					immediate += slot
				}
			}
		}
	}

	override fun onMouseClicked(x: Float, y: Float, button: Int) {
		if (handled!!.client) {
			super.onMouseClicked(x, y, button)
		}
		
		val pos = Position(x, y)

		if (scrollerRectangle.isPositionWithin(pos)) {
			scrollerHeld = true
		} else if (scrollbarRectangle.isPositionWithin(pos)) {
			if (y > scrollerY) {
				onMouseScrolled(x, y, -1.0)
			} else if (y < scrollerY) {
				onMouseScrolled(x, y, 1.0)
			}
		}
	}

	override fun onMouseReleased(x: Float, y: Float, button: Int) {
		if (handled!!.client) {
			super.onMouseReleased(x, y, button)
		}

		scrollerHeld = false
	}

	override fun onMouseDragged(x: Float, y: Float, button: Int, deltaX: Double, deltaY: Double) {
		if (handled!!.client) {
			super.onMouseDragged(x, y, button, deltaX, deltaY)
		}

		if (scrollerHeld) {
			if (deltaY > 0.0) {
				while (scrollerY < y - scrollerHeight / 2.0F && row < bottomRow) {
					onMouseScrolled(x, y, -deltaY)
				}
			} else if (deltaY < 0.0) {
				while (scrollerY > y - scrollerHeight / 2.0F && row > 0.0F) {
					onMouseScrolled(x, y, -deltaY)
				}
			}
		}
	}

	override fun onMouseScrolled(x: Float, y: Float, deltaY: Double) {
		if (handled!!.client) {
			if (focused || scrollerHeld) {
				super.onMouseScrolled(x, y, deltaY)
			} else {
				return
			}
		}

		if (deltaY > 0.0 && row > 0) {
			--row
			
			widgets.forEach { widget ->
				widget as SlotWidget
				
				val slot = widget.backendSlot!!

				if (slot.index - widthInSlots >= 0) {
					slot.index = slot.index - widthInSlots
				}

				handled!!.handler!!.sendContentUpdates()
			}
		} else if (deltaY < 0.0 && row < bottomRow) {
			++row
			
			widgets.forEach { widget ->
				widget as SlotWidget

				val slot = widget.backendSlot!!

				if (slot.index + widthInSlots <= inventory.size()) {
					slot.index = slot.index + widthInSlots
				}

				handled!!.handler!!.sendContentUpdates()
			}
		}

		updateScrollerRectangle = true
		updateScrollbarRectangle = true
	}

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, tickDelta: Float) {
		scrollbarTexture.draw(matrices, provider, position.x + size.width - 18.0F, position.y, 18.0F, size.height)

		val scrollerFocus = scrollerRectangle.isPositionWithin(PositionUtils.MOUSE_POSITION)

		if (scrollerFocus || scrollerHeld) {
			focusedScrollerTexture.draw(matrices, provider, position.x + size.width - 18.0F + 1.0F, scrollerY - 1.0F, 16.0F, scrollerHeight)
		} else {
			scrollerTexture.draw(matrices, provider, position.x + size.width - 18.0F + 1.0F, scrollerY - 1.0F, 16.0F, scrollerHeight)
		}
	}
}