package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.Blade
import com.github.vini2003.blade.client.data.PartitionedTexture
import com.github.vini2003.blade.common.widget.WidgetCollection
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack

open class PanelWidget : AbstractWidget(), WidgetCollection {
	override val widgets: ArrayList<AbstractWidget> = ArrayList()

	var texture = PartitionedTexture(Blade.identifier("textures/widget/panel.png"), 18F, 18F, 0.25F, 0.25F, 0.25F, 0.25F)

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
		if (hidden) return

		texture.draw(matrices, provider, position.x, position.y, size.width, size.height)

		if (provider is VertexConsumerProvider.Immediate) provider.draw()

		widgets.forEach { it.drawWidget(matrices, provider) }
	}
}