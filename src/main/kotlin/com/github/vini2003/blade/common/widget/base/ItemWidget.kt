package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.client.utilities.Drawings
import com.github.vini2003.blade.client.utilities.Instances
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class ItemWidget : AbstractWidget() {
    var stack: ItemStack = ItemStack.EMPTY;

    override fun getTooltip(): List<Text> {
        if (stack.isEmpty) return emptyList()

        return stack.getTooltip(null) {
            Instances.getClientInstance().options.advancedItemTooltips
        }
    }

    override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
        if (hidden) return

        RenderSystem.pushMatrix()
        RenderSystem.translatef(0F, 0F, -256F)

        Drawings.getItemRenderer()?.renderInGui(stack, getPosition().x.toInt(), getPosition().y.toInt())

        RenderSystem.popMatrix()

        RenderSystem.pushMatrix()
        RenderSystem.translatef(0F, 0F, 256F)

        super.drawWidget(matrices, provider)

        RenderSystem.popMatrix()
    }
}