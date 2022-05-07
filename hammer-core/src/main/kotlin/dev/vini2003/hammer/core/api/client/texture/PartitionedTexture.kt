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

package dev.vini2003.hammer.core.api.client.texture

import dev.vini2003.hammer.core.api.client.util.DrawingUtils
import dev.vini2003.hammer.core.api.client.util.InstanceUtils
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

/**
 * A partitioned texture is a scalable texture, divided into nine regions, which are automatically determined through the specified paddings, whose data is located through a [textureId].
 *
 * - The corner regions maintain their relative dimensions.
 *
 * - The border regions scale their relative dimensions
 * with the distance between their adjacent corners.
 *
 * - The center region scales its relative dimensions relative to the
 * empty space inside the borders.
 *
 * These properties make it particularly useful for textures where borders should only become longer, but never thicker, such as in slots, buttons and toggles.
 *
 * For example, suppose a 4x4 texture, whose paddings are all `1` - its regions would look like:
 *
 * `|A||BB||C|`
 *
 * `|D||EE||F|`
 *
 * `|D||EE||F|`
 *
 * `|G||HH||I|`
 *
 * Now, for example, suppose an 8x4 texture, whose paddings are all `1` - its regions would look like:
 *
 * `|A||BBBBBB||C|`
 *
 * `|D||EEEEEE||F|`
 *
 * `|D||EEEEEE||F|`
 *
 * `|G||HHHHHH||I|`
 */
class PartitionedTexture(
	/**
	 * Constructs a partitioned texture.
	 *
	 * @param textureId the texture's ID.
	 * @param textureWidth the texture's width.
	 * @param textureHeight the texture's height.
	 * @param leftPadding the texture's left padding.
	 * @param rightPadding the texture's right padding.
	 * @param topPadding the texture's top padding.
	 * @param bottomPadding the textures' bottom padding.
	 * @return the texture.
	 */
	private val textureId: Identifier,
	private val textureWidth: Float,
	private val textureHeight: Float,
	leftPadding: Float,
	rightPadding: Float,
	topPadding: Float,
	bottomPadding: Float
) : BaseTexture {
	private val topLeft = Part(0.0F, 0.0F, leftPadding, topPadding)
	private val topRight = Part(1.0F - rightPadding, 0.0F, 1.0F, topPadding)
	private val bottomLeft = Part(0.0F, 1.0F - bottomPadding, leftPadding, 1.0F)
	private val bottomRight = Part(1.0F - rightPadding, 1.0F - bottomPadding, 1.0F, 1.0F)
	private val middleLeft = Part(0.0F, topPadding, leftPadding, 1.0F - bottomPadding)
	private val middleRight = Part(1.0F - rightPadding, topPadding, 1.0F, 1.0F - bottomPadding)
	private val middleTop = Part(leftPadding, 0.0F, 1.0F - rightPadding, topPadding)
	private val middleBottom = Part(leftPadding, 1.0F - bottomPadding, 1.0F - rightPadding, 1.0F)
	private val center = Part(leftPadding, topPadding, 1.0F - rightPadding, 1.0F - bottomPadding)

	override fun draw(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, width: Float, height: Float) {
		val scaleWidth = width / textureWidth
		val scaleHeight = height / textureHeight

		val topLeftWidth = (width * (topLeft.uE - topLeft.uS) / scaleWidth)
		val topLeftHeight = (height * (topLeft.vE - topLeft.vS) / scaleHeight)

		val topRightWidth = (width * (topRight.uE - topRight.uS) / scaleWidth)
		val topRightHeight = (height * (topRight.vE - topRight.vS) / scaleHeight)


		val bottomLeftWidth = (width * (bottomLeft.uE - bottomLeft.uS) / scaleWidth)
		val bottomLeftHeight = (height * (bottomLeft.vE - bottomLeft.vS) / scaleHeight)

		val bottomRightWidth = (width * (bottomRight.uE - bottomRight.uS) / scaleWidth)
		val bottomRightHeight = (height * (bottomRight.vE - bottomRight.vS) / scaleHeight)


		val middleTopWidth = (width * (middleTop.uE + middleTop.uS) - topLeftWidth - topRightWidth)
		val middleTopHeight = (height * (middleTop.vE - middleTop.vS) / scaleHeight)

		val middleBottomWidth = (width * (middleBottom.uE + middleBottom.uS) - bottomLeftWidth - bottomRightWidth)
		val middleBottomHeight = (height * (middleBottom.vE - middleBottom.vS) / scaleHeight)


		val middleLeftWidth = (width * (middleLeft.uE - middleLeft.uS) / scaleWidth)
		var middleLeftHeight = (height * (middleLeft.vE + middleLeft.vS) - topLeftHeight - topRightHeight)

		val middleRightWidth = (width * (middleRight.uE - middleRight.uS) / scaleWidth)
		var middleRightHeight = (height * (middleRight.vE + middleRight.vS) - topLeftHeight - topRightHeight)


		val centerWidth = (width * (center.uE + center.uS) - topLeftWidth - topRightWidth)
		var centerHeight = (height * (center.vE + center.vS) - topLeftHeight - topRightHeight)


		val heightMultiplier = height / (topLeftHeight + middleLeftHeight + bottomLeftHeight)

		middleLeftHeight *= heightMultiplier
		middleRightHeight *= heightMultiplier
		centerHeight *= heightMultiplier
		
		val client = InstanceUtils.CLIENT!!
		
		val scaledX = x - (x % (client.window.scaledWidth.toFloat() / client.window.width.toFloat()))
		val scaledY = y - (y % (client.window.scaledHeight.toFloat() / client.window.height.toFloat()))

		DrawingUtils.drawTexturedQuad(
			matrices,
			provider,
			textureId,
			x = scaledX,
			y = scaledY,
			width = topLeftWidth,
			height = topLeftHeight,
			uStart = topLeft.uS,
			vStart = topLeft.vS,
			uEnd = topLeft.uE,
			vEnd = topLeft.vE
		)

		DrawingUtils.drawTexturedQuad(
			matrices,
			provider,
			textureId,
			x = scaledX + topLeftWidth,
			y = scaledY,
			width = middleTopWidth,
			height = middleTopHeight,
			uStart = middleTop.uS,
			vStart = middleTop.vS,
			uEnd = middleTop.uE,
			vEnd = middleTop.vE
		)

		DrawingUtils.drawTexturedQuad(
			matrices,
			provider,
			textureId,
			x = scaledX + topLeftWidth + middleTopWidth,
			y = scaledY,
			width = topRightWidth,
			height = topRightHeight,
			uStart = topRight.uS,
			vStart = topRight.vS,
			uEnd = topRight.uE,
			vEnd = topRight.vE
		)

		DrawingUtils.drawTexturedQuad(
			matrices,
			provider,
			textureId,
			x = scaledX,
			y = scaledY + topRightHeight,
			width = middleLeftWidth,
			height = middleLeftHeight,
			uStart = middleLeft.uS,
			vStart = middleLeft.vS,
			uEnd = middleLeft.uE,
			vEnd = middleLeft.vE
		)

		DrawingUtils.drawTexturedQuad(
			matrices,
			provider,
			textureId,
			x = scaledX + middleLeftWidth + middleTopWidth,
			y = scaledY + topRightHeight,
			width = middleRightWidth,
			height = middleRightHeight,
			uStart = middleRight.uS,
			vStart = middleRight.vS,
			uEnd = middleRight.uE,
			vEnd = middleRight.vE
		)

		DrawingUtils.drawTexturedQuad(
			matrices,
			provider,
			textureId,
			x = scaledX + topLeftWidth,
			y = scaledY + topLeftHeight,
			width = centerWidth,
			height = centerHeight,
			uStart = center.uS,
			vStart = center.vS,
			uEnd = center.uE,
			vEnd = center.vE
		)

		DrawingUtils.drawTexturedQuad(
			matrices,
			provider,
			textureId,
			x = scaledX,
			y = scaledY + centerHeight + topLeftHeight,
			width = bottomLeftWidth,
			height = bottomLeftHeight,
			uStart = bottomLeft.uS,
			vStart = bottomLeft.vS,
			uEnd = bottomLeft.uE,
			vEnd = bottomLeft.vE
		)

		DrawingUtils.drawTexturedQuad(
			matrices,
			provider,
			textureId,
			x = scaledX + topLeftWidth,
			y = scaledY + centerHeight + topLeftHeight,
			width = middleBottomWidth,
			height = middleBottomHeight,
			uStart = middleBottom.uS,
			vStart = middleBottom.vS,
			uEnd = middleBottom.uE,
			vEnd = middleBottom.vE
		)

		DrawingUtils.drawTexturedQuad(
			matrices,
			provider,
			textureId,
			x = scaledX + topLeftWidth + middleBottomWidth,
			y = scaledY + centerHeight + topLeftHeight,
			width = bottomRightWidth,
			height = bottomRightHeight,
			uStart = bottomRight.uS,
			vStart = bottomRight.vS,
			uEnd = bottomRight.uE,
			vEnd = bottomRight.vE
		)
	}

	data class Part(val uS: Float, val vS: Float, val uE: Float, val vE: Float)
}