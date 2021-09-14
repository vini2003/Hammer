package dev.vini2003.hammer.client.screen

import dev.vini2003.hammer.client.util.Instances
import dev.vini2003.hammer.common.geometry.Rectangle
import dev.vini2003.hammer.common.geometry.position.Position
import dev.vini2003.hammer.common.geometry.size.Size
import dev.vini2003.hammer.common.widget.Widget
import dev.vini2003.hammer.common.widget.WidgetCollection
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

abstract class BaseScreen(title: Text?) : Screen(title), WidgetCollection.Handled {
	override val widgets: ArrayList<Widget> = ArrayList()
	
	override val id = null
	
	override val client = true
	
	override val handler = null
	
	var rectangle: Rectangle = Rectangle(Position.of(0, 0), Size.of(0, 0))
	
	override fun init() {
		widgets.clear()
		
		super.init()
		
		initialize(width, height)
		onLayoutChanged()
		
		allWidgets.forEach(Widget::onLayoutChanged)
	}
	
	override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
		widgets.forEach {
			it.onMouseClicked(mouseX.toFloat(), mouseY.toFloat(), button)
		}
		
		return super.mouseClicked(mouseX, mouseY, button)
	}
	
	override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
		widgets.forEach {
			it.onMouseReleased(mouseX.toFloat(), mouseY.toFloat(), button)
		}
		
		return super.mouseReleased(mouseX, mouseY, button)
	}
	
	override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
		widgets.forEach {
			it.onMouseDragged(mouseX.toFloat(), mouseY.toFloat(), button, deltaX, deltaY)
		}
		
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
	}
	
	override fun mouseMoved(mouseX: Double, mouseY: Double) {
		widgets.forEach {
			it.onMouseMoved(mouseX.toFloat(), mouseY.toFloat())
		}
		
		super.mouseMoved(mouseX, mouseY)
	}
	
	override fun mouseScrolled(mouseX: Double, mouseY: Double, deltaY: Double): Boolean {
		widgets.forEach {
			it.onMouseScrolled(mouseX.toFloat(), mouseY.toFloat(), deltaY)
		}
		
		return super.mouseScrolled(mouseX, mouseY, deltaY)
	}
	
	override fun keyPressed(keyCode: Int, scanCode: Int, keyModifiers: Int): Boolean {
		widgets.forEach {
			it.onKeyPressed(keyCode, scanCode, keyModifiers)
		}
		
		return super.keyPressed(keyCode, scanCode, keyModifiers)
	}
	
	override fun keyReleased(keyCode: Int, scanCode: Int, keyModifiers: Int): Boolean {
		widgets.forEach {
			it.onKeyReleased(keyCode, scanCode, keyModifiers)
		}
		
		return super.keyReleased(keyCode, scanCode, keyModifiers)
	}
	
	override fun charTyped(character: Char, keyCode: Int): Boolean {
		widgets.forEach {
			it.onCharacterTyped(character, keyCode)
		}
		
		return super.charTyped(character, keyCode)
	}
	
	override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
		super.renderBackground(matrices)
		
		val provider = Instances.client.bufferBuilders.effectVertexConsumers
		
		widgets.asSequence().filterNot(Widget::hidden).forEach {
			it.drawWidget(matrices, provider, delta)
		}
		
		allWidgets.asSequence().filterNot(Widget::hidden).filter(Widget::focused).forEach {
			renderTooltip(matrices, it.getTooltip(), mouseX, mouseY)
		}
		
		provider.draw()
		
		super.render(matrices, mouseX, mouseY, delta)
	}
	
	abstract fun initialize(width: Int, height: Int)
	
	override fun onLayoutChanged() {
		var minimumX = Float.MAX_VALUE
		var minimumY = Float.MAX_VALUE
		var maximumX = 0F
		var maximumY = 0F
		
		widgets.forEach {
			if (it.x < minimumX) {
				minimumX = it.x
			}
			if (it.x + it.width > maximumX) {
				maximumX = it.x + it.width
			}
			if (it.y < minimumY) {
				minimumY = it.y
			}
			if (it.y + it.height > maximumY) {
				maximumY = it.y + it.height
			}
		}
		
		rectangle = Rectangle(Position.of(minimumX.toInt(), minimumY.toInt()), Size.of((maximumX - minimumX).toInt(), (maximumY - minimumY).toInt()))
	}
	
	override fun add(widget: Widget) {
		widgets += widget
		
		widget.onAdded(this, this)
		
		widgets.forEach(Widget::onLayoutChanged)
	}
	
	override fun remove(widget: Widget) {
		widgets -= widget
		
		widget.onRemoved(this, this)
		
		widgets.forEach(Widget::onLayoutChanged)
	}
	
	override fun tick() {
		super.tick()
		
		widgets.forEach(Widget::tick)
	}
}