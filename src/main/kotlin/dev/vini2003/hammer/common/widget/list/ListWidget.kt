package dev.vini2003.hammer.common.widget.list

import dev.vini2003.hammer.H
import dev.vini2003.hammer.client.util.Instances
import dev.vini2003.hammer.client.scissor.Scissors
import dev.vini2003.hammer.client.texture.Texture
import dev.vini2003.hammer.common.widget.WidgetCollection
import dev.vini2003.hammer.common.geometry.position.Position
import dev.vini2003.hammer.common.geometry.Rectangle
import dev.vini2003.hammer.common.geometry.size.Size
import dev.vini2003.hammer.common.util.Positions
import dev.vini2003.hammer.common.widget.Widget
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

open class ListWidget : Widget(), WidgetCollection {
	companion object {
		val StandardScrollbarTexture: Texture = Texture.of(
			H.id("textures/widget/scrollbar.png"),
			18F,
			18F,
			0.11111111111111111111F,
			0.11111111111111111111F,
			0.11111111111111111111F,
			0.16666666666666666667F
		)
		
		val StandardScrollerTexture: Texture = Texture.of(
			H.id("textures/widget/scroller.png"),
			18F,
			18F,
			0.11111111111111111111F,
			0.11111111111111111111F,
			0.11111111111111111111F,
			0.11111111111111111111F
		)
		
		val StandardFocusedScrollerTexture: Texture = Texture.of(
			H.id("textures/widget/scroller_focus.png"),
			18F,
			18F,
			0.11111111111111111111F,
			0.11111111111111111111F,
			0.11111111111111111111F,
			0.11111111111111111111F
		)
	}
	
    override val widgets: MutableList<Widget> = mutableListOf()

    var scrollbarTexture: Texture = StandardScrollbarTexture
    var scrollerTexture: Texture = StandardScrollerTexture
    var focusedScrollerTexture: Texture = StandardFocusedScrollerTexture

    private var scrollerHeld = false

    private var updateScrollerRectangle: Boolean = false
    private var updateScrollbarRectangle: Boolean = false

    private var scrollerRectangleCached: Rectangle = Rectangle.Empty
    private var scrollbarRectangleCached: Rectangle = Rectangle.Empty

    private val scrollerHeight: Float
        get() = min(size.height - 2, height / widgets.sumBy { it.height.toInt() } * height)

    private val scrollerY: Float
        get() {
            val maxY = widgets.asSequence().map { it.y + it.height }.maxOrNull() ?: 0F
            val minY = widgets.asSequence().map { it.y }.minOrNull() ?: 0F

            return max(y + 2, min(y + size.height - scrollerHeight, (abs(y - minY) / (maxY - minY) * (size.height + scrollerHeight) + y + 1)))
        }

    private val scrollerRectangle: Rectangle
        get() {
            return if (updateScrollerRectangle) {
                scrollerRectangleCached = Rectangle(Position.of(position.x + size.width - 1 - 16F, scrollerY - 1), Size.of(16F, scrollerHeight))
                updateScrollerRectangle = false
                return scrollerRectangleCached
            } else {
                scrollerRectangleCached
            }
        }

    private val scrollbarRectangle: Rectangle
        get() {
            return if (updateScrollbarRectangle) {
                scrollbarRectangleCached = Rectangle(Position.of(position.x + size.width - 1 - 16F, position.y + 1), Size.of(16F, size.height - 2))
                updateScrollbarRectangle = false
                return scrollbarRectangleCached
            } else {
                scrollbarRectangleCached
            }
        }

    override fun add(widget: Widget) {
        widgets += widget
	    
	    if (handled != null) {
	    	widget.onAdded(handled!!, this)
	    }

	    widgets.forEach(Widget::onLayoutChanged)

        super.add(widget)
    }

    override fun remove(widget: Widget) {
        widgets -= widget
	    
	    if (handled != null) {
	    	widget.onRemoved(handled!!, this)
	    }
	
	    widgets.forEach(Widget::onLayoutChanged)
	    
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

        if (scrollerRectangle.isWithin(x, y)) {
            scrollerHeld = true
        } else if (scrollbarRectangle.isWithin(x, y)) {
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
			if (deltaY > 0) {
				onMouseScrolled(x, y, -deltaY)
			} else if (deltaY < 0) {
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
			if (deltaY > 0 && widgets.minByOrNull { it.y }!!.y < y + 2) {
				allWidgets.forEach {
					it.y += deltaY.toFloat() * 2.5F

					it.onLayoutChanged()
					onLayoutChanged()

					if (it.y >= y + height) it.hidden = true
					else if (it.y >= y) it.hidden = false
				}
			} else if (deltaY <= 0 && widgets.asSequence().map { it.y + it.height }.maxOrNull()!! >= y + height - 2) {
				allWidgets.forEach {
					it.y += deltaY.toFloat() * 2.5F

					it.onLayoutChanged()
					onLayoutChanged()

					if (it.y >= y + height) it.hidden = true
					else if (it.y >= y) it.hidden = false
				}
			}
		}
    }

    override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, delta: Float) {
        if (hidden) return

        scrollbarTexture.draw(matrices, provider, position.x + size.width - 18F, position.y, 18F, size.height)

        val scrollerFocus = scrollerRectangle.isWithin(Positions.MouseX, Positions.MouseY)

        if (scrollerFocus || scrollerHeld) {
            focusedScrollerTexture.draw(matrices, provider, position.x + size.width - 18F + 1F, scrollerY - 1, 16F, scrollerHeight)
        } else {
            scrollerTexture.draw(matrices, provider, position.x + size.width - 18F + 1F, scrollerY - 1, 16F, scrollerHeight)
        }

        val rawHeight = Instances.client.window.height.toFloat()
        val scale = Instances.client.window.scaleFactor.toFloat()

        val area = Scissors(provider, (x * scale).toInt(), (rawHeight - (y + height) * scale).toInt(), (width * scale).toInt(), (height * scale).toInt())
	
	    widgets.asSequence().filterNot(Widget::hidden).forEach {
		    it.drawWidget(matrices, provider, delta)
	    }

        area.destroy(provider)
    }
}
