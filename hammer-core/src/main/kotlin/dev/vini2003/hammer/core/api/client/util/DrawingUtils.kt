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

package dev.vini2003.hammer.core.api.client.util

import dev.vini2003.hammer.core.api.client.util.InstanceUtils.CLIENT
import dev.vini2003.hammer.core.api.common.color.Color
import dev.vini2003.hammer.core.api.common.util.extension.normalMatrix
import dev.vini2003.hammer.core.api.common.util.extension.positionMatrix
import net.minecraft.client.render.LightmapTextureManager
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import kotlin.math.abs
import kotlin.math.min

object DrawingUtils {
	@JvmStatic
	@JvmOverloads
	fun drawQuad(
		matrices: MatrixStack,
		provider: VertexConsumerProvider,
		layer: RenderLayer,
		x: Float, y: Float,
		width: Float, height: Float,
		z: Float = 0.0F,
		normalX: Float = 0.0F, normalY: Float = 0.0F, normalZ: Float = 0.0F,
		overlay: Int = DEFAULT_OVERLAY,
		light: Int = DEFAULT_LIGHT,
		color: Color = DEFAULT_COLOR,
	) {
		val consumer = provider.getBuffer(layer)
		
		consumer.vertex(matrices.positionMatrix, x, y, z).color(color.r, color.g, color.b, color.a).overlay(overlay).light(light).normal(matrices.normalMatrix, normalX, normalY, normalZ).next()
		consumer.vertex(matrices.positionMatrix, x, y + height, z).color(color.r, color.g, color.b, color.a).overlay(overlay).light(light).normal(matrices.normalMatrix, normalX, normalY, normalZ).next()
		consumer.vertex(matrices.positionMatrix, x + width, y + height, z).color(color.r, color.g, color.b, color.a).overlay(overlay).normal(matrices.normalMatrix, normalX, normalY, normalZ).light(light).next()
		consumer.vertex(matrices.positionMatrix, x + width, y, z).color(color.r, color.g, color.b, color.a).overlay(overlay).normal(matrices.normalMatrix, normalX, normalY, normalZ).light(light).next()
	}
	
	@JvmStatic
	@JvmOverloads
	fun drawTexturedQuad(
		matrices: MatrixStack,
		provider: VertexConsumerProvider,
		textureId: Identifier,
		x: Float, y: Float,
		width: Float, height: Float,
		z: Float = 0.0F,
		uStart: Float = 0.0F, vStart: Float = 0.0F,
		uEnd: Float = 1.0F, vEnd: Float = 1.0F,
		normalX: Float = 0.0F, normalY: Float = 0.0F, normalZ: Float = 0.0F,
		overlay: Int = DEFAULT_OVERLAY,
		light: Int = DEFAULT_LIGHT,
		color: Color = DEFAULT_COLOR
	) {
		val layer = LayerUtils.get(textureId)
		
		val consumer = provider.getBuffer(layer)
		
		consumer.vertex(matrices.positionMatrix, x, y + height, z).color(color.r, color.g, color.b, color.a).texture(uStart, vEnd).overlay(overlay).light(light).normal(matrices.normalMatrix, normalX, normalY, normalZ).next()
		consumer.vertex(matrices.positionMatrix, x + width, y + height, z).color(color.r, color.g, color.b, color.a).texture(uEnd, vEnd).overlay(overlay).light(light).normal(matrices.normalMatrix, normalX, normalY, normalZ).next()
		consumer.vertex(matrices.positionMatrix, x + width, y, z).color(color.r, color.g, color.b, color.a).texture(uEnd, vStart).overlay(overlay).light(light).normal(matrices.normalMatrix, normalX, normalY, normalZ).next()
		consumer.vertex(matrices.positionMatrix, x, y, z).color(color.r, color.g, color.b, color.a).texture(uStart, vStart).overlay(overlay).light(light).normal(matrices.normalMatrix, normalX, normalY, normalZ).next()
	}
	
	@JvmStatic
	@JvmOverloads
	fun drawTiledTexturedQuad(
		matrices: MatrixStack,
		provider: VertexConsumerProvider,
		textureId: Identifier,
		x: Float, y: Float,
		width: Float, height: Float,
		tileWidth: Float, tileHeight: Float,
		z: Float = 0.0F,
		uStart: Float = 0.0F, vStart: Float = 0.0F,
		uEnd: Float = 1.0F, vEnd: Float = 1.0F,
		normalX: Float = 0.0F, normalY: Float = 0.0F, normalZ: Float = 0.0F,
		overlay: Int = DEFAULT_OVERLAY,
		light: Int = DEFAULT_LIGHT,
		color: Color = DEFAULT_COLOR
	) {
		val layer = RenderLayer.getSolid()
		
		val consumer = provider.getBuffer(layer)
		
		val endX = x + width
		val endY = y + height
		
		var currentX = x
		var currentY = y
		
		while (currentY < endY) {
			currentX = x
			
			while (currentX < endX) {
				val diffX = min(endX - currentX, tileWidth)
				val diffY = min(endY - currentY, tileHeight)
				
				val deltaX: Float
				
				if (diffX < tileWidth) {
					deltaX = (uEnd - uStart) * (1.0F - (diffX / tileWidth))
				} else {
					deltaX = 0.0F
				}
				
				val deltaY: Float
				
				if (diffY < tileHeight) {
					deltaY = (vEnd - vStart) * (1.0F - (diffY / tileHeight))
				} else {
					deltaY = 0.0F
				}
				
				consumer.vertex(matrices.positionMatrix, currentX, currentY + diffY, z).color(color.r, color.g, color.b, color.a).texture(uStart, vEnd - deltaY).overlay(overlay).light(light).normal(matrices.normalMatrix, normalX, normalY, normalZ).next()
				consumer.vertex(matrices.positionMatrix, currentX + diffX, currentY + diffY, z).color(color.r, color.g, color.b, color.a).texture(uEnd - deltaX, vEnd - deltaY).overlay(overlay).light(light).normal(matrices.normalMatrix, normalX, normalY, normalZ).next()
				consumer.vertex(matrices.positionMatrix, currentX + diffX, currentY, z).color(color.r, color.g, color.b, color.a).texture(uEnd - deltaX, vStart).overlay(overlay).light(light).normal(matrices.normalMatrix, normalX, normalY, normalZ).next()
				consumer.vertex(matrices.positionMatrix, currentX, currentY, z).color(color.r, color.g, color.b, color.a).texture(uStart, vStart).overlay(overlay).light(light).normal(matrices.normalMatrix, normalX, normalY, normalZ).next()
				
				currentX += min(abs(endX - currentX), tileWidth)
			}
			
			currentY += min(abs(endY - currentY), tileHeight)
		}
	}

	@JvmStatic
	@get:JvmName("getBlockRenderManager")
	val BLOCK_RENDERER
		get() = CLIENT?.blockRenderManager
	
	@JvmStatic
	@get:JvmName("getTextureManager")
	val TEXTURE_MANAGER
		get() = CLIENT?.textureManager

	@JvmStatic
	@get:JvmName("getBakedModelManager")
	val BAKED_MODEL_MANAGER
		get() = CLIENT?.bakedModelManager

	@JvmStatic
	@get:JvmName("getItemRenderer")
	val ITEM_RENDERER
		get() = CLIENT?.itemRenderer
	
	@JvmStatic
	@get:JvmName("getTextRenderer")
	val TEXT_RENDERER
		get() = CLIENT?.textRenderer
	
	@JvmField
	val DEFAULT_OVERLAY = OverlayTexture.DEFAULT_UV
	
	@JvmField
	val DEFAULT_LIGHT = LightmapTextureManager.MAX_LIGHT_COORDINATE

	@JvmField
	val DEFAULT_COLOR = Color.WHITE
}