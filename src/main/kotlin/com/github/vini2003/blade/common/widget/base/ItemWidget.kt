package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.client.utilities.Drawings
import com.github.vini2003.blade.client.utilities.Instances
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class ItemWidget(var stack: ItemStack = ItemStack.EMPTY) : AbstractWidget() {
    override fun getTooltip(): List<Text> {
        if (stack.isEmpty) return emptyList()

        return stack.getTooltip(null) {
            Instances.client().options.advancedItemTooltips
        }
    }

    override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
        if (hidden) return

        Drawings.getItemRenderer()?.renderInGui(stack, position.x.toInt(), position.y.toInt())
    }
}