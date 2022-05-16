package dev.vini2003.hammer.gui.api.common.widget.text

import dev.vini2003.hammer.core.api.client.util.DrawingUtils
import dev.vini2003.hammer.core.api.common.color.Color
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

open class TextArea : AbstractTextEditor() {
    protected val newLine: MutableList<Boolean> = ArrayList()

    protected var lineWrap: Boolean = true

    open fun <W : TextArea?> setLineWrap(lineWrap: Boolean): W {
        this.lineWrap = lineWrap
        return this as W
    }

    override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider, tickDelta: Float) {
        if (hidden) {
            return
        }

        val x = position.x
        val y = position.y
        val z = z

        val sX: Float = size.width
        val sY: Float = size.height

        matrices.push()
        DrawingUtils.drawTexturedQuad(
            matrices,
            provider,
            Identifier("e"),
            x,
            y,
            sX,
            sY,
            color = Color.BLACK
        )

        if (lineWrap && xOffset != 0f) xOffset = 0f

        renderField(matrices, provider)
        matrices.pop()

        super.drawWidget(matrices, provider, tickDelta)
    }
}