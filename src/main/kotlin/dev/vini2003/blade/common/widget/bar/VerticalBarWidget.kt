package dev.vini2003.blade.common.widget.bar

import dev.vini2003.blade.BL
import dev.vini2003.blade.client.texture.PartitionedTexture
import dev.vini2003.blade.client.util.Instances
import dev.vini2003.blade.client.scissor.Scissors
import dev.vini2003.blade.client.texture.Texture
import dev.vini2003.blade.common.widget.AbstractWidget
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

open class VerticalBarWidget(
	var maximum: () -> Float = { 100.0F },
	var current: () -> Float = { 0.0F },
	foregroundId: Identifier = BL.id("textures/widget/bar_foreground.png"),
	backgroundId: Identifier = BL.id("textures/widget/bar_background.png")
) : AbstractWidget() {
	var foregroundTexture: Texture = Texture.of(
		foregroundId,
		18F,
		18F,
		0.05555555555555555556F,
		0.05555555555555555556F,
		0.05555555555555555556F,
		0.05555555555555555556F
	)
	var backgroundTexture: Texture = Texture.of(
		backgroundId,
		18F,
		18F,
		0.05555555555555555556F,
		0.05555555555555555556F,
		0.05555555555555555556F,
		0.05555555555555555556F
	)

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
		if (hidden) return

		val rawHeight = Instances.client.window.height.toFloat()
		val scale = Instances.client.window.scaleFactor.toFloat()

		val sBGY = height / maximum() * current()

		var area: Scissors?

		area = Scissors(provider, (x * scale).toInt(), (rawHeight - (y + height - sBGY) * scale).toInt(), (width * scale).toInt(), ((height - sBGY) * scale).toInt())

		backgroundTexture.draw(matrices, provider, x, y, width, height)

		area.destroy(provider)

		area = Scissors(provider, (x * scale).toInt(), (rawHeight - (y + height) * scale).toInt(), (width * scale).toInt(), (sBGY * scale).toInt())
		
		foregroundTexture.draw(matrices, provider, x, y, width, height)

		area.destroy(provider)
	}
}