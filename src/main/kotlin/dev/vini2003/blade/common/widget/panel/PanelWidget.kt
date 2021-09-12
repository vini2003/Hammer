package dev.vini2003.blade.common.widget.panel

import dev.vini2003.blade.BL
import dev.vini2003.blade.client.texture.PartitionedTexture
import dev.vini2003.blade.client.texture.Texture
import dev.vini2003.blade.common.collection.base.WidgetCollection
import dev.vini2003.blade.common.widget.AbstractWidget
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack

open class PanelWidget : AbstractWidget(), WidgetCollection {
	override val widgets: MutableList<AbstractWidget> = mutableListOf()

	var texture: Texture = Texture.of(
		BL.id("textures/widget/panel.png"),
		18F,
		18F,
		0.25F,
		0.25F,
		0.25F,
		0.25F
	)

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
		if (hidden) return

		texture.draw(matrices, provider, position.x, position.y, size.width, size.height)

		if (provider is VertexConsumerProvider.Immediate) provider.draw()

		widgets.forEach { it.drawWidget(matrices, provider) }
	}
}