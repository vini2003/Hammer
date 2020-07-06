package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.client.utilities.Drawings
import com.github.vini2003.blade.common.utilities.Positions
import com.github.vini2003.blade.client.utilities.Texts
import com.github.vini2003.blade.common.data.Position
import com.github.vini2003.blade.common.data.Positioned
import com.github.vini2003.blade.common.data.Size
import com.github.vini2003.blade.common.data.Sized
import com.github.vini2003.blade.common.widget.OriginalWidgetCollection
import com.github.vini2003.blade.common.widget.WidgetCollection
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

abstract class AbstractWidget : Positioned, Sized {
    private var position: Position = Position({0F}, {0F})
    private var size: Size = Size({0F}, {0F})

    var origin: OriginalWidgetCollection? = null;

    var hidden: Boolean = false
        get() {
            return (if (parent == null) field else field || parent!!.hidden)
        }

    var focused: Boolean = false
        get() {
            return (if (parent == null) field else field && !parent!!.hidden)
        }

    var held: Boolean = false
        get() {
            return (if (parent == null) field else field && !parent!!.hidden)
        }

    var parent: AbstractWidget? = null

    override fun getPosition(): Position {
        return position
    }

    override fun setPosition(position: Position) {
        this.position = position
    }

    override fun getSize(): Size {
        return size
    }

    override fun setSize(size: Size) {
        this.size = size
    }

    open fun onMouseMoved(x: Float, y: Float) {
        val wasFocused = focused;
        focused = isWithin(x, y)

        if (wasFocused && !focused) onFocusReleased()
        if (!wasFocused && focused) onFocusGained()

        if (this is WidgetCollection) {
            this.getWidgets().forEach {
                it.onMouseMoved(x, y)
            }
        }
    }

    open fun onMouseClicked(x: Float, y: Float, button: Int) {
        if (focused) held = true

        if (this is WidgetCollection) {
            this.getWidgets().forEach {
                it.onMouseClicked(x, y, button)
            }
        }
    }

    open fun onMouseReleased(x: Float, y: Float, button: Int) {
        held = false

        if (this is WidgetCollection) {
            this.getWidgets().forEach{
                it.onMouseReleased(x, y, button)
            }
        }
    }

    open fun onMouseDragged(x: Float, y: Float, button: Int, deltaX: Double, deltaY: Double) {
        if (this is WidgetCollection) {
            this.getWidgets().forEach{
                it.onMouseDragged(x, y, button, deltaX, deltaY)
            }
        }
    }

    open fun onMouseScrolled(x: Float, y: Float, deltaY: Double) {
        if (this is WidgetCollection) {
            this.getWidgets().forEach{
                it.onMouseScrolled(x, y, deltaY)
            }
        }
    }

    open fun onKeyPressed(keyCode: Int, scanCode: Int, keyModifiers: Int) {
        if (this is WidgetCollection) {
            this.getWidgets().forEach {
                it.onKeyPressed(keyCode, scanCode, keyModifiers)
            }
        }
    }

    open fun onKeyReleased(keyCode: Int, character: Int, keyModifier: Int) {
        if (this is WidgetCollection) {
            this.getWidgets().forEach {
                it.onKeyReleased(keyCode, character, keyModifier)
            }
        }
    }

    open fun onCharTyped(character: Char, keyCode: Int) {
        if (this is WidgetCollection) {
            this.getWidgets().forEach{
                it.onCharTyped(character, keyCode)
            }
        }
    }

    open fun onFocusGained() {
        if (this is WidgetCollection) {
            this.getWidgets().forEach{
                it.onFocusGained()
            }
        }
    }

    open fun onFocusReleased() {
        if (this is WidgetCollection) {
            this.getWidgets().forEach{
                it.onFocusReleased()
            }
        }
    }

    fun getTooltip(): List<Text> {
        return emptyList()
    }

    open fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
        if (hidden) return
        if (focused) drawTooltip(matrices, provider)
    }

    open fun onLayoutChanged() {
        position.recalculate()
        size.recalculate()
    }

    fun drawTooltip(matrices: MatrixStack, provider: VertexConsumerProvider) {
        val list = getTooltip()

        if (list.isEmpty()) return

        val width = Texts.width( list.maxBy { Texts.width(it) }!! )

        val height = list.sumBy { Texts.height() }

        val x: Float = Positions.mouseX + 8F
        var y: Float = Positions.mouseY - height / 2F

        Drawings.drawTooltip(matrices, provider, x, y, width.toFloat() + 7 + 7, height.toFloat() + 7 + 7);

        list.forEach{
            y += 7
            Drawings.getTextRenderer()?.draw(matrices, it, x + 7, y, 0xFFFFFF)
        }
    }

    fun isWithin(x: Float, y: Float): Boolean {
        return x > position.x && x < position.x + size.width && y > position.y && y < position.y + size.height;
    }
}