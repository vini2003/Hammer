package com.github.vini2003.blade.client.data

import com.github.vini2003.blade.client.utilities.Drawings
import com.github.vini2003.blade.client.utilities.Layers
import com.github.vini2003.blade.common.data.Color
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import kotlin.math.min

class PartitionedTexture(
    val texture: Identifier,
    val originalWidth: Float,
    val originalHeight: Float,
    leftPadding: Float,
    rightPadding: Float,
    topPadding: Float,
    bottomPadding: Float
) {
    private val parts: List<Part>

    init {
        parts = listOf(
            Part(0F, 0F, leftPadding, topPadding),                                  // Top left.
            Part(1F - rightPadding, 0F, 1F, topPadding),                            // Top right.
            Part(0F, 1F - bottomPadding, leftPadding, 1F),                          // Bottom left.
            Part(1F - rightPadding, 1F - bottomPadding, 1F, 1F),                    // Bottom right.
            Part(0F, topPadding, leftPadding, 1F - bottomPadding),                  // Middle left.
            Part(1F - rightPadding, topPadding, 1F, 1F - bottomPadding),            // Middle right.
            Part(leftPadding, 0F, 1F - rightPadding, topPadding),                   // Middle top.
            Part(leftPadding, 1F - bottomPadding, 1F - rightPadding, 1F),           // Middle bottom.
            Part(leftPadding, topPadding, 1F - rightPadding, 1F - bottomPadding)    // Center.
        )
    }

    fun draw(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, width: Float, height: Float) {
        val scaleWidth = width / originalWidth
        val scaleHeight = height / originalHeight

        val p0W = width * (parts[0].uE - parts[0].uS) / scaleWidth
        val p0H = height * (parts[0].vE - parts[0].vS) / scaleHeight

        val p1W = width * (parts[1].uE - parts[1].uS) / scaleWidth
        val p1H = height * (parts[1].vE - parts[1].vS) / scaleHeight

        val p6W = width * (parts[6].uE + parts[6].uS) - p0W - p1W
        val p6H = height * (parts[6].vE - parts[6].vS) / scaleHeight

        val p4W = width * (parts[4].uE - parts[4].uS) / scaleWidth
        val p4H = height * (parts[4].vE + parts[4].vS) - p0H - p1H

        val p5W = width * (parts[5].uE - parts[5].uS) / scaleWidth
        val p5H = height * (parts[5].vE + parts[5].vS) - p0H - p1H

        val p8W = width * (parts[8].uE + parts[8].uS) - p0W - p1W
        val p8H = height * (parts[8].vE + parts[8].vS) - p0H - p1H

        val p2W = width * (parts[2].uE - parts[2].uS) / scaleWidth
        val p2H = height * (parts[2].vE - parts[2].vS) / scaleHeight

        val p3W = width * (parts[3].uE - parts[3].uS) / scaleWidth
        val p3H = height * (parts[3].vE - parts[3].vS) / scaleHeight

        val p7W = width * (parts[7].uE + parts[7].uS) - p2W - p3W
        val p7H = height * (parts[7].vE - parts[7].vS) / scaleHeight

        parts[0].let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x, y, 0F, p0W, p0H, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }

        parts[6].let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x + p0W, y, 0F, p6W, p6H, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }

        parts[1].let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x + p0W + p6W, y, 0F, p1W, p1H, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }

        parts[4].let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x, y + p1H, 0F, p4W, p4H, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }

        parts[5].let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x + p4W + p6W, y + p1H, 0F, p5W, p5H, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }

        parts[8].let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x + p0W, y + p0H, 0F, p8W, p8H, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }

        parts[2].let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x, y + p8H + p0H, 0F, p2W, p2H, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }

        parts[7].let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x + p0W, y + p8H + p0H, 0F, p7W, p7H, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }

        parts[3].let {
            Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), x + p0W + p7W, y + p8H + p0H, 0F, p3W, p3H, it.uS, it.vS, it.uE, it.vE, 0x00F000F0, Color.default(), texture)
        }
    }

    data class Part(val uS: Float, val vS: Float, val uE: Float, val vE: Float)
}