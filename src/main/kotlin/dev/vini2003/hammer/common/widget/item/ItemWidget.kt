package dev.vini2003.hammer.common.widget.item

import dev.vini2003.hammer.client.util.Drawings
import dev.vini2003.hammer.client.util.Instances
import dev.vini2003.hammer.common.widget.Widget
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

open class ItemWidget(var stack: ItemStack = ItemStack.EMPTY) : Widget() {
	override fun getTooltip(): List<Text> {
		if (stack.isEmpty) return emptyList()

		return stack.getTooltip(null) {
			Instances.client.options.advancedItemTooltips
		}
	}

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
		Drawings.itemRenderer?.renderInGui(stack, position.x.toInt(), position.y.toInt())
	}
}