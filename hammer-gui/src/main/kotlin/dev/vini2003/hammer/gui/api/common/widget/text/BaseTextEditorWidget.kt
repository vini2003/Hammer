package dev.vini2003.hammer.gui.api.common.widget.text

import dev.vini2003.hammer.core.api.common.color.Color
import dev.vini2003.hammer.core.api.common.math.position.Position
import dev.vini2003.hammer.gui.api.common.filter.InputFilter
import dev.vini2003.hammer.gui.api.common.widget.BaseWidget
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import org.lwjgl.glfw.GLFW
import java.lang.Float.max
import kotlin.math.min
import kotlin.math.round

/**
 * A [BaseTextEditorWidget] is a widget that contains a text editor.
 */
abstract class BaseTextEditorWidget : BaseWidget() {
	protected open val lines: MutableList<String> = mutableListOf()
	
	protected open val cursor: Cursor = Cursor(0, 0)
	
	protected open val prevMouseCursor: Cursor = Cursor(0, 0)
	
	protected open val selection: Selection = Selection(Cursor(-1, -1), Cursor(-1, -1))
	
	protected open var text: String = ""
		get() = field
		set(value) {
			lines.clear()
			
			field = value
			
			lines.addAll(value.split("\n"))
		}
	
	protected open var scale: Float = 1.0F
	
	protected open var editable: Boolean = true

	protected open var xOffset: Float = 0F
	protected open var yOffset: Float = 0F
	
	protected open var cursorTick: Int = 20
	
	protected open var filter: InputFilter<*>? = null
	
	protected open val innerSize: Size
		get() = Size(size.width - 6.0F, size.height - 6.0F)
	
	protected open val innerPos: Position
		get() = Position(this, 3.0F, 3.0F)
	
	protected open val charHeight: Int
		get() = round(DrawingUtils.TEXT_RENDERER!!.fontHeight * scale).toInt()
	
	protected open val charWidth: Int
		get() {
			val textRenderer = DrawingUtils.TEXT_RENDERER ?: return 0
			
			return (textRenderer.getWidth("m") * scale).toInt()
		}
	
	protected open val lineOffset: Float
		get() = -yOffset / charHeight
	
	protected open val visibleLines: Int
		get() = max(1.0F, innerSize.height / charHeight).toInt()
	
	protected open val visibleColumns: Int
		get() = (innerSize.width / charWidth).toInt()
	
	open var label: String = "Enter text here!"
	
	protected open fun hasSelection(): Boolean {
		return selection.left.isWithinEditor() && selection.right.isWithinEditor()
	}
	
	protected open fun clearSelection() {
		selection.left = Cursor(-1, -1)
		selection.right = Cursor(-1, -1)
	}
	
	protected open fun isEmpty(): Boolean {
		return text.isEmpty()
	}
	
	protected open fun isLineVisible(line: Int): Boolean {
		return line >= lineOffset && line <= lineOffset + visibleLines
	}
	
	protected open fun getXOffset(lineIndex: Int, charIndex: Int): Float {
		val textRenderer = DrawingUtils.TEXT_RENDERER ?: return 0.0F
		
		if (charIndex < 0 || lines.isEmpty() || lineIndex > lines.size - 1) {
			return 0.0F
		}
		
		val line = lines[lineIndex]
		
		val endIndex = min(charIndex, line.length)
		
		return textRenderer.getWidth(line.substring(0, endIndex)) * scale
	}
	
	protected open fun getSelectedChars(lineIndex: Int): Pair<Int, Int>? {
		if (!selection.left.isWithinEditor() || !selection.right.isWithinEditor()) {
			return null
		}
		
		if (lineIndex < selection.left.y || lineIndex > selection.right.y) {
			return null
		}
		
		val left: Int
		
		if (selection.left.y == lineIndex) {
			left = selection.left.x
		} else {
			left = 0
		}
		
		val right: Int
		
		if (selection.right.y == lineIndex) {
			right = selection.right.x
		} else {
			right = getLineLength(lineIndex)
		}
		
		return Pair(left, right)
	}
	
	protected open fun getCursorFromMouse(mouseX: Float, mouseY: Float): Cursor {
		var x = -1
		var y = -1
		
		val offsetMouseX = -xOffset + mouseX
		
		for (i in 0 until visibleLines) {
			if (mouseY >= innerPos.y + (charHeight + 2) * i && mouseY <= innerPos.y + (charHeight + 2) * i + charHeight) {
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
	
	protected open fun getNewlineLineLength(index: Int): Int {
		return getLineLength(index) + 1
	}
	
	protected open fun getLineLength(index: Int): Int {
		if (lines.isEmpty()) {
			return 0
		}
		
		if (index < 0 || index > lines.size - 1) {
			return 0
		}
		
		return lines[index].length
	}
	
	protected open fun getLineWidth(lineIndex: Int): Int {
		val textRenderer = DrawingUtils.TEXT_RENDERER ?: return 0
		
		if (lineIndex > lines.size - 1 || lineIndex == -1) {
			return 0
		}
		
		return (textRenderer.getWidth(lines.get(lineIndex)) * scale).toInt()
	}
	
	protected open fun getTextSegment(start: Cursor, end: Cursor): String {
		val startIndex = getStringIndex(start)
		val endIndex = getStringIndex(end)
		
		if (startIndex < 0 || startIndex > text.length || endIndex < 0 || endIndex > text.length) {
			return ""
		}
		
		return text.substring(startIndex, endIndex)
	}
	
	protected open fun getStringIndex(cursor: Cursor): Int {
		var index = 0
		
		for (i in 0 until cursor.y) {
			index += getNewlineLineLength(i)
		}
		
		index += cursor.x
		
		return index
	}
	
	open fun getLine(index: Int): String {
		if (index < 0 || index > lines.size - 1) return ""
		return lines.get(index)
	}
	
	protected open fun insertText(insert: String) {
		val cursorIndex = getStringIndex(cursor)
		
		text = (text.substring(0, cursorIndex) + insert + text.substring(cursorIndex))
	}
	
	protected open fun deleteText(start: Cursor, end: Cursor): String {
		val startIndex: Int = getStringIndex(start)
		val endIndex: Int = getStringIndex(end)
		
		if (endIndex == 0 || startIndex > text.length || endIndex > text.length) {
			return ""
		}
		
		val deleted = text.substring(startIndex, endIndex)
		
		text = (text.substring(0, startIndex) + text.substring(endIndex))
		
		return deleted
	}
	
	override fun tick() {
		focused = focused || locking
		
		if (cursorTick > 0) {
			--cursorTick
		} else {
			cursorTick = 20
		}
		
		super.tick()
	}
	
	override fun onKeyPressed(keyCode: Int, scanCode: Int, keyModifiers: Int) {
		if (locking && keyCode == GLFW.GLFW_KEY_E) {
			return
		}
		
		if (!locking) {
			return
		}
		
		processKeyActions(keyCode, scanCode, keyModifiers)
		
		if (selection.left == selection.right) {
			clearSelection()
		}
		
		cursorTick = 20
		
		if (locking && keyCode == GLFW.GLFW_KEY_E) {
			return
		}
		
		super.onKeyPressed(keyCode, scanCode, keyModifiers)
	}
	
	override fun onKeyReleased(keyCode: Int, scanCode: Int, keyModifiers: Int) {
		if (locking && keyCode == GLFW.GLFW_KEY_E) {
			return
		}
		
		super.onKeyReleased(keyCode, scanCode, keyModifiers)
	}
	
	override fun onCharacterTyped(character: Char, keyCode: Int) {
		if (filter == null || filter?.accepts(character.toString(), text) == true) {
			if (locking) {
				if (hasSelection()) {
					cursor.x = selection.left.x
					cursor.y = selection.left.y
					
					deleteText(selection.left, selection.right)
					
					clearSelection()
				}
				
				insertText(character.toString())
				
				val prevY = cursor.y
				
				cursor.moveRight()
				
				if (cursor.y != prevY) {
					cursor.moveRight()
				}
				
				onCursorMove()
			}
			
			cursorTick = 20
		}
		
		if (locking && keyCode == GLFW.GLFW_KEY_E) {
			return
		}
		
		super.onCharacterTyped(character, keyCode)
	}
	
	override fun onMouseClicked(x: Float, y: Float, button: Int) {
		if (!hidden && editable && button == 0) {
			locking = isPointWithin(x, y)
			
			cursorTick = 20
			
			if (locking) {
				val mouseClickCursor = getCursorFromMouse(x, y)
				
				if (mouseClickCursor.isWithinEditor()) {
					this.prevMouseCursor.x = mouseClickCursor.x
					this.prevMouseCursor.y = mouseClickCursor.y
					
					this.cursor.x = mouseClickCursor.x
					this.cursor.y = mouseClickCursor.y
					
					onCursorMove()
				}
				
				if (hasSelection()) {
					clearSelection()
				}
			}
		}
		
		super.onMouseClicked(x, y, button)
	}
	
	override fun onMouseDragged(x: Float, y: Float, button: Int, deltaX: Double, deltaY: Double) {
		if (!hidden && editable && button == 0 && locking) {
			cursorTick = 20
			
			val mouseCursor = getCursorFromMouse(x, y)
			
			var cursorUpdated = false
			
			if (mouseCursor.isWithinEditor()) {
				if (mouseCursor.lessThan(prevMouseCursor)) {
					selection.left.x = mouseCursor.x
					selection.left.y = mouseCursor.x
					
					selection.right.x = prevMouseCursor.x
					selection.right.y = prevMouseCursor.y
				} else {
					selection.left.x = prevMouseCursor.x
					selection.left.y = prevMouseCursor.y
					
					selection.right.x = mouseCursor.x
					selection.right.y = mouseCursor.y
				}
				
				cursor.x = mouseCursor.x
				cursor.y = mouseCursor.y
				
				cursorUpdated = true
			}
			
			if (y > innerPos.y + innerSize.height) {
				cursor.moveDown()
				
				selection.right.moveDown()
				
				cursorUpdated = true
			} else if (y < innerPos.y) {
				cursor.moveUp()
				
				selection.left.moveUp()
				
				cursorUpdated = true
			}
			
			if (cursorUpdated) {
				onCursorMove()
			}
		}
		
		super.onMouseDragged(x, y, button, deltaX, deltaY)
	}
	
	override fun onMouseScrolled(x: Float, y: Float, deltaY: Double) {
		val textHeight = (lines.size - 1) * charHeight
		
		if (textHeight <= innerSize.height) {
			return
		}
		
		yOffset += (deltaY * charHeight).toFloat()
		
		if (yOffset > 0.0F) {
			yOffset = 0.0F
		}
		
		if (yOffset < -textHeight) {
			yOffset = (-textHeight).toFloat()
		}
		
		super.onMouseScrolled(x, y, deltaY)
	}
	
	protected open fun processKeyActions(keyPressed: Int, character: Int, keyModifier: Int) {
		val prevY: Int
		
		val prevCursor: Cursor
		
		when (keyPressed) {
			GLFW.GLFW_KEY_ESCAPE -> locking = false
			
			GLFW.GLFW_KEY_ENTER -> {
				insertText("\n")
				
				cursor.moveRight()
				
				onCursorMove()
			}
			
			GLFW.GLFW_KEY_C -> {
				if (Screen.hasControlDown() && hasSelection()) {
					val client = InstanceUtils.CLIENT ?: return
					
					client.keyboard.clipboard = getTextSegment(selection.left, selection.right)
				}
			}
			
			GLFW.GLFW_KEY_V -> {
				val client = InstanceUtils.CLIENT ?: return
				
				if (Screen.hasControlDown()) {
					val clipboard = client.keyboard.clipboard
					
					if (hasSelection()) {
						cursor.x = selection.left.x
						cursor.y = selection.left.y
						
						deleteText(selection.left, selection.right)
						
						clearSelection()
					}
					
					insertText(clipboard)
					
					prevY = cursor.y
					
					var i = 0
					
					while (i < clipboard.length) {
						cursor.moveRight()
						
						i++
					}
					
					if (cursor.y != prevY) {
						cursor.moveRight()
					}
					
					onCursorMove()
				}
			}
			
			GLFW.GLFW_KEY_D -> {
				if (Screen.hasControlDown()) {
					cursor.x = 0
					cursor.y = 0
					
					text = ""
					
					onCursorMove()
				}
			}
			
			GLFW.GLFW_KEY_X -> {
				if (Screen.hasControlDown()) {
					val client = InstanceUtils.CLIENT ?: return
					
					client.keyboard.clipboard = getTextSegment(selection.left, selection.right)
					
					cursor.x = selection.left.x
					cursor.y = selection.left.y
					
					onCursorMove()
					
					deleteText(selection.left, selection.right)
					
					clearSelection()
				}
			}
			
			GLFW.GLFW_KEY_A -> {
				if (Screen.hasControlDown()) {
					selection.left.x = 0
					selection.left.y = 0
					
					selection.right.x = (getLineLength(lines.size - 1))
					selection.right.y = lines.size - 1

					cursor.x = selection.right.x
					cursor.y = selection.right.y
					
					onCursorMove()
				}
			}
			
			GLFW.GLFW_KEY_BACKSPACE -> {
				if (hasSelection()) {
					cursor.x = selection.left.x
					cursor.y = selection.left.y
					
					deleteText(selection.left, selection.right)
					
					clearSelection()
				} else {
					val lineLength = getLineLength(cursor.y)
					
					val deletionCursor = Cursor(cursor.x, cursor.y)
					deletionCursor.moveLeft()
					
					val deleted = deleteText(deletionCursor, cursor)
					
					if (deleted == "\n") {
						var i = 0
						
						while (i < lineLength) {
							cursor.moveLeft()
							
							i++
						}
					}
					
					cursor.moveLeft()
				}
				
				onCursorMove()
			}
			
			GLFW.GLFW_KEY_DELETE -> {
				if (hasSelection()) {
					cursor.x = selection.left.x
					cursor.y = selection.left.y
					
					onCursorMove()
					
					deleteText(selection.left, selection.right)
					
					clearSelection()
				} else {
					val deletionCursor = Cursor(cursor.x, cursor.y)
					deletionCursor.moveRight()
					
					deleteText(cursor, deletionCursor)
				}
			}
			
			GLFW.GLFW_KEY_LEFT -> {
				if (hasSelection() && !Screen.hasShiftDown()) {
					clearSelection()
				}
				
				if (Screen.hasShiftDown()) {
					if (!hasSelection()) {
						selection.right.x = cursor.x
						selection.right.y = cursor.y
						
						selection.left.x = cursor.x
						selection.left.y = cursor.y
					}
				}
				
				prevY = cursor.y
				
				cursor.moveLeft()
				
				if (Screen.hasShiftDown()) {
					if (cursor.y != prevY) {
						cursor.moveLeft()
					}
					
					if (cursor.lessThan(selection.left)) {
						selection.left.moveLeft()
						
						if (cursor.y != prevY) {
							selection.left.moveLeft()
						}
					} else if (cursor.greaterThan(selection.left)) {
						selection.right.moveLeft()
						
						if (cursor.y != prevY) {
							selection.right.moveLeft()
						}
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
						selection.right.x = cursor.x
						selection.right.y = cursor.y
						
						selection.left.x = cursor.x
						selection.left.y = cursor.y
					}
				}
				
				prevY = cursor.y
				
				cursor.moveRight()
				
				if (Screen.hasShiftDown()) {
					if (cursor.y != prevY) {
						cursor.moveRight()
					}
					
					if (cursor.greaterThan(selection.right)) {
						selection.right.moveRight()
						
						if (cursor.y != prevY) {
							selection.right.moveRight()
						}
					} else if (cursor.lessThan(selection.right)) {
						selection.left.moveRight()
						
						if (cursor.y != prevY) {
							selection.left.moveRight()
						}
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
						selection.right.x = cursor.x
						selection.right.y = cursor.y
					} else {
						if (cursor == selection.left) {
							selection.left.x = selection.right.x
							selection.left.y = selection.right.y
						}
					}
				}
				
				prevCursor = Cursor(cursor.x, cursor.y)
				
				cursor.moveUp()
				
				if (Screen.hasShiftDown()) {
					if (cursor.lessThan(selection.left)) {
						if (!prevCursor.lessThan(selection.right)) {
							selection.right.x = selection.left.x
							selection.right.y = selection.left.y
						}
						
						selection.left.x = cursor.x
						selection.left.y = cursor.y
					} else {
						if (!hasSelection()) {
							selection.right.x = prevCursor.x
							selection.right.y = prevCursor.y
							
							selection.left.x = cursor.x
							selection.left.y = cursor.y
						} else {
							selection.right.x = cursor.x
							selection.right.y = cursor.y
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
						selection.left.x = cursor.x
						selection.left.y = cursor.y
					} else {
						if (cursor == selection.left) {
							selection.left.x = selection.right.x
							selection.left.y = selection.right.y
						}
					}
				}
				
				cursor.moveDown()
				
				if (Screen.hasShiftDown()) {
					if (cursor.greaterThan(selection.right)) {
						selection.right.x = cursor.x
						selection.right.y = cursor.y
					} else if (cursor.lessThan(selection.right)) {
						selection.left.x = cursor.x
						selection.left.y = cursor.y
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
						selection.right.x = cursor.x
						selection.right.y = cursor.y
					} else {
						if (!cursor.lessThan(selection.right)) {
							selection.right.x = selection.left.x
							selection.right.y = selection.left.y
						}
					}
				}
				
				cursor.x = 0
				
				if (Screen.hasControlDown()) {
					cursor.y = 0
				}
				
				if (Screen.hasShiftDown()) {
					if (!cursor.lessThan(selection.right)) {
						selection.right.x = cursor.x
						selection.right.y = cursor.y
					} else {
						selection.left.x = cursor.x
						selection.left.y = cursor.y
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
						selection.left.x = cursor.x
						selection.left.y = cursor.y
					} else {
						if (!cursor.greaterThan(selection.left)) {
							selection.left.x = selection.right.x
							selection.left.y = selection.right.y
						}
					}
				}
				if (Screen.hasControlDown()) {
					cursor.y = lines.size - 1
				}
				
				cursor.x = getLineLength(cursor.y)
				
				if (Screen.hasShiftDown()) {
					selection.right.x = cursor.x
					selection.right.y = cursor.y
				}
				
				onCursorMove()
			}
			
			GLFW.GLFW_KEY_PAGE_UP -> {
				if (hasSelection() && !Screen.hasShiftDown()) {
					clearSelection()
				}
				
				if (Screen.hasShiftDown()) {
					if (!hasSelection()) {
						selection.right.x = cursor.x
						selection.right.y = cursor.y
					} else {
						if (cursor == selection.right) {
							selection.right.x = selection.left.x
							selection.right.y = selection.left.y
						}
					}
				}
				
				var i = 0
				
				while (i < min(lines.size - 1, visibleLines)) {
					cursor.moveUp()
					
					i++
				}
				
				if (Screen.hasShiftDown()) {
					selection.left.x = cursor.x
					selection.left.y = cursor.y
				}
				
				onCursorMove()
			}
			
			GLFW.GLFW_KEY_PAGE_DOWN -> {
				if (hasSelection() && !Screen.hasShiftDown()) {
					clearSelection()
				}
				
				if (Screen.hasShiftDown()) {
					if (!hasSelection()) {
						selection.left.x = cursor.x
						selection.left.y = cursor.y
					} else {
						if (cursor == selection.left) {
							selection.left.x = selection.right.x
							selection.left.y = selection.right.y
						}
					}
				}
				
				var i = 0
				
				while (i < min(lines.size - 1, visibleLines)) {
					cursor.moveDown()
					
					i++
				}
				
				if (Screen.hasShiftDown()) {
					selection.right.x = cursor.x
					selection.right.y = cursor.y
				}
				
				onCursorMove()
			}
		}
	}
	
	protected open fun onCursorMove() {
		val innerX = innerPos.x
		val innerY = innerPos.y
		
		val innerWidth = innerSize.width
		val innerHeight = innerSize.height
		
		val cursorX = innerX + getXOffset(cursor.y, cursor.x) - 1.0F
		val cursorY = innerY + (charHeight + 2.0F) * (cursor.y - lineOffset) - 2.0F
		
		val offsetCursorX = cursorX + xOffset
		
		val yRenderOffset = yOffset + lineOffset * charHeight
		
		val offsetCursorStartY = cursorY + yRenderOffset
		val offsetCursorEndY = cursorY + yRenderOffset + charHeight
		
		if (offsetCursorX > innerX + innerWidth) {
			xOffset -= offsetCursorX - (innerX + innerWidth) + 2
		} else if (offsetCursorX < innerX) {
			val compensateOffset = xOffset + (innerX - offsetCursorX)
			val widthOffset = xOffset + innerWidth
			
			xOffset = min(0.0F, max(widthOffset, compensateOffset))
		}
		
		if (offsetCursorEndY > innerY + innerHeight) {
			yOffset -= offsetCursorEndY - (innerY + innerHeight)
		} else if (offsetCursorStartY < innerY) {
			yOffset = min(0.0F, yOffset + innerY - (offsetCursorStartY + 2.0F))
		}
	}
	
	protected open fun renderField(matrices: MatrixStack, provider: VertexConsumerProvider) {
		val textRenderer = DrawingUtils.TEXT_RENDERER ?: return
		
		val innerX = innerPos.x
		val innerY = innerPos.y
		
		if (isEmpty() && !locking) {
			matrices.scale(scale, scale, 0.0F)
			
			textRenderer.drawWithShadow(matrices, label, innerX, innerY, 0xFFFFFF)
			
			return
		}

		val cursorX = innerX + getXOffset(cursor.y, cursor.x) - 1.0F
		val cursorY = innerY + (charHeight + 2.0F) * (cursor.y - lineOffset) - 2.0F
		
		val yRenderOffset = yOffset + lineOffset * charHeight
		
		matrices.push()
		
		matrices.translate(xOffset.toDouble(), yRenderOffset.toDouble(), 0.0)
		
		for (i in lineOffset.toInt() until (lineOffset + visibleLines).toInt()) {
			if (i < 0 || !isLineVisible(i) || i > lines.size - 1.0F) {
				continue
			}
			
			val adjustedI = i - lineOffset
			val line = lines[i]
			
			matrices.scale(scale, scale, 0F)
			
			textRenderer.drawWithShadow(matrices, line, innerX, innerY + (charHeight + 2.0F) * adjustedI, 0xFFFFFF)

			if (getSelectedChars(i) != null) {
				val selectedChars = getSelectedChars(i)!!
				val selectionWidth = getXOffset(i, selectedChars.second) - getXOffset(i, selectedChars.first)
				
				DrawingUtils.drawQuad(
					matrices=  matrices,
					provider = provider,
					x = innerX + getXOffset(i, selectedChars.first),
					y = innerY + (charHeight + 2.0F) * adjustedI,
					width = selectionWidth,
					height = charHeight + 1.0F,
					z = z,
					color = Color(0.33F, 0.33F, 1.0F, 0.5F)
				)
			}
		}
		
		if (locking && cursorTick > 10) {
			DrawingUtils.drawQuad(
				matrices = matrices,
				provider = provider,
				x = cursorX,
				y = cursorY,
				width = 1.0F,
				height = charHeight + 2.0F,
				z = z,
				color = Color(0.75F, 0.75F, 0.75F, 0.8F))
		}
		
		matrices.pop()
	}
	
	inner class Selection(
		var left: Cursor,
		var right: Cursor
	)
	
	inner class Cursor(
		var x: Int,
		var y: Int
	) {
		fun moveLeft() {
			x -= 1
			
			if (x < 0) {
				val prevY = y
				
				moveUp()
				
				if (y != prevY) {
					x = getLineLength(y)
				} else {
					x = 0
				}
			}
		}
		
		fun moveUp()  {
			y = max(y - 1.0F, 0.0F).toInt()
			
			if (x > getLineLength(y)) {
				x = getLineLength(y)
			}
		}
		
		fun moveRight() {
			x++
			
			if (x > getLineLength(y)) {
				val prevY = y
				
				moveDown()
				
				if (y != prevY) {
					x = 0
				} else {
					x = getLineLength(y)
				}
			}
		}
		
		fun moveDown() {
			y = min(y + 1.0F, lines.size - 1.0F).toInt()
			
			if (x > getLineLength(y)) {
				x = getLineLength(y)
			}
		}
		
		fun isWithinEditor(): Boolean {
			return x != -1 && y != -1
		}
		
		fun lessThan(cursor: Cursor): Boolean {
			return getStringIndex(this) < getStringIndex(cursor)
		}
		
		fun greaterThan(cursor: Cursor): Boolean {
			return getStringIndex(this) > getStringIndex(cursor)
		}
	}
}