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

package dev.vini2003.hammer.gui.api.common.widget.list

import dev.vini2003.hammer.core.HC
import dev.vini2003.hammer.core.api.client.scissor.Scissors
import dev.vini2003.hammer.core.api.client.texture.BaseTexture
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture
import dev.vini2003.hammer.gui.api.common.widget.BaseWidget
import dev.vini2003.hammer.gui.api.common.widget.BaseWidgetCollection
import dev.vini2003.hammer.core.api.client.util.InstanceUtils
import dev.vini2003.hammer.core.api.common.math.position.Position
import dev.vini2003.hammer.core.api.client.util.PositionUtils
import dev.vini2003.hammer.core.api.common.math.shape.Shape
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * A [ListWidget] is a widget that holds [BaseWidget]s.
 */
open class ListWidget : BaseWidget(), BaseWidgetCollection {
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
		
		@JvmField
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
	
    protected open var scrollerHeld = false

    protected open var updateScrollerRectangle: Boolean = false
    protected open var updateScrollbarRectangle: Boolean = false

    protected open var scrollerRectangleCached: Shape = Shape.ScreenRectangle(0.0F, 0.0F)
    protected open var scrollbarRectangleCached: Shape = Shape.ScreenRectangle(0.0F, 0.0F)

    protected open val scrollerHeight: Float
        get() = min(size.height - 2.0F, height / widgets.sumOf { widget -> widget.height.toInt() } * height)

    protected open val scrollerY: Float
        get() {
            val maxY = widgets.maxOfOrNull { widget -> widget.y + widget.height } ?: 0.0F
            val minY = widgets.minOfOrNull { widget -> widget.y } ?: 0.0F

            return max(y + 2.0F, min(y + size.height - scrollerHeight, (abs(y - minY) / (maxY - minY) * (size.height + scrollerHeight) + y + 1.0F)))
        }

    protected open val scrollerRectangle: Shape
        get() {
            return if (updateScrollerRectangle) {
                scrollerRectangleCached = Shape.ScreenRectangle(16.0F, scrollerHeight, Position(position.x + size.width - 1.0F - 16.0F, scrollerY - 1.0F))
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
				onMouseScrolled(x, y, -deltaY)
			} else if (deltaY < 0.0) {
				onMouseScrolled(x, y, -deltaY)
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

		if (widgets.isNotEmpty()) {
			if (deltaY > 0.0 && widgets.minOf { widget -> widget.y } < y + 2.0F) {
				allWidgets.forEach { widget ->
					widget.y += deltaY.toFloat() * 2.5F

					widget.onLayoutChanged()
					
					onLayoutChanged()

					if (widget.y >= y + height) {
						widget.hidden = true
					} else if (widget.y >= y) {
						widget.hidden = false
					}
				}
			} else if (deltaY <= 0.0 && widgets.maxOf { widget -> widget.y + widget.height } >= y + height - 2.0F) {
				allWidgets.forEach { widget ->
					widget.y += deltaY.toFloat() * 2.5F

					widget.onLayoutChanged()
					
					onLayoutChanged()

					if (widget.y >= y + height) {
						widget.hidden = true
					} else if (widget.y >= y) {
						widget.hidden = false
					}
				}
			}
		}
    }

    override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, tickDelta: Float) {
        scrollbarTexture.draw(matrices, provider, position.x + size.width - 18.0F, position.y, 18.0F, size.height)

        val scrollerFocus = scrollerRectangle.isPositionWithin(PositionUtils.MOUSE_POSITION)

        if (scrollerFocus || scrollerHeld) {
            focusedScrollerTexture.draw(matrices, provider, position.x + size.width - 18.0F + 1.0F, scrollerY - 1.0F, 16.0F, scrollerHeight)
        } else {
            scrollerTexture.draw(matrices, provider, position.x + size.width - 18.0F + 1.0F, scrollerY - 1.0F, 16.0F, scrollerHeight)
        }
		
        val scissors = Scissors(x, y, width, height, provider)
	
	    widgets.filterNot { widget ->
			widget.hidden
		} .forEach { widget ->
			widget.drawWidget(matrices, provider, tickDelta)
	    }

        scissors.destroy()
    }
}
