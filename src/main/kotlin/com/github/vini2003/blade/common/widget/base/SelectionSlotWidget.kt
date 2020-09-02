package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.Blade
import com.github.vini2003.blade.client.data.PartitionedTexture
import com.github.vini2003.blade.client.utilities.Instances
import com.github.vini2003.blade.common.utilities.Networks
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

class SelectionSlotWidget(var item: ItemStack) : AbstractWidget() {
	var texture = PartitionedTexture(Blade.identifier("textures/widget/slot.png"), 18F, 18F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F)

	init {
		synchronize.add(Networks.MOUSE_CLICK)
	}

	override fun onMouseClicked(x: Float, y: Float, button: Int) {
		super.onMouseClicked(x, y, button)

		if (focused) {
			item = handler!!.getPlayer().inventory.cursorStack
		}
	}

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
		if (hidden) return

		texture.draw(matrices, provider, position.x, position.y, size.width, size.height)

		Instances.client().itemRenderer.renderInGui(item, position.x.toInt(), position.y.toInt())

		super.drawWidget(matrices, provider)
	}
}