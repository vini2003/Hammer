@file:Suppress("DEPRECATION", "UnstableApiUsage")

package dev.vini2003.hammer.client.texture

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

interface Texture {
	companion object {
		@JvmStatic
		fun of(fluidVariant: FluidVariant): Texture = FluidTexture(fluidVariant)
		
		@JvmStatic
		fun of(id: Identifier,
		       originalWidth: Float,
		       originalHeight: Float,
		       leftPadding: Float,
		       rightPadding: Float,
		       topPadding: Float,
		       bottomPadding: Float
		): Texture = PartitionedTexture(id, originalWidth, originalHeight, leftPadding, rightPadding, topPadding, bottomPadding)
	}
	
	fun draw(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, width: Float, height: Float)
}