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

package dev.vini2003.hammer.gui.api.client.screen

import dev.vini2003.hammer.gui.api.common.widget.BaseWidget
import dev.vini2003.hammer.gui.api.common.widget.BaseWidgetCollection
import dev.vini2003.hammer.core.api.client.util.InstanceUtils
import dev.vini2003.hammer.core.api.common.math.position.Position
import dev.vini2003.hammer.core.api.common.math.shape.Shape
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

/**
 * A [BaseScreen] is a screen that holds widgets.
 */
abstract class BaseScreen(title: Text?) : Screen(title), BaseWidgetCollection.Handled {
	override val widgets: MutableList<BaseWidget> = mutableListOf()
	
	override val id = null
	
	override val client = true
	
	override val handler = null
	
	var rectangle: Shape = Shape.ScreenRectangle(0.0F, 0.0F)
	
	override fun init() {
		widgets.clear()
		
		super.init()
		
		initialize(width, height)
		
		onLayoutChanged()
		
		allWidgets.forEach { widget ->
			widget.onLayoutChanged()
		}
	}
	
	override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
		widgets.forEach { widget ->
			widget.onMouseClicked(mouseX.toFloat(), mouseY.toFloat(), button)
		}
		
		return super.mouseClicked(mouseX, mouseY, button)
	}
	
	override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
		widgets.forEach { widget ->
			widget.onMouseReleased(mouseX.toFloat(), mouseY.toFloat(), button)
		}
		
		return super.mouseReleased(mouseX, mouseY, button)
	}
	
	override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
		widgets.forEach { widget ->
			widget.onMouseDragged(mouseX.toFloat(), mouseY.toFloat(), button, deltaX, deltaY)
		}
		
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
	}
	
	override fun mouseMoved(mouseX: Double, mouseY: Double) {
		widgets.forEach { widget ->
			widget.onMouseMoved(mouseX.toFloat(), mouseY.toFloat())
		}
		
		super.mouseMoved(mouseX, mouseY)
	}
	
	override fun mouseScrolled(mouseX: Double, mouseY: Double, deltaY: Double): Boolean {
		widgets.forEach { widget ->
			widget.onMouseScrolled(mouseX.toFloat(), mouseY.toFloat(), deltaY)
		}
		
		return super.mouseScrolled(mouseX, mouseY, deltaY)
	}
	
	override fun keyPressed(keyCode: Int, scanCode: Int, keyModifiers: Int): Boolean {
		widgets.forEach { widget ->
			widget.onKeyPressed(keyCode, scanCode, keyModifiers)
		}
		
		return super.keyPressed(keyCode, scanCode, keyModifiers)
	}
	
	override fun keyReleased(keyCode: Int, scanCode: Int, keyModifiers: Int): Boolean {
		widgets.forEach { widget ->
			widget.onKeyReleased(keyCode, scanCode, keyModifiers)
		}
		
		return super.keyReleased(keyCode, scanCode, keyModifiers)
	}
	
	override fun charTyped(character: Char, keyCode: Int): Boolean {
		widgets.forEach { widget ->
			widget.onCharacterTyped(character, keyCode)
		}
		
		return super.charTyped(character, keyCode)
	}
	
	override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
		super.renderBackground(matrices)
		
		val client = InstanceUtils.CLIENT ?: return
		
		val provider = client.bufferBuilders.effectVertexConsumers
		
		widgets.filterNot { widget ->
			widget.hidden
		}.forEach { widget ->
			widget.drawWidget(matrices, provider, delta)
		}
		
		allWidgets.filterNot { widget ->
			widget.hidden
		}.filter { widget ->
			widget.focused
		}.forEach { widget ->
			renderTooltip(matrices, widget.getTooltip(), mouseX, mouseY)
		}
		
		provider.draw()
		
		super.render(matrices, mouseX, mouseY, delta)
	}
	
	abstract fun initialize(width: Int, height: Int)
	
	override fun onLayoutChanged() {
		var minimumX = Float.MAX_VALUE
		var minimumY = Float.MAX_VALUE
		
		var maximumX = 0.0F
		var maximumY = 0.0F
		
		widgets.forEach { widgety ->
			if (widgety.x < minimumX) {
				minimumX = widgety.x
			}
			if (widgety.x + widgety.width > maximumX) {
				maximumX = widgety.x + widgety.width
			}
			if (widgety.y < minimumY) {
				minimumY = widgety.y
			}
			if (widgety.y + widgety.height > maximumY) {
				maximumY = widgety.y + widgety.height
			}
		}
		
		rectangle = Shape.ScreenRectangle(maximumX - minimumX, maximumY - minimumY, Position(minimumX, minimumY))
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
	
	override fun tick() {
		super.tick()
		
		widgets.forEach { widget ->
			widget.tick()
		}
	}
}