package dev.vini2003.hammer.gui.api.common.widget.text

import dev.vini2003.hammer.core.api.client.scissor.Scissors
import dev.vini2003.hammer.core.api.client.util.DrawingUtils
import dev.vini2003.hammer.core.api.common.color.Color
import dev.vini2003.hammer.core.api.common.math.position.Position
import dev.vini2003.hammer.core.api.common.math.size.Size
import dev.vini2003.hammer.gui.api.common.widget.BaseWidget
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.lwjgl.glfw.GLFW
import java.util.*

abstract class AbstractTextEditor : BaseWidget() {
    
    protected val lines: MutableList<String> = ArrayList()

    protected val cursor: Cursor = Cursor(0, 0)

    protected val selection: Pair<Cursor, Cursor> = Pair(Cursor(-1, -1), Cursor(-1, -1))

    protected val mouseClick: Cursor = Cursor(0, 0)

    protected var padding: Int = 2

    protected var text: String = ""

    protected var scale: Float = 1.0F

    protected var editable: Boolean = true

    protected var active: Boolean = false

    protected var xOffset: Float = 0F

    protected var yOffset: Float = 0F

    protected var cursorTick: Int = 20

    protected var filter: InputFilter<*>? = null
    
    var label: String = "Enter text here"

    init {
        setText("")
    }

    protected open fun hasSelection(): Boolean {
        return selection.first.present() && selection.second.present()
    }

    protected open fun clearSelection() {
        selection.first.assign(Cursor(-1, -1))
        selection.second.assign(Cursor(-1, -1))
    }

    protected open fun getNewlineLineLength(index: Int): Int {
        return getLineLength(index) + 1
    }

    protected open fun getVisibleColumns(): Int {
        return (getInnerSize().width / getXOffsetStep()).toInt()
    }

    protected open fun getInnerSize(): Size {
        return Size(size.width - 2 * padding, size.height - 2 * padding)
    }

    protected open fun getInnerAnchor(): Position {
        return Position(this, 2F, 2F)
    }

    protected open fun getXOffsetStep(): Int {
        return (DrawingUtils.TEXT_RENDERER!!.getWidth("m") * scale).toInt()
    }

    protected open fun getCursorFromMouse(mouseX: Float, mouseY: Float): Cursor {
        val innerPos: Position = getInnerAnchor()
        var x: Int = -1
        var y: Int = -1
        val lineOffset: Float = getLineOffset()
        val cH: Int = getTextHeight()
        val offsetMouseX: Float = -xOffset + mouseX
        for (i in 0 until getVisibleLines()) {
            if (mouseY >= innerPos.y + (cH + 2) * i && mouseY <= innerPos.y + (cH + 2) * i + cH) {
                if (lineOffset + i > lines.size - 1) {
                    y = lines.size - 1
                    if (offsetMouseX >= innerPos.x + getLineWidth(y)) {
                        x = getLineLength(y)
                    }
                    break
                }
                y = (lineOffset + i).toInt()
                if (offsetMouseX >= innerPos.x + getLineWidth(y)) {
                    x = getLineLength(y)
                    break
                }
            }
        }
        for (j in 0 until getLineLength(y)) {
            if (offsetMouseX + 2 >= innerPos.x + getXOffset(y, j) && offsetMouseX + 2 <= innerPos.x + getXOffset(y, j + 1)) {
                x = j
                break
            }
        }
        return Cursor(x, y)
    }

    protected open fun getLineWidth(lineIndex: Int): Int {
        if (lineIndex > lines.size - 1 || lineIndex == -1) {
            return 0
        }
        return (DrawingUtils.TEXT_RENDERER!!.getWidth(lines.get(lineIndex)) * scale).toInt()
    }

    protected open fun getTextSegment(start: Cursor, end: Cursor): String? {
        val startIndex: Int = getStringIndex(start)
        val endIndex: Int = getStringIndex(end)
        if (startIndex < 0 || startIndex > text.length || endIndex < 0 || endIndex > text.length)
            return ""
        return text.substring(startIndex, endIndex)
    }

    protected open fun getStringIndex(cursor: Cursor): Int {
        var i = 0
        for (y in 0 until cursor.y) {
            i += getNewlineLineLength(y)
        }
        i += cursor.x
        return i
    }

    open fun setText(text: String): AbstractTextEditor {
        lines.clear()
        this.text = text
        lines.addAll(text.split("\n"))
        return this
    }

    open fun setText(text: Text): AbstractTextEditor {
        return setText(text.string)
    }

    open fun getLine(index: Int): String {
        if (index < 0 || index > lines.size - 1) return ""
        return lines.get(index)
    }

    protected open fun insertText(insert: String) {
        val cursorIndex: Int = getStringIndex(cursor)
        setText(text.substring(0, cursorIndex) + insert + text.substring(cursorIndex))
    }

    protected open fun deleteText(start: Cursor, end: Cursor) : String {
        val startIndex: Int = getStringIndex(start)
        val endIndex: Int = getStringIndex(end)
        if (endIndex == 0 || startIndex > text.length || endIndex > text.length) return ""
        val deleted: String = text.substring(startIndex, endIndex)
        setText(text.substring(0, startIndex) + text.substring(endIndex))
        return deleted
    }

    override fun tick() {
        focused = focused || active

        if (cursorTick > 0) {
            --cursorTick
        } else {
            cursorTick = 20
        }
        super.tick()
    }

    override fun onKeyPressed(keyCode: Int, scanCode: Int, keyModifiers: Int) {
        if (!active) return
        processKeyActions(keyCode, scanCode, keyModifiers)
        if (selection.first == selection.second) {
            clearSelection()
        }
        cursorTick = 20

        if (active && keyCode == GLFW.GLFW_KEY_E) {
            return
        }

        super.onKeyPressed(keyCode, scanCode, keyModifiers)
    }

    override fun onKeyReleased(keyCode: Int, scanCode: Int, keyModifiers: Int) {
        if (active && keyCode == GLFW.GLFW_KEY_E) {
            return
        }

        super.onKeyReleased(keyCode, scanCode, keyModifiers)
    }

    override fun onCharacterTyped(character: Char, keyCode: Int) {
        if (filter == null || filter!!.accepts(character.toString(), text)) {
            if (active) {
                if (hasSelection()) {
                    cursor.assign(selection.first)
                    deleteText(selection.first, selection.second)
                    clearSelection()
                }
                insertText(character.toString())
                val prevY: Int = cursor.y
                cursor.right()
                if (cursor.y != prevY) {
                    cursor.right()
                }
                onCursorMove()
            }
            cursorTick = 20
        }

        if (active && keyCode == GLFW.GLFW_KEY_E) {
            return
        }

        super.onCharacterTyped(character, keyCode)
    }

    override fun onMouseClicked(x: Float, y: Float, button: Int) {
        if (!hidden && editable && button == 0) {
            active = isPointWithin(x, y)
            cursorTick = 20
            if (active) {
                val mousePos: Cursor = getCursorFromMouse(x, y)
                if (mousePos.present()) {
                    mouseClick.assign(mousePos)
                    cursor.assign(mouseClick)
                    onCursorMove()
                }
                if (hasSelection()) clearSelection()
            }
        }
        super.onMouseClicked(x, y, button)
    }

    override fun onMouseDragged(x: Float, y: Float, button: Int, deltaX: Double, deltaY: Double) {
        if (!hidden && editable && button == 0 && active) {
            cursorTick = 20
            val mousePos: Cursor = getCursorFromMouse(x, y)
            var cursorUpdated = false
            if (mousePos.present()) {
                if (mousePos.lessThan(mouseClick)) {
                    selection.first.assign(mousePos)
                    selection.second.assign(mouseClick)
                } else {
                    selection.first.assign(mouseClick)
                    selection.second.assign(mousePos)
                }
                cursor.assign(mousePos)
                cursorUpdated = true
            }
            if (y > getInnerAnchor().y + getInnerSize().height) {
                cursor.down()
                selection.second.down()
                cursorUpdated = true
            } else if (y < getInnerAnchor().y) {
                cursor.up()
                selection.first.up()
                cursorUpdated = true
            }
            if (cursorUpdated) onCursorMove()
        }
        super.onMouseDragged(x, y, button, deltaX, deltaY)
    }

    override fun onMouseScrolled(x: Float, y: Float, deltaY: Double) {
        val cH: Int = getTextHeight()
        val textHeight: Int = (lines.size - 1) * cH
        if (textHeight <= getInnerSize().height) return
        yOffset += (deltaY * cH).toFloat()
        if (yOffset > 0) yOffset = 0F
        if (yOffset < -textHeight) yOffset = (-textHeight).toFloat()
        super.onMouseScrolled(x, y, deltaY)
    }

    protected open fun getTextHeight(): Int {
        return Math.round(DrawingUtils.TEXT_RENDERER!!.fontHeight * scale)
    }

    /**
     * When injecting custom key action handling logic, you should overload this function
     * and not {@link #onKeyPressed(int, int, int)}!
     */
    // TODO: Comment selection expansion/contraction logic
    // *******************
    // * BLESS THIS MESS *
    // *******************
    protected open fun processKeyActions(keyPressed: Int, character: Int, keyModifier: Int) {
        val prevY: Int
        val prevCursor: Cursor
        when (keyPressed) {
            GLFW.GLFW_KEY_ESCAPE -> active = false
            GLFW.GLFW_KEY_ENTER -> {
                insertText("\n")
                cursor.right()
                onCursorMove()
            }
            GLFW.GLFW_KEY_C -> if (Screen.hasControlDown() && hasSelection()) {
                MinecraftClient.getInstance().keyboard.clipboard =
                    getTextSegment(selection.first, selection.second)
            }
            GLFW.GLFW_KEY_V -> if (Screen.hasControlDown()) {
                val clipboard = MinecraftClient.getInstance().keyboard.clipboard
                if (hasSelection()) {
                    cursor.assign(selection.first)
                    deleteText(selection.first, selection.second)
                    clearSelection()
                }
                insertText(clipboard)
                prevY = cursor.y
                var i = 0
                while (i < clipboard.length) {
                    cursor.right()
                    i++
                }
                if (cursor.y != prevY) cursor.right()
                onCursorMove()
            }
            GLFW.GLFW_KEY_D -> if (Screen.hasControlDown()) {
                cursor.x = 0
                cursor.y = 0
                setText("")
                onCursorMove()
            }
            GLFW.GLFW_KEY_X -> if (Screen.hasControlDown()) {
                MinecraftClient.getInstance().keyboard.clipboard =
                    getTextSegment(selection.first, selection.second)
                cursor.assign(selection.first)
                onCursorMove()
                deleteText(selection.first, selection.second)
                clearSelection()
            }
            GLFW.GLFW_KEY_A -> if (Screen.hasControlDown()) {
                selection.first.assign(Cursor(0, 0))
                selection.second.assign(Cursor(getLineLength(lines.size - 1), lines.size - 1))
                cursor.assign(selection.second)
                onCursorMove()
            }
            GLFW.GLFW_KEY_BACKSPACE -> {
                if (hasSelection()) {
                    cursor.assign(selection.first)
                    deleteText(selection.first, selection.second)
                    clearSelection()
                } else {
                    val lineLength = getLineLength(cursor.y)
                    val deleted = deleteText(cursor.copy().left(), cursor)
                    if (deleted == "\n") {
                        var i = 0
                        while (i < lineLength) {
                            cursor.left()
                            i++
                        }
                    }
                    cursor.left()
                }
                onCursorMove()
            }
            GLFW.GLFW_KEY_DELETE -> if (hasSelection()) {
                cursor.assign(selection.first)
                onCursorMove()
                deleteText(selection.first, selection.second)
                clearSelection()
            } else {
                deleteText(cursor, cursor.copy().right())
            }
            GLFW.GLFW_KEY_LEFT -> {
                if (hasSelection() && !Screen.hasShiftDown()) {
                    clearSelection()
                }
                if (Screen.hasShiftDown()) {
                    if (!hasSelection()) {
                        selection.second.assign(cursor)
                        selection.first.assign(cursor)
                    }
                }
                prevY = cursor.y
                cursor.left()
                if (Screen.hasShiftDown()) {
                    if (cursor.y != prevY) cursor.left()
                    if (cursor.lessThan(selection.first)) {
                        selection.first.left()
                        if (cursor.y != prevY) selection.first.left()
                    } else if (cursor.greaterThan(selection.first)) {
                        selection.second.left()
                        if (cursor.y != prevY) selection.second.left()
                    }
                }
                onCursorMove()
            }
            GLFW.GLFW_KEY_RIGHT -> {
                if (hasSelection() && !Screen.hasShiftDown()) {
                    clearSelection()
                }
                if (Screen.hasShiftDown()) {
                    if (!hasSelection()) {
                        selection.second.assign(cursor)
                        selection.first.assign(cursor)
                    }
                }
                prevY = cursor.y
                cursor.right()
                if (Screen.hasShiftDown()) {
                    if (cursor.y != prevY) cursor.right()
                    if (cursor.greaterThan(selection.second)) {
                        selection.second.right()
                        if (cursor.y != prevY) selection.second.right()
                    } else if (cursor.lessThan(selection.second)) {
                        selection.first.right()
                        if (cursor.y != prevY) selection.first.right()
                    } else {
                        clearSelection()
                    }
                }
                onCursorMove()
            }
            GLFW.GLFW_KEY_UP -> {
                if (hasSelection() && !Screen.hasShiftDown()) {
                    clearSelection()
                }
                if (Screen.hasShiftDown()) {
                    if (!hasSelection()) {
                        selection.second.assign(cursor)
                    } else {
                        if (cursor == selection.first) {
                            selection.first.assign(selection.second)
                        }
                    }
                }
                prevCursor = cursor.copy()
                cursor.up()
                if (Screen.hasShiftDown()) {
                    if (cursor.lessThan(selection.first)) {
                        if (!prevCursor.lessThan(selection.second)) {
                            selection.second.assign(selection.first)
                        }
                        selection.first.assign(cursor)
                    } else {
                        if (!hasSelection()) {
                            selection.second.assign(prevCursor)
                            selection.first.assign(cursor)
                        } else {
                            selection.second.assign(cursor)
                        }
                    }
                }
                onCursorMove()
            }
            GLFW.GLFW_KEY_DOWN -> {
                if (hasSelection() && !Screen.hasShiftDown()) {
                    clearSelection()
                }
                if (Screen.hasShiftDown()) {
                    if (!hasSelection()) {
                        selection.first.assign(cursor)
                    } else {
                        if (cursor == selection.first) {
                            selection.first.assign(selection.second)
                        }
                    }
                }
                cursor.down()
                if (Screen.hasShiftDown()) {
                    if (cursor.greaterThan(selection.second)) {
                        selection.second.assign(cursor)
                    } else if (cursor.lessThan(selection.second)) {
                        selection.first.assign(cursor)
                    }
                }
                onCursorMove()
            }
            GLFW.GLFW_KEY_HOME -> {
                if (hasSelection() && !Screen.hasShiftDown()) {
                    clearSelection()
                }
                if (Screen.hasShiftDown()) {
                    if (!hasSelection()) {
                        selection.second.assign(cursor)
                    } else {
                        if (!cursor.lessThan(selection.second)) {
                            selection.second.assign(selection.first)
                        }
                    }
                }
                cursor.x = 0
                if (Screen.hasControlDown()) cursor.y = 0
                if (Screen.hasShiftDown()) {
                    if (!cursor.lessThan(selection.second)) {
                        selection.second.assign(cursor)
                    } else {
                        selection.first.assign(cursor)
                    }
                }
                onCursorMove()
            }
            GLFW.GLFW_KEY_END -> {
                if (hasSelection() && !Screen.hasShiftDown()) {
                    clearSelection()
                }
                if (Screen.hasShiftDown()) {
                    if (!hasSelection()) {
                        selection.first.assign(cursor)
                    } else {
                        if (!cursor.greaterThan(selection.first)) {
                            selection.first.assign(selection.second)
                        }
                    }
                }
                if (Screen.hasControlDown()) cursor.y = lines.size - 1
                cursor.x = getLineLength(cursor.y)
                if (Screen.hasShiftDown()) {
                    selection.second.assign(cursor)
                }
                onCursorMove()
            }
            GLFW.GLFW_KEY_PAGE_UP -> {
                if (hasSelection() && !Screen.hasShiftDown()) {
                    clearSelection()
                }
                if (Screen.hasShiftDown()) {
                    if (!hasSelection()) {
                        selection.second.assign(cursor)
                    } else {
                        if (cursor == selection.second) {
                            selection.second.assign(selection.first)
                        }
                    }
                }
                var i = 0
                while (i < Math.min(lines.size - 1, getVisibleLines())) {
                    cursor.up()
                    i++
                }
                if (Screen.hasShiftDown()) {
                    selection.first.assign(cursor)
                }
                onCursorMove()
            }
            GLFW.GLFW_KEY_PAGE_DOWN -> {
                if (hasSelection() && !Screen.hasShiftDown()) {
                    clearSelection()
                }
                if (Screen.hasShiftDown()) {
                    if (!hasSelection()) {
                        selection.first.assign(cursor)
                    } else {
                        if (cursor == selection.first) {
                            selection.first.assign(selection.second)
                        }
                    }
                }
                var i = 0
                while (i < Math.min(lines.size - 1, getVisibleLines())) {
                    cursor.down()
                    i++
                }
                if (Screen.hasShiftDown()) {
                    selection.second.assign(cursor)
                }
                onCursorMove()
            }
        }
    }

    protected open fun onCursorMove() {
        val innerPos = getInnerAnchor()
        val innerSize = getInnerSize()
        val innerX: Float = innerPos.x
        val innerY: Float = innerPos.y
        val innerWidth: Float = innerSize.width
        val innerHeight: Float = innerSize.height
        val lineOffset: Float = getLineOffset()
        val cH = getTextHeight()
        val cursorX = innerX + getXOffset(cursor.y, cursor.x) - 1
        val cursorY = innerY + (cH + 2) * (cursor.y - lineOffset) - 2
        val offsetCursorX = cursorX + xOffset
        val yRenderOffset = yOffset + lineOffset * cH
        val offsetCursorStartY = cursorY + yRenderOffset
        val offsetCursorEndY = cursorY + yRenderOffset + cH
        if (offsetCursorX > innerX + innerWidth) {
            xOffset -= offsetCursorX - (innerX + innerWidth) + 2
        } else if (offsetCursorX < innerX) {
            val compensateOffset = xOffset + (innerX - offsetCursorX)
            val widthOffset = xOffset + innerWidth
            xOffset = Math.min(0f, Math.max(widthOffset, compensateOffset))
        }
        if (offsetCursorEndY > innerY + innerHeight) {
            yOffset -= offsetCursorEndY - (innerY + innerHeight)
        } else if (offsetCursorStartY < innerY) {
            yOffset = Math.min(0f, yOffset + innerY - (offsetCursorStartY + 2))
        }
    }

    protected open fun renderField(matrices: MatrixStack, provider: VertexConsumerProvider) {
        val z: Float = z

        val innerPos: Position = getInnerAnchor()
        val innerSize: Size = getInnerSize()
        val innerX = innerPos.x
        val innerY = innerPos.y
        val innerWidth = innerSize.width
        val innerHeight = innerSize.height

        if (isEmpty() && !active) {
            matrices.scale(scale, scale, 0F)
            DrawingUtils.TEXT_RENDERER!!.drawWithShadow(matrices, label, innerX, innerY, 0xFFFFFF)
            return
        }

        val glScale = MinecraftClient.getInstance().window.scaleFactor
        val rawHeight = MinecraftClient.getInstance().window.height

        //val area = Scissors((((innerX - 1) * glScale).toInt()).toFloat(), ((rawHeight - ((innerY - 2) * glScale + innerHeight * glScale)).toInt()).toFloat(), ((innerWidth * glScale).toInt()).toFloat(), ((innerHeight * glScale).toInt()).toFloat(), provider)

        val lineOffset: Float = getLineOffset()
        val cH = getTextHeight().toFloat()
        val cursorX = innerX + getXOffset(cursor.y, cursor.x) - 1
        val cursorY = innerY + (cH + 2) * (cursor.y - lineOffset) - 2
        val yRenderOffset = yOffset + lineOffset * cH

        matrices.push()
        matrices.translate(xOffset.toDouble(), yRenderOffset.toDouble(), 0.0)
        for (i in lineOffset.toInt() until (lineOffset + getVisibleLines()).toInt()) {
            if (i < 0 || !isLineVisible(i) || i > lines.size - 1) continue
            val adjustedI = i - lineOffset
            val line = lines[i]
            matrices.scale(scale, scale, 0F)
            DrawingUtils.TEXT_RENDERER!!.drawWithShadow(matrices, line, innerX, innerY + (cH + 2) * adjustedI, 0xFFFFFF)
//            TextRenderer.pass().text(line).at(innerX, innerY + (cH + 2) * adjustedI, z).scale(scale)
//                .shadow(getStyle().asBoolean("text.shadow")).shadowColor(getStyle().asColor("text.shadow_color"))
//                .color(getStyle().asColor("text.color"))
//                .render(matrices, provider)
            if (getSelectedChars(i) != null) {
                val selectedChars: Pair<Int, Int> = getSelectedChars(i)!!
                val selW = getXOffset(i, selectedChars.second) - getXOffset(i, selectedChars.first)
                DrawingUtils.drawQuad(
                    matrices,
                    provider,
                    innerX + getXOffset(i, selectedChars.first),
                    innerY + (cH + 2) * adjustedI,
                    selW,
                    cH + 1,
                    z,
                    color = Color(0.33F, 0.33F, 1.0F, 0.5F)
                )
            }
        }
        if (active && cursorTick > 10) {
            DrawingUtils.drawQuad(matrices, provider, cursorX, cursorY, 1F, cH + 2, z,
                color = Color(0.75F, 0.75F, 0.75F, 0.8F))
        }
        matrices.pop()

        //area.destroy()
    }

    open fun isEmpty(): Boolean {
        return text.isEmpty()
    }

    protected open fun getLineOffset(): Float {
        return (-yOffset / getTextHeight())
    }

    protected open fun getXOffset(lineIndex: Int, charIndex: Int): Float {
        if (charIndex < 0 || lines.isEmpty() || lineIndex > lines.size - 1) return 0F
        val line = lines[lineIndex]
        val endIndex = Math.min(charIndex, line.length)
        return DrawingUtils.TEXT_RENDERER!!.getWidth(line.substring(0, endIndex)) * scale
    }

    protected open fun getVisibleLines(): Int {
        return (Math.max(1F, getInnerSize().height / getTextHeight())).toInt()
    }

    protected open fun isLineVisible(line: Int): Boolean {
        val lineOffset: Float = getLineOffset()
        return line >= lineOffset && line <= lineOffset + getVisibleLines()
    }

    protected open fun getSelectedChars(lineIndex: Int) : Pair<Int, Int>? {
        if (!selection.first.present() || !selection.second.present()) return null
        if (lineIndex < selection.first.y || lineIndex > selection.second.y) return null
        val left = if (selection.first.y == lineIndex) selection.first.x else 0
        val right = if (selection.second.y == lineIndex) selection.second.x else getLineLength(lineIndex)
        return Pair(left, right)
    }

    protected open fun getLineLength(index: Int): Int {
        if (lines.isEmpty()) return 0
        if (index < 0 || index > lines.size - 1) return 0
        return lines[index].length
    }

    open fun <W : AbstractTextEditor?> setScale(scale: Float): W {
        this.scale = scale
        return this as W
    }

    open fun <W : AbstractTextEditor?> setEditable(editable: Boolean): W {
        this.editable = editable
        return this as W
    }

    open fun <W : BaseWidget?> setActive(active: Boolean): W {
        this.active = active
        return this as W
    }

    protected inner class Cursor(var x: Int, var y: Int) {

        fun left(): Cursor {
            x--
            if (x < 0) {
                val prevY: Int = y
                up()
                if (y != prevY) {
                    x = getLineLength(y)
                } else {
                    x = 0
                }
            }
            return this
        }

        fun up(): Cursor {
            y = Math.max(y - 1, 0)
            if (x > getLineLength(y)) {
                x = getLineLength(y)
            }
            return this
        }

        fun right(): Cursor {
            x++
            if (x > getLineLength(y)) {
                val prevY: Int = y
                down()
                if (y != prevY) {
                    x = 0
                } else {
                    x = getLineLength(y)
                }
            }
            return this
        }

        fun down(): Cursor {
            y = Math.min(y + 1, lines.size - 1)
            if (x > getLineLength(y)) {
                x = getLineLength(y)
            }
            return this
        }

        fun present(): Boolean {
            return x != -1 && y != -1
        }

        fun assign(cursor: Cursor): Cursor {
            x = cursor.x
            y = cursor.y
            return this
        }

        fun copy(): Cursor {
            return Cursor(x, y)
        }

        fun lessThan(cursor: Cursor): Boolean {
            return getStringIndex(this) < getStringIndex(cursor)
        }

        fun greaterThan(cursor: Cursor): Boolean {
            return getStringIndex(this) > getStringIndex(cursor)
        }

        override fun hashCode(): Int {
            return Objects.hash(x, y)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || javaClass != other.javaClass) return false
            val cursor: Cursor = other as Cursor
            return x == cursor.x && y == cursor.y
        }
    }

}