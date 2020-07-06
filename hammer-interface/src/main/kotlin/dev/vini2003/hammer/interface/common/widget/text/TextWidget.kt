package dev.vini2003.hammer.`interface`.common.widget.text

import dev.vini2003.hammer.`interface`.common.widget.Widget
import dev.vini2003.hammer.client.util.DrawingUtils
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

open class TextWidget(var text: Text? = null) : Widget() {
	var shadow = false
	
	var color = 0x404040

	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, delta: Float) {
		if (text != null) {
			if (shadow) {
				DrawingUtils.TEXT_RENDERER?.drawWithShadow(matrices, text, position.x, position.y, color)
			} else {
				DrawingUtils.TEXT_RENDERER?.draw(matrices, text, position.x, position.y, color)
			}
		}
	}
}