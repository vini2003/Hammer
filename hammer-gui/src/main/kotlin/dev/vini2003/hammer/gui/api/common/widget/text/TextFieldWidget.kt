package dev.vini2003.hammer.gui.api.common.widget.text

import dev.vini2003.hammer.core.HC
import dev.vini2003.hammer.core.api.client.texture.BaseTexture
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack

/**
 * A [TextFieldWidget] is a text field, with a single line.
 */
open class TextFieldWidget : BaseTextEditorWidget() {
	companion object {
		@JvmField
		val STANDARD_TEXTURE: BaseTexture = PartitionedTexture(
			HC.id("textures/widget/text_area.png"),
			18.0F,
			18.0F,
			0.055F,
			0.055F,
			0.055F,
			0.055F
		)
	}
	
	var texture: BaseTexture = STANDARD_TEXTURE
	
	var fixedLength: Int = 15
	
	open fun setFixedLength(fixedLength: Int): TextFieldWidget {
		this.fixedLength = fixedLength
		
		return this
	}
	
	override var text: String
		get() = super.text
		set(value) {
			var text = value.replace("\n".toRegex(), "")
			
			if (fixedLength >= 0 && fixedLength < text.length) {
				text = text.substring(0, fixedLength)
			}
			
			super.text = text
		}
	
	override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, tickDelta: Float) {
		if (hidden) {
			return
		}

		matrices.push()
		
		texture.draw(matrices, provider, position.x, position.y, size.width, size.height)
		
		renderField(matrices, provider)
		
		matrices.pop()
		
		super.drawWidget(matrices, provider, tickDelta)
	}
	
}