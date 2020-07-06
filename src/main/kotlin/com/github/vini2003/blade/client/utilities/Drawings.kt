package com.github.vini2003.blade.client.utilities

import com.github.vini2003.blade.common.data.Color
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.texture.TextureManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class Drawings {
    companion object {
        fun drawQuad(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, sX: Float, sY: Float, color: Color) {
            drawQuad(matrices, provider, x, y, 0F, sX, sY, 0x00f000f0, color)
        }

        fun drawQuad(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, z: Float, sX: Float, sY: Float, color: Color) {
            drawQuad(matrices, provider, x, y, z, sX, sY, 0x00f000f0, color)
        }

        fun drawQuad(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, z: Float, sX: Float, sY: Float, light: Int, color: Color) {
            val consumer = provider.getBuffer(Layers.getFlat())

            consumer.vertex(matrices.peek().model, x, y, z).color(color.r, color.g, color.b, color.a).light(light).next()
            consumer.vertex(matrices.peek().model, x, y + sY, z).color(color.r, color.g, color.b, color.a).light(light).next()
            consumer.vertex(matrices.peek().model, x + sX, y + sY, z).color(color.r, color.g, color.b, color.a).light(light).next()
            consumer.vertex(matrices.peek().model, x + sX, y, z).color(color.r, color.g, color.b, color.a).light(light).next()
        }

        fun drawGradientQuad(matrices: MatrixStack, provider: VertexConsumerProvider, startX: Float, startY: Float, endX: Float, endY: Float, colorStart: Color, colorEnd: Color) {
            drawGradientQuad(matrices, provider, startX, startY, endX, endY, 0F, 0f, 0f, 1f, 1f, 0x00f000f0, colorStart, colorEnd, false)
        }

        fun drawGradientQuad(matrices: MatrixStack, provider: VertexConsumerProvider, startX: Float, startY: Float, endX: Float, endY: Float, z: Float, colorStart: Color, colorEnd: Color) {
            drawGradientQuad(matrices, provider, startX, startY, endX, endY, z, 0f, 0f, 1f, 1f, 0x00f000f0, colorStart, colorEnd, false)
        }

        fun drawGradientQuad(matrices: MatrixStack, provider: VertexConsumerProvider, startX: Float, startY: Float, endX: Float, endY: Float, z: Float, light: Int, colorStart: Color, colorEnd: Color) {
            drawGradientQuad(matrices, provider, startX, startY, endX, endY, z, 0f, 0f, 1f, 1f, light, colorStart, colorEnd, false)
        }

        fun drawGradientQuad(matrices: MatrixStack, provider: VertexConsumerProvider, startX: Float, startY: Float, endX: Float, endY: Float, z: Float, uS: Float, vS: Float, uE: Float, vE: Float, light: Int, colorStart: Color, colorEnd: Color, textured: Boolean) {
            val consumer = provider.getBuffer(Layers.getFlat())

            consumer.vertex(matrices.peek().model, endX, startY, z + 201).color(colorStart.r, colorStart.g, colorStart.b, colorStart.a).texture(uS, vS).light(light).normal(matrices.peek().normal, 0f, 1f, 0f).next()
            consumer.vertex(matrices.peek().model, startX, startY, z + 201).color(colorStart.r, colorStart.g, colorStart.b, colorStart.a).texture(uS, vE).light(light).normal(matrices.peek().normal, 0f, 1f, 0f).next()
            consumer.vertex(matrices.peek().model, startX, endY, z + 201).color(colorEnd.r, colorEnd.g, colorEnd.b, colorEnd.a).texture(uE, vS).light(light).normal(matrices.peek().normal, 0f, 1f, 0f).next()
            consumer.vertex(matrices.peek().model, endX, endY, z + 201).color(colorEnd.r, colorEnd.g, colorEnd.b, colorEnd.a).texture(uE, vE).light(light).normal(matrices.peek().normal, 0f, 1f, 0f).next()
        }

        fun drawPanel(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, sX: Float, sY: Float, shadow: Color, panel: Color, highlight: Color, outline: Color) {
            drawPanel(matrices, provider, x, y, 0F, sX, sY, shadow, panel, highlight, outline)
        }

        fun drawPanel(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, z: Float, sX: Float, sY: Float, shadow: Color, panel: Color, hilight: Color, outline: Color) {
            drawQuad(matrices, provider, x + 3, y + 3, z, sX - 6, sY - 6, 0x00F000F0, panel)
            drawQuad(matrices, provider, x + 2, y + 1, z, sX - 4, 2F, 0x00F000F0, hilight)
            drawQuad(matrices, provider, x + 2, y + sY - 3, z, sX - 4, 2F, 0x00F000F0, shadow)
            drawQuad(matrices, provider, x + 1, y + 2, z, 2F, sY - 4, 0x00F000F0, hilight)
            drawQuad(matrices, provider, x + sX - 3, y + 2, z, 2F, sY - 4, 0x00F000F0, shadow)
            drawQuad(matrices, provider, x + sX - 3, y + 2, z, 1F, 1F, 0x00F000F0, panel)
            drawQuad(matrices, provider, x + 2, y + sY - 3, z, 1F, 1F, 0x00F000F0, panel)
            drawQuad(matrices, provider, x + 3, y + 3, z, 1F, 1F, 0x00F000F0, hilight)
            drawQuad(matrices, provider, x + sX - 4, y + sY - 4, z, 1F, 1F, 0x00F000F0, shadow)
            drawQuad(matrices, provider, x + 2, y, z, sX - 4, 1F, 0x00F000F0, outline)
            drawQuad(matrices, provider, x, y + 2, z, 1F, sY - 4, 0x00F000F0, outline)
            drawQuad(matrices, provider, x + sX - 1, y + 2, z, 1F, sY - 4, 0x00F000F0, outline)
            drawQuad(matrices, provider, x + 2, y + sY - 1, z, sX - 4, 1F, 0x00F000F0, outline)
            drawQuad(matrices, provider, x + 1, y + 1, z, 1F, 1F, 0x00F000F0, outline)
            drawQuad(matrices, provider, x + 1, y + sY - 2, z, 1F, 1F, 0x00F000F0, outline)
            drawQuad(matrices, provider, x + sX - 2, y + 1, z, 1F, 1F, 0x00F000F0, outline)
            drawQuad(matrices, provider, x + sX - 2, y + sY - 2, z, 1F, 1F, 0x00F000F0, outline)
        }

        fun drawBeveledPanel(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, sX: Float, sY: Float, topLeft: Color, panel: Color, bottomRight: Color) {
            drawBeveledPanel(matrices, provider, x, y, 0F, sX, sY, 0x00F000F0, topLeft, panel, bottomRight)
        }

        fun drawBeveledPanel(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, z: Float, sX: Float, sY: Float, topLeft: Color, panel: Color, bottomRight: Color) {
            drawBeveledPanel(matrices, provider, x, y, z, sX, sY, 0x00F000F0, topLeft, panel, bottomRight)
        }

        fun drawBeveledPanel(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, z: Float, sX: Float, sY: Float, light: Int, topLeft: Color, panel: Color, bottomRight: Color) {
            drawQuad(matrices, provider, x, y, z, sX, sY, light, panel)
            drawQuad(matrices, provider, x, y, z, sX, 1F, light, topLeft)
            drawQuad(matrices, provider, x, y + 1, z, 1F, sY - 1, light, topLeft)
            drawQuad(matrices, provider, x + sX - 1, y + 1, z, 1F, sY - 1, light, bottomRight)
            drawQuad(matrices, provider, x, y + sY - 1, z, sX - 1, 1F, light, bottomRight)
        }

        fun drawTexturedQuad(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, sX: Float, sY: Float, texture: Identifier?) {
            drawTexturedQuad(matrices, provider, x, y, 0F, sX, sY, 0F, 0F, 1F, 1F, 0x00F000F0, Color.default(), texture)
        }

        fun drawTexturedQuad(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, z: Float, sX: Float, sY: Float, texture: Identifier?) {
            drawTexturedQuad(matrices, provider, x, y, z, sX, sY, 0F, 0F, 1F, 1F, 0x00F000F0, Color.default(), texture)
        }

        fun drawTexturedQuad(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, z: Float, sX: Float, sY: Float, color: Color, texture: Identifier?) {
            drawTexturedQuad(matrices, provider, x, y, z, sX, sY, 0F, 0F, 1F, 1F, 0x00F000F0, color, texture)
        }

        fun drawTexturedQuad(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, z: Float, sX: Float, sY: Float, light: Int, color: Color, texture: Identifier?) {
            drawTexturedQuad(matrices, provider, x, y, z, sX, sY, 0F, 0F, 1F, 1F, light, color, texture)
        }

        fun drawTexturedQuad(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, z: Float, sX: Float, sY: Float, u0: Float, v0: Float, u1: Float, v1: Float, light: Int, color: Color, texture: Identifier?) {
            getTextureManager()?.bindTexture(texture)

            val consumer = provider.getBuffer(Layers.get(texture))

            consumer.vertex(matrices.peek().model, x, y + sY, z).color(color.r, color.g, color.b, color.a).texture(u0, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrices.peek().normal, 0F, 0F, 0F).next()
            consumer.vertex(matrices.peek().model, x + sX, y + sY, z).color(color.r, color.g, color.b, color.a).texture(u1, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrices.peek().normal, 0F, 0F, 0F).next()
            consumer.vertex(matrices.peek().model, x + sX, y, z).color(color.r, color.g, color.b, color.a).texture(u1, v0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrices.peek().normal, 0F, 0F, 0F).next()
            consumer.vertex(matrices.peek().model, x, y, z).color(color.r, color.g, color.b, color.a).texture(u0, v0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrices.peek().normal, 0F, 0F, 0F).next()
        }

        fun drawTooltip(matrices: MatrixStack, provider: VertexConsumerProvider, x: Float, y: Float, width: Float, height: Float) {
            drawGradientQuad(matrices, provider, x - 3, y - 4, x + width + 3, y - 3, 0F, Colors.TOOLTIP_SHADOW_START, Colors.TOOLTIP_SHADOW_START) // Border - top
            drawGradientQuad(matrices, provider, x - 3, y + height + 3, x + width + 3, y + height + 4, 0F, Colors.TOOLTIP_SHADOW_END, Colors.TOOLTIP_SHADOW_END) // Border - bottom
            drawGradientQuad(matrices, provider, x - 3, y - 3, x + width + 3, y + height + 3, 0F, Colors.TOOLTIP_BACKGROUND_START, Colors.TOOLTIP_BACKGROUND_END) // Body
            drawGradientQuad(matrices, provider, x - 4, y - 3, x - 3, y + height + 3, 0F, Colors.TOOLTIP_SHADOW_START, Colors.TOOLTIP_SHADOW_END) // Border - left
            drawGradientQuad(matrices, provider, x + width + 3, y - 3, x + width + 4, y + height + 3, 0F, Colors.TOOLTIP_SHADOW_START, Colors.TOOLTIP_SHADOW_END) // Border - right

            drawGradientQuad(matrices, provider, x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, 0F, Colors.TOOLTIP_OUTLINE_START, Colors.TOOLTIP_OUTLINE_END) // Outline - left
            drawGradientQuad(matrices, provider, x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, 0F, Colors.TOOLTIP_OUTLINE_START, Colors.TOOLTIP_OUTLINE_END) // Outline - right
            drawGradientQuad(matrices, provider, x - 3, y - 3, x + width + 3, y - 3 + 1, 0F, Colors.TOOLTIP_OUTLINE_START, Colors.TOOLTIP_OUTLINE_START) // Outline - top
            drawGradientQuad(matrices, provider, x - 3, y + height + 2, x + width + 3, y + height + 3, 0F, Colors.TOOLTIP_OUTLINE_END, Colors.TOOLTIP_OUTLINE_END) // Outline - bottom
        }

        fun getTextureManager(): TextureManager? {
            return Instances.getClientInstance().textureManager
        }

        fun getItemRenderer(): ItemRenderer? {
            return Instances.getClientInstance().itemRenderer
        }

        fun getTextRenderer(): TextRenderer? {
            return Instances.getClientInstance().textRenderer
        }
    }
}