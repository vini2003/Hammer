package dev.vini2003.hammer.client.texture

import dev.vini2003.hammer.client.util.DrawingUtils
import dev.vini2003.hammer.client.util.LayerUtils
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class SpriteTexture(
	private val texture: Identifier,
) : Texture {
	override fun draw(
		matrices: MatrixStack,
		provider: VertexConsumerProvider,
		x: Float,
		y: Float,
		width: Float,
		height: Float
	) {
		val layer = LayerUtils.get(texture)
		
		DrawingUtils.drawTexturedQuad(matrices, provider, layer, x, y, width, height, texture)
	}
}