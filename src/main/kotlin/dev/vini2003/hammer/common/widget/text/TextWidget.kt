package dev.vini2003.hammer.common.widget.text

import dev.vini2003.hammer.client.util.Drawings
import dev.vini2003.hammer.common.widget.Widget
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

open class TextWidget(var text: Text? = null) : Widget() {
	var shadow = false
	var color = 4210752

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
		if (hidden) return
		
		super.drawWidget(matrices, provider)

		text?.also {
			if (shadow) {
				Drawings.textRenderer?.drawWithShadow(matrices, text, position.x, position.y, color)
			} else {
				Drawings.textRenderer?.draw(matrices, text, position.x, position.y, color)
			}
		}
	}
}