package dev.vini2003.hammer.gui.api.common.widget.text

import dev.vini2003.hammer.core.api.common.color.Color
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import org.lwjgl.glfw.GLFW




open class TextArea : AbstractTextEditor() {
    protected val newLine: MutableList<Boolean> = ArrayList()

    protected var lineWrap: Boolean = true

    open fun <W : TextArea?> setLineWrap(lineWrap: Boolean): W {
        this.lineWrap = lineWrap
        return this as W
    }

    override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, tickDelta: Float) {
        if (hidden) {
            return
        }

        val x = position.x
        val y = position.y
        val z = z

        val sX: Float = size.width
        val sY: Float = size.height

        matrices.push()
        DrawingUtils.drawQuad(
            matrices,
            provider,
            x,
            y,
            sX,
            sY,
            z,
            color = Color.BLACK
        )

        if (lineWrap && xOffset != 0f) xOffset = 0f

        renderField(matrices, provider)
        matrices.pop()

        super.drawWidget(matrices, provider, tickDelta)
    }

    // This is useful e.g. for line numbering
    protected open fun hasNewline(index: Int): Boolean {
        return if (lineWrap) newLine[index] else true
    }

    protected open fun getNewlinedLineLength(index: Int): Int {
        return getLineLength(index) + if (hasNewline(index)) 1 else 0
    }

    override fun getStringIndex(cursor: Cursor): Int {
        var i = 0
        for (y in 0 until cursor.y) {
            i += getNewlinedLineLength(y)
        }
        i += cursor.x
        return i
    }

    override fun setText(text: String): TextArea {
        if (lineWrap) {
            lines.clear()
            newLine.clear()
            this.text = text
            var currentLine = StringBuilder()
            var lineWidth = 0
            for (c in text.toCharArray()) {
                lineWidth += Math.round(DrawingUtils.TEXT_RENDERER!!.getWidth(c.toString()) * scale)
                if (lineWidth > getInnerSize().width || c == '\n') {
                    lines.add(currentLine.toString())
                    newLine.add(c == '\n')
                    lineWidth = Math.round(DrawingUtils.TEXT_RENDERER!!.getWidth(c.toString()) * scale)
                    currentLine = StringBuilder()
                }
                if (c != '\n') currentLine.append(c)
            }
            val finalLine = currentLine.toString()
            if (!finalLine.isEmpty() || text.endsWith("\n")) {
                newLine.add(text.endsWith("\n"))
                lines.add(finalLine)
                newLine.add(true)
            }
        } else {
            super.setText(text)
        }
        return this
    }

    override fun processKeyActions(keyPressed: Int, character: Int, keyModifier: Int) {
        if (keyPressed == GLFW.GLFW_KEY_BACKSPACE && !hasSelection()) {
            val lineLength = getLineLength(cursor.y)
            val deleted = deleteText(cursor.copy().left(), cursor)
            if (deleted == "") {
                cursor.left()
                deleteText(cursor.copy().left(), cursor)
            }
            if (deleted == "\n") {
                for (i in 0 until lineLength) {
                    cursor.left()
                }
            }
            cursor.left()
            return
        } else if (keyPressed == GLFW.GLFW_KEY_DELETE && !hasSelection()) {
            val deleted = deleteText(cursor, cursor.copy().right())
            if (deleted == "") {
                deleteText(cursor, cursor.copy().right().right())
            }
            return
        }
        super.processKeyActions(keyPressed, character, keyModifier)
    }
}