package dev.vini2003.hammer.`interface`.common.widget.button

import dev.vini2003.hammer.H
import dev.vini2003.hammer.`interface`.common.widget.Widget
import dev.vini2003.hammer.`interface`.common.widget.WidgetCollection
import dev.vini2003.hammer.client.texture.Texture
import dev.vini2003.hammer.client.util.DrawingUtils
import dev.vini2003.hammer.client.util.InstanceUtils
import dev.vini2003.hammer.client.util.extension.height
import dev.vini2003.hammer.client.util.extension.width
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text

open class ButtonWidget(var clickAction: () -> Unit = {}) : Widget() {
	companion object {
		@JvmField
		val STANDARD_ON_TEXTURE: Texture = Texture.of(
			H.id("textures/widget/button_on.png"),
			18F,
			18F,
			0.11111111111111111111F,
			0.11111111111111111111F,
			0.11111111111111111111F,
			0.16666666666666666667F
		)
		
		@JvmField
		var STANDARD_OFF_TEXTURE: Texture = Texture.of(
			H.id("textures/widget/button_off.png"),
			18F,
			18F,
			0.11111111111111111111F,
			0.11111111111111111111F,
			0.11111111111111111111F,
			0.11111111111111111111F
		)
		
		@JvmField
		var STANDARD_FOCUSED_TEXTURE: Texture = Texture.of(
			H.id("textures/widget/button_on_focus.png"),
			18F,
			18F,
			0.11111111111111111111F,
			0.11111111111111111111F,
			0.11111111111111111111F,
			0.11111111111111111111F
		)
	}
	
	var onTexture: Texture = STANDARD_ON_TEXTURE
	var offTexture: Texture = STANDARD_OFF_TEXTURE
	var focusedTexture: Texture = STANDARD_FOCUSED_TEXTURE

	var disabled: () -> Boolean = { false }

	var label: Text? = null

	override fun onAdded(handled: WidgetCollection.Handled, immediate: WidgetCollection) {
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
		InstanceUtils.CLIENT.soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F))
	}

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, delta: Float) {
		val texture = if (disabled()) offTexture else if (focused) focusedTexture else onTexture

		texture.draw(matrices, provider, position.x, position.y, size.width, size.height)

		if (provider is VertexConsumerProvider.Immediate) provider.draw()

		label?.also {
			DrawingUtils.TEXT_RENDERER?.drawWithShadow(matrices, label, position.x + (size.width / 2 - label!!.width / 2), position.y + (size.height / 2 - label!!.height / 2), 0xFCFCFC)
		}
	}
}