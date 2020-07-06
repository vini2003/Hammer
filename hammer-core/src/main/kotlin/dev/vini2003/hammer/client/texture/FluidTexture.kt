@file:Suppress("DEPRECATION", "UnstableApiUsage")

package dev.vini2003.hammer.client.texture

import dev.vini2003.hammer.client.util.DrawingUtils
import dev.vini2003.hammer.common.color.Color
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import kotlin.math.min

class FluidTexture (
	val fluid: FluidVariant
) : Texture {
	val sprite = FluidVariantRendering.getSprite(fluid)
	val color = Color.of(FluidVariantRendering.getColor(fluid))
	
	override fun draw(
		matrices: MatrixStack,
		provider: VertexConsumerProvider,
		x1: Float,
		y1: Float,
		width: Float,
		height: Float
	) {
		if (sprite == null) {
			return
		}
		
		val x2 = x1 + width
		val y2 = y1 + height
		
		val z = 0.0F
		
		val nX = 0.0F
		val nY = 0.0F
		val nZ = 0.0F
		
		val light = DrawingUtils.DEFAULT_LIGHT
		val overlay = DrawingUtils.DEFAULT_OVERLAY
		
		val peek = matrices.peek()
		val model = peek.positionMatrix
		val normal = peek.normalMatrix
		
		val consumer = provider.getBuffer(RenderLayer.getSolid())
		
		var x = x1
		var y = y1
		
		while (y < y2) {
			x = x1
			
			while (x < x2) {
				val nW = min(x2 - x, sprite.width.toFloat())
				val nH = min(y2 - y, sprite.height.toFloat())
				
				val dX = if (nW < sprite.width.toFloat()) {
					(sprite.maxU - sprite.minU) * (1 - (nW / sprite.width.toFloat()))
				} else {
					0.0F
				}
				
				val dY = if (nH < sprite.height.toFloat()) {
					(sprite.maxV - sprite.minV) * (1 - (nH / sprite.height.toFloat()))
				} else {
					0.0F
				}
				
				consumer.apply {
					vertex(model, x, y + nH, z)
					color(color.r, color.g, color.b, color.a)
					texture(sprite.minU, sprite.maxV - dY)
					overlay(overlay)
					light(light)
					normal(normal, nX, nY, nZ)
					next()
				}
				
				consumer.apply {
					vertex(model, x + nW, y + nH, z)
					color(color.r, color.g, color.b, color.a)
					texture(sprite.maxU - dX, sprite.maxV - dY)
					overlay(overlay)
					light(light)
					normal(normal, nX, nY, nZ)
					next()
				}
				
				consumer.apply {
					vertex(model, x + nW, y, z)
					color(color.r, color.g, color.b, color.a)
					texture(sprite.maxU - dX, sprite.minV)
					overlay(overlay)
					light(light)
					normal(normal, nX, nY, nZ)
					next()
				}
				
				consumer.apply {
					vertex(model, x, y, z)
					color(color.r, color.g, color.b, color.a)
					texture(sprite.minU, sprite.minV)
					overlay(overlay)
					light(light)
					normal(normal, nX, nY, nZ)
					next()
				}
				
				x += min(x2 - x, sprite.width.toFloat())
			}
			
			y += min(y2 - y, sprite.height.toFloat())
		}
	}
}