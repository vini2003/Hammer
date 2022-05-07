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

package dev.vini2003.hammer.gui.api.common.widget.button

import dev.vini2003.hammer.core.HC
import dev.vini2003.hammer.core.api.client.texture.BaseTexture
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture
import dev.vini2003.hammer.core.api.client.util.DrawingUtils
import dev.vini2003.hammer.gui.api.common.widget.BaseWidget
import dev.vini2003.hammer.gui.api.common.widget.BaseWidgetCollection
import dev.vini2003.hammer.core.api.client.util.InstanceUtils
import dev.vini2003.hammer.core.api.client.util.extension.height
import dev.vini2003.hammer.core.api.client.util.extension.width
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text

/**
 * A [ButtonWidget] is a button widget.
 */
open class ButtonWidget @JvmOverloads constructor(
	var clickAction: () -> Unit = {}
) : BaseWidget() {
	companion object {
		@JvmField
		val STANDARD_ON_TEXTURE: BaseTexture = PartitionedTexture(
			HC.id("textures/widget/button_on.png"),
			18F,
			18F,
			0.11F,
			0.11F,
			0.11F,
			0.16F
		)
		
		@JvmField
		var STANDARD_OFF_TEXTURE: BaseTexture = PartitionedTexture(
			HC.id("textures/widget/button_off.png"),
			18F,
			18F,
			0.11F,
			0.11F,
			0.11F,
			0.11F
		)
		
		@JvmField
		var STANDARD_FOCUSED_TEXTURE: BaseTexture = PartitionedTexture(
			HC.id("textures/widget/button_on_focus.png"),
			18F,
			18F,
			0.11111111111111111111F,
			0.11111111111111111111F,
			0.11111111111111111111F,
			0.11111111111111111111F
		)
	}
	
	var disabled: () -> Boolean = { false }
	
	var label: Text? = null
	
	var onTexture: BaseTexture = STANDARD_ON_TEXTURE
	
	var offTexture: BaseTexture = STANDARD_OFF_TEXTURE
	
	var focusedTexture: BaseTexture = STANDARD_FOCUSED_TEXTURE

	override fun onAdded(handled: BaseWidgetCollection.Handled, immediate: BaseWidgetCollection) {
		super.onAdded(handled, immediate)

		syncMouseClicked = true
	}

	override fun onMouseClicked(x: Float, y: Float, button: Int) {
		if (focused || (!focused && handled != null && !handled!!.client)) {
			clickAction()

			if (handled!!.client) {
				playSound()
			}
		}

		super.onMouseClicked(x, y, button)
	}

	private fun playSound() {
		val client = InstanceUtils.CLIENT ?: return
		
		client.soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F))
	}

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, tickDelta: Float) {
		val texture: BaseTexture
		
		if (disabled()) {
			texture = offTexture
		} else if (focused) {
			texture = focusedTexture
		} else {
			texture = onTexture
		}

		texture.draw(matrices, provider, position.x, position.y, size.width, size.height)

		if (provider is VertexConsumerProvider.Immediate) {
			provider.draw()
		}

		if (label != null) {
			val textRenderer = DrawingUtils.TEXT_RENDERER ?: return
			
			textRenderer.drawWithShadow(matrices, label, position.x + (size.width / 2.0F - label!!.width / 2), position.y + (size.height / 2 - label!!.height / 2), 0xFCFCFC)
		}
	}
}