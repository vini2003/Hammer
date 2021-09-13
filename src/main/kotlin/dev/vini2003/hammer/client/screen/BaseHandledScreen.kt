package dev.vini2003.hammer.client.screen

import dev.vini2003.hammer.client.util.Instances
import dev.vini2003.hammer.common.screen.handler.BaseScreenHandler
import dev.vini2003.hammer.common.util.Networks
import dev.vini2003.hammer.common.widget.Widget
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

open class BaseHandledScreen<T : BaseScreenHandler>(handler: BaseScreenHandler, inventory: PlayerInventory, title: Text) : HandledScreen<T>(handler as T, inventory, title) {
	override fun init() {
		handler.widgets.clear()
		handler.slots.clear()
		
		backgroundWidth = width
		backgroundHeight = height
		
		super.init()
		
		handler.initialize(width, height)
		handler.onLayoutChanged()
		
		handler.allWidgets.forEach(Widget::onLayoutChanged)
		
		Networks.toServer(Networks.Initialize, Networks.ofInitialize(handler.syncId, width, height))
	}

	override fun drawBackground(matrices: MatrixStack?, delta: Float, mouseX: Int, mouseY: Int) {

	}

	override fun isClickOutsideBounds(mouseX: Double, mouseY: Double, left: Int, top: Int, button: Int): Boolean {
		return handler.allWidgets.none { widget -> widget.isWithin(mouseX.toFloat(), mouseY.toFloat()) }
	}

	override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
		handler.widgets.forEach {
			it.onMouseClicked(mouseX.toFloat(), mouseY.toFloat(), button)
		}

		return super.mouseClicked(mouseX, mouseY, button)
	}

	override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
		handler.widgets.forEach {
			it.onMouseReleased(mouseX.toFloat(), mouseY.toFloat(), button)
		}

		return super.mouseReleased(mouseX, mouseY, button)
	}

	override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
		handler.widgets.forEach {
			it.onMouseDragged(mouseX.toFloat(), mouseY.toFloat(), button, deltaX, deltaY)
		}

		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
	}

	override fun mouseMoved(mouseX: Double, mouseY: Double) {
		handler.widgets.forEach {
			it.onMouseMoved(mouseX.toFloat(), mouseY.toFloat())
		}

		super.mouseMoved(mouseX, mouseY)
	}

	override fun mouseScrolled(mouseX: Double, mouseY: Double, deltaY: Double): Boolean {
		handler.widgets.forEach {
			it.onMouseScrolled(mouseX.toFloat(), mouseY.toFloat(), deltaY)
		}

		return super.mouseScrolled(mouseX, mouseY, deltaY)
	}

	override fun keyPressed(keyCode: Int, scanCode: Int, keyModifiers: Int): Boolean {
		handler.widgets.forEach {
			it.onKeyPressed(keyCode, scanCode, keyModifiers)
		}

		return super.keyPressed(keyCode, scanCode, keyModifiers)
	}

	override fun keyReleased(keyCode: Int, scanCode: Int, keyModifiers: Int): Boolean {
		handler.widgets.forEach {
			it.onKeyReleased(keyCode, scanCode, keyModifiers)
		}

		return super.keyReleased(keyCode, scanCode, keyModifiers)
	}

	override fun charTyped(character: Char, keyCode: Int): Boolean {
		handler.widgets.forEach {
			it.onCharacterTyped(character, keyCode)
		}

		return super.charTyped(character, keyCode)
	}

	override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
		super.renderBackground(matrices)

		val provider: VertexConsumerProvider.Immediate = Instances.client.bufferBuilders.effectVertexConsumers

		handler.widgets.asSequence().filterNot(Widget::hidden).forEach {
			it.drawWidget(matrices, provider)
		}
		
		handler.allWidgets.asSequence().filterNot(Widget::hidden).filter(Widget::focused).forEach {
			renderTooltip(matrices, it.getTooltip(), mouseX, mouseY)
		}

		provider.draw()

		super.render(matrices, mouseX, mouseY, delta)

		super.drawMouseoverTooltip(matrices, mouseX, mouseY)
	}
}