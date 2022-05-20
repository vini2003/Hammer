package dev.vini2003.hammer.gui.api.common.widget.text

import dev.vini2003.hammer.core.api.common.color.Color
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack


open class TextField : AbstractTextEditor() {

    protected var fixedLength: Int = 15

    open fun setFixedLength(fixedLength: Int): TextField {
        this.fixedLength = fixedLength

        return this
    }

    override fun setText(text: String): AbstractTextEditor {
        var finalText = text.replace("\n".toRegex(), "")
        if (fixedLength >= 0 && fixedLength < finalText.length) {
            finalText = finalText.substring(0, fixedLength)
        }
        return super.setText(finalText)
    }

    override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, tickDelta: Float) {
        if (hidden) {
            return
        }
        val x = position.x
        val y = position.y
        val z: Float = z
        val sX: Float = size.width
        val sY: Float = size.height
        matrices.push()
        DrawingUtils.drawQuad(
            matrices,
            provider,
            x,
            y,
            sX,
            sY,
            z,
            color = Color.BLACK
        )
        renderField(matrices, provider)
        matrices.pop()
        super.drawWidget(matrices, provider, tickDelta)
    }

}