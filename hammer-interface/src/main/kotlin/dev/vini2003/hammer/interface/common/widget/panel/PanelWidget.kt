package dev.vini2003.hammer.`interface`.common.widget.panel

import dev.vini2003.hammer.H
import dev.vini2003.hammer.`interface`.common.widget.Widget
import dev.vini2003.hammer.`interface`.common.widget.WidgetCollection
import dev.vini2003.hammer.client.texture.Texture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack

open class PanelWidget : Widget(), WidgetCollection {
	companion object {
		@JvmField
		val STANDARD_TEXTURE: Texture = Texture.of(
			H.id("textures/widget/panel.png"),
			18F,
			18F,
			0.25F,
			0.25F,
			0.25F,
			0.25F
		)
	}
	
	override val widgets: MutableList<Widget> = mutableListOf()

	var texture: Texture = STANDARD_TEXTURE
	
	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, delta: Float) {
		texture.draw(matrices, provider, position.x, position.y, size.width, size.height)

		if (provider is VertexConsumerProvider.Immediate) provider.draw()
		
		widgets.asSequence().filterNot(Widget::hidden).forEach {
			it.drawWidget(matrices, provider, delta)
		}
	}
}