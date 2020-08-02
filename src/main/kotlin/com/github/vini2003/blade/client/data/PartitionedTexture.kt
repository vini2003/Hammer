package com.github.vini2003.blade.client.data

import com.github.vini2003.blade.client.utilities.Drawings
import com.github.vini2003.blade.client.utilities.Layers
import com.github.vini2003.blade.common.data.Color
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class PartitionedTexture(
    private val texture: Identifier,
    private val originalWidth: Float,
    private val originalHeight: Float,
    leftPadding: Float,
    rightPadding: Float,
    topPadding: Float,
    bottomPadding: Float
) {
    private val topLeft = Part(0F, 0F, leftPadding, topPadding)
    private val topRight = Part(1F - rightPadding, 0F, 1F, topPadding)
    private val bottomLeft = Part(0F, 1F - bottomPadding, leftPadding, 1F)
    private val bottomRight = Part(1F - rightPadding, 1F - bottomPadding, 1F, 1F)
    private val middleLeft = Part(0F, topPadding, leftPadding, 1F - bottomPadding)
    private val middleRight = Part(1F - rightPadding, topPadding, 1F, 1F - bottomPadding)
    private val middleTop = Part(leftPadding, 0F, 1F - rightPadding, topPadding)
    private val middleBottom = Part(leftPadding, 1F - bottomPadding, 1F - rightPadding, 1F)
    private val center = Part(leftPadding, topPadding, 1F - rightPadding, 1F - bottomPadding)

    fun draw(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, width: Float, height: Float) {
        val scaleWidth = width / originalWidth
        val scaleHeight = height / originalHeight

        val topLeftWidth = width * (topLeft.uE - topLeft.uS) / scaleWidth
        val topLeftHeight = height * (topLeft.vE - topLeft.vS) / scaleHeight

        val topRightWidth = width * (topRight.uE - topRight.uS) / scaleWidth
        val topRightHeight = height * (topRight.vE - topRight.vS) / scaleHeight

        val middleTopWidth = width * (middleTop.uE + middleTop.uS) - topLeftWidth - topRightWidth
        val middleTopHeight = height * (middleTop.vE - middleTop.vS) / scaleHeight

        val middleLeftWidth = width * (middleLeft.uE - middleLeft.uS) / scaleWidth
        val middleLeftHeight = height * (middleLeft.vE + middleLeft.vS) - topLeftHeight - topRightHeight

        val middleRightWidth = width * (middleRight.uE - middleRight.uS) / scaleWidth
        val middleRightHeight = height * (middleRight.vE + middleRight.vS) - topLeftHeight - topRightHeight

        val centerWidth = width * (center.uE + center.uS) - topLeftWidth - topRightWidth
        val centerHeight = height * (center.vE + center.vS) - topLeftHeight - topRightHeight

        val bottomLeftWidth = width * (bottomLeft.uE - bottomLeft.uS) / scaleWidth
        val bottomLeftHeight = height * (bottomLeft.vE - bottomLeft.vS) / scaleHeight

        val bottomRightWidth = width * (bottomRight.uE - bottomRight.uS) / scaleWidth
        val bottomRightHeight = height * (bottomRight.vE - bottomRight.vS) / scaleHeight

        val middleBottomWidth = width * (middleBottom.uE + middleBottom.uS) - bottomLeftWidth - bottomRightWidth
        val middleBottomHeight = height * (middleBottom.vE - middleBottom.vS) / scaleHeight

        topLeft.let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x, y, 0F, topLeftWidth, topLeftHeight, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }

        middleTop.let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x + topLeftWidth, y, 0F, middleTopWidth, middleTopHeight, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }

        topRight.let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x + topLeftWidth + middleTopWidth, y, 0F, topRightWidth, topRightHeight, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }

        middleLeft.let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x, y + topRightHeight, 0F, middleLeftWidth, middleLeftHeight, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }

        middleRight.let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x + middleLeftWidth + middleTopWidth, y + topRightHeight, 0F, middleRightWidth, middleRightHeight, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }

        center.let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x + topLeftWidth, y + topLeftHeight, 0F, centerWidth, centerHeight, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }

        bottomLeft.let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x, y + centerHeight + topLeftHeight, 0F, bottomLeftWidth, bottomLeftHeight, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }

        middleBottom.let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x + topLeftWidth, y + centerHeight + topLeftHeight, 0F, middleBottomWidth, middleBottomHeight, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }

        bottomRight.let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x + topLeftWidth + middleBottomWidth, y + centerHeight + topLeftHeight, 0F, bottomRightWidth, bottomRightHeight, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }
    }

    data class Part(val uS: Float, val vS: Float, val uE: Float, val vE: Float)
}