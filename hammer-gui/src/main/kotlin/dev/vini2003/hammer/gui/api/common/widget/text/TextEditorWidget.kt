package dev.vini2003.hammer.gui.api.common.widget.text

import dev.vini2003.hammer.core.HC
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import org.lwjgl.glfw.GLFW
import java.lang.Math.round

/**
 * A [TextEditorWidget] is a large text editor, with multiple lines.
 */
open class TextEditorWidget : BaseTextEditorWidget() {
	companion object {
		@JvmField
		val STANDARD_TEXTURE: BaseTexture = PartitionedTexture(
			HC.id("textures/widget/text_area.png"),
			18.0F,
			18.0F,
			0.055F,
			0.055F,
			0.055F,
			0.055F
		)
	}
	
	var texture: BaseTexture = STANDARD_TEXTURE
	
	protected val newLine: MutableList<Boolean> = ArrayList()
	
	protected var lineWrap: Boolean = true

	protected open fun hasNewline(index: Int): Boolean {
		if (lineWrap) {
			return newLine[index]
		} else {
			return true
		}
	}
	
	protected open fun getNewlinedLineLength(index: Int): Int {
		val lineLength = getLineLength(index)
		
		if (hasNewline(index)) {
			return lineLength + 1
		} else {
			return lineLength
		}
	}
	
	override fun getStringIndex(cursor: Cursor): Int {
		var index = 0
		
		for (y in 0 until cursor.y) {
			index += getNewlinedLineLength(y)
		}
		
		index += cursor.x
		
		return index
	}
	
	override var text: String = ""
		get() = field
		set(value) {
			if (lineWrap) {
				field = value
				
				lines.clear()
				
				newLine.clear()
				
				var currentLine = ""
				
				var lineWidth = 0
				
				for (char in text.toCharArray()) {
					val textRenderer = DrawingUtils.TEXT_RENDERER ?: continue
					
					lineWidth += round(textRenderer.getWidth(char.toString()) * scale)
					
					if (lineWidth > innerSize.width || char == '\n') {
						lines.add(currentLine)
						
						newLine.add(char == '\n')
						
						lineWidth = round(textRenderer.getWidth(char.toString()) * scale)
						
						currentLine = ""
					}
					
					if (char != '\n') {
						currentLine += char
					}
				}
				
				val finalLine = currentLine.toString()
				
				if (!finalLine.isEmpty() || text.endsWith("\n")) {
					newLine.add(text.endsWith("\n"))
					
					lines.add(finalLine)
					
					newLine.add(true)
				}
			} else {
				super.text = value
			}
		}
	
	override fun processKeyActions(keyPressed: Int, character: Int, keyModifier: Int) {
		when (keyPressed) {
			GLFW.GLFW_KEY_BACKSPACE -> {
				if (!hasSelection()) {
					val lineLength = getLineLength(cursor.y)
					
					var deletionCursor = Cursor(cursor.x, cursor.y)
					deletionCursor.moveLeft()
					
					val deleted = deleteText(deletionCursor, cursor)
					
					if (deleted == "") {
						cursor.moveLeft()
						
						deletionCursor = Cursor(cursor.x, cursor.y)
						deletionCursor.moveLeft()
						
						deleteText(deletionCursor, cursor)
					}
					
					if (deleted == "\n") {
						for (i in 0 until lineLength) {
							cursor.moveLeft()
						}
					}
					
					cursor.moveLeft()
					
					return
				}
			}
			
			GLFW.GLFW_KEY_DELETE -> {
				if (!hasSelection()) {
					var deletionCursor = Cursor(cursor.x, cursor.y)
					deletionCursor.moveRight()
					
					val deleted = deleteText(cursor, deletionCursor)
					
					if (deleted == "") {
						deletionCursor = Cursor(cursor.x, cursor.y)
						deletionCursor.moveRight()
						deletionCursor.moveRight()
						
						deleteText(cursor, deletionCursor)
					}
					
					return
				}
			}
		}
		
		super.processKeyActions(keyPressed, character, keyModifier)
	}
	
	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, tickDelta: Float) {
		if (hidden) {
			return
		}
		
		matrices.push()
		
		texture.draw(matrices, provider, position.x, position.y, size.width, size.height)
		
		if (lineWrap && xOffset != 0.0F) {
			xOffset = 0.0F
		}
		
		renderField(matrices, provider)
		
		matrices.pop()
		
		super.drawWidget(matrices, provider, tickDelta)
	}
}