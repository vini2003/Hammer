package dev.vini2003.blade.common.widget.base

import dev.vini2003.blade.BL
import dev.vini2003.blade.client.data.PartitionedTexture
import dev.vini2003.blade.client.utilities.Drawings
import dev.vini2003.blade.client.utilities.Instances
import dev.vini2003.blade.client.utilities.Texts
import dev.vini2003.blade.common.collection.base.ExtendedWidgetCollection
import dev.vini2003.blade.common.collection.base.WidgetCollection
import dev.vini2003.blade.common.utilities.Networks
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text

open class ButtonWidget(var clickAction: () -> Unit = {}) : AbstractWidget() {
	var textureOn = PartitionedTexture(
		BL.id("textures/widget/button_on.png"),
		18F,
		18F,
		0.11111111111111111111F,
		0.11111111111111111111F,
		0.11111111111111111111F,
		0.16666666666666666667F
	)
	var textureOff = PartitionedTexture(
		BL.id("textures/widget/button_off.png"),
		18F,
		18F,
		0.11111111111111111111F,
		0.11111111111111111111F,
		0.11111111111111111111F,
		0.11111111111111111111F
	)
	var textureFocus = PartitionedTexture(
		BL.id("textures/widget/button_on_focus.png"),
		18F,
		18F,
		0.11111111111111111111F,
		0.11111111111111111111F,
		0.11111111111111111111F,
		0.11111111111111111111F
	)

	var disabled: () -> Boolean = { false }

	var label: Text? = null

	override fun onAdded(extended: ExtendedWidgetCollection, immediate: WidgetCollection) {
		super.onAdded(extended, immediate)

		synchronize.add(Networks.MouseClicked)
	}

	override fun onMouseClicked(x: Float, y: Float, button: Int) {
		if (focused || (!focused && extended != null && !extended!!.client)) {
			clickAction()

			if (extended!!.client) {
				playSound()
			}
		}

		super.onMouseClicked(x, y, button)
	}

	private fun playSound() {
		Instances.client().soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F))
	}

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
		if (hidden) return

		val texture = if (disabled()) textureOff else if (focused) textureFocus else textureOn

		texture.draw(matrices, provider, position.x, position.y, size.width, size.height)

		if (provider is VertexConsumerProvider.Immediate) provider.draw()

		label?.also {
			Drawings.getTextRenderer()?.drawWithShadow(matrices, label, position.x + (size.width / 2 - Texts.width(label!!) / 2), position.y + (size.height / 2 - Texts.height() / 2), color("button.label").toInt()) // 0xFCFCFC
		}
	}
}