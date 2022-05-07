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

import dev.vini2003.hammer.gui.impl.common.packet.sync.SyncScreenHandlerPacket
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler
import dev.vini2003.hammer.gui.registry.common.HGUINetworking
import dev.vini2003.hammer.core.api.client.util.InstanceUtils
import dev.vini2003.hammer.core.api.common.util.BufUtils
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

/**
 * A [BaseHandledScreen] is a screen attached to a [BaseScreenHandler].
 */
open class BaseHandledScreen<T : BaseScreenHandler>(handler: BaseScreenHandler, inventory: PlayerInventory, title: Text) : HandledScreen<T>(handler as T, inventory, title) {
	override fun init() {
		handler.widgets.clear()
		handler.slots.clear()
		
		backgroundWidth = width
		backgroundHeight = height
		
		super.init()
		
		handler.initialize(width, height)
		
		handler.onLayoutChanged()
		
		handler.allWidgets.forEach { widget ->
			widget.onLayoutChanged()
		}
		
		val packet = SyncScreenHandlerPacket(handler.syncId, width, height)
		val buf = BufUtils.toPacketByteBuf(packet)
		
		ClientPlayNetworking.send(HGUINetworking.SYNC_SCREEN_HANDLER, buf)
	}

	override fun drawBackground(matrices: MatrixStack?, delta: Float, mouseX: Int, mouseY: Int) {

	}

	override fun isClickOutsideBounds(mouseX: Double, mouseY: Double, left: Int, top: Int, button: Int): Boolean {
		return handler.allWidgets.none { widget ->
			widget.isPointWithin(mouseX.toFloat(), mouseY.toFloat())
		}
	}

	override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
		handler.widgets.forEach { widget ->
			widget.onMouseClicked(mouseX.toFloat(), mouseY.toFloat(), button)
		}

		return super.mouseClicked(mouseX, mouseY, button)
	}

	override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
		handler.widgets.forEach { widget ->
			widget.onMouseReleased(mouseX.toFloat(), mouseY.toFloat(), button)
		}

		return super.mouseReleased(mouseX, mouseY, button)
	}

	override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
		handler.widgets.forEach { widget ->
			widget.onMouseDragged(mouseX.toFloat(), mouseY.toFloat(), button, deltaX, deltaY)
		}

		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
	}

	override fun mouseMoved(mouseX: Double, mouseY: Double) {
		handler.widgets.forEach { widget ->
			widget.onMouseMoved(mouseX.toFloat(), mouseY.toFloat())
		}

		super.mouseMoved(mouseX, mouseY)
	}

	override fun mouseScrolled(mouseX: Double, mouseY: Double, deltaY: Double): Boolean {
		handler.widgets.forEach { widget ->
			widget.onMouseScrolled(mouseX.toFloat(), mouseY.toFloat(), deltaY)
		}

		return super.mouseScrolled(mouseX, mouseY, deltaY)
	}

	override fun keyPressed(keyCode: Int, scanCode: Int, keyModifiers: Int): Boolean {
		handler.widgets.forEach { widget ->
			widget.onKeyPressed(keyCode, scanCode, keyModifiers)
		}

		return super.keyPressed(keyCode, scanCode, keyModifiers)
	}

	override fun keyReleased(keyCode: Int, scanCode: Int, keyModifiers: Int): Boolean {
		handler.widgets.forEach { widget ->
			widget.onKeyReleased(keyCode, scanCode, keyModifiers)
		}

		return super.keyReleased(keyCode, scanCode, keyModifiers)
	}

	override fun charTyped(character: Char, keyCode: Int): Boolean {
		handler.widgets.forEach { widget ->
			widget.onCharacterTyped(character, keyCode)
		}

		return super.charTyped(character, keyCode)
	}

	override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
		super.renderBackground(matrices)

		val client = InstanceUtils.CLIENT ?: return
		
		val provider = client.bufferBuilders.effectVertexConsumers

		handler.widgets.filterNot { widget ->
			widget.hidden
		}.forEach { widget ->
			widget.drawWidget(matrices, provider, delta)
		}
		
		handler.allWidgets.filterNot { widget ->
			widget.hidden
		}.filter { widget ->
			widget.focused
		}.forEach {
			renderTooltip(matrices, it.getTooltip(), mouseX, mouseY)
		}

		provider.draw()

		super.render(matrices, mouseX, mouseY, delta)

		super.drawMouseoverTooltip(matrices, mouseX, mouseY)
	}
}