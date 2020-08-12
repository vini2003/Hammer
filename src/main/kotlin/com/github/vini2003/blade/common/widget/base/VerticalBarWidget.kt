package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.Blade
import com.github.vini2003.blade.client.data.PartitionedTexture
import com.github.vini2003.blade.client.utilities.Instances
import com.github.vini2003.blade.client.utilities.Scissors
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class VerticalBarWidget(var maximum: () -> Float, var current: () -> Float, foregroundId: Identifier = Blade.identifier("textures/widget/bar_foreground.png"), backgroundId: Identifier = Blade.identifier("textures/widget/bar_background.png")) : AbstractWidget() {
	var foreground = PartitionedTexture(foregroundId, 18F, 18F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F)
	var background = PartitionedTexture(backgroundId, 18F, 18F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F)

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
		if (hidden) return

		val x = position.x
		val y = position.y

		val sX = size.width
		val sY = size.height

		val rawHeight = Instances.client().window.height.toFloat()
		val scale = Instances.client().window.scaleFactor.toFloat()

		val sBGY: Float = sY / maximum.invoke() * current.invoke()

		var area: Scissors?

		area = Scissors(provider, (x * scale).toInt(), (rawHeight - (y + sY - sBGY) * scale).toInt(), (sX * scale).toInt(), ((sY - sBGY) * scale).toInt())

		background.draw(matrices, provider, x, y, sX, sY)

		area.destroy(provider)

		area = Scissors(provider, (x * scale).toInt(), (rawHeight - (y + sY) * scale).toInt(), (sX * scale).toInt(), (sBGY * scale).toInt())

		foreground.draw(matrices, provider, x, y, sX, sY)

		foreground.draw(matrices, provider, x, y, sX, sY)

		area.destroy(provider)
	}
}