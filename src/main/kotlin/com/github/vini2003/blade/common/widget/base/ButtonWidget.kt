package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.Blade
import com.github.vini2003.blade.client.data.PartitionedTexture
import com.github.vini2003.blade.client.utilities.Drawings
import com.github.vini2003.blade.client.utilities.Layers
import com.github.vini2003.blade.client.utilities.Texts
import com.github.vini2003.blade.common.data.Color
import com.github.vini2003.blade.common.utilities.Networks
import com.github.vini2003.blade.common.widget.OriginalWidgetCollection
import com.github.vini2003.blade.common.widget.WidgetCollection
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class ButtonWidget(private val clickAction: (ButtonWidget) -> Unit) : AbstractWidget() {
    var textureOn = PartitionedTexture(Blade.identifier("textures/widget/button_on.png"), 18F, 18F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F, 0.16666666666666666667F)
    var textureOff = PartitionedTexture(Blade.identifier("textures/widget/button_off.png"), 18F, 18F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F)

    var disabled: Boolean = false

    var label: Text? = null

    override fun onAdded(original: OriginalWidgetCollection, immediate: WidgetCollection) {
        super.onAdded(original, immediate)

        synchronize.add(Networks.MOUSE_CLICK)
    }

    override fun onMouseClicked(x: Float, y: Float, button: Int) {
        super.onMouseClicked(x, y, button)

        clickAction.invoke(this)
    }

    override fun getTooltip(): List<Text> {
        return emptyList()
    }

    override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
        if (hidden) return

        val texture = if (disabled) textureOff else textureOn

        Drawings.drawQuad(matrices, provider, Layers.flat(), getPosition().x, getPosition().y, 0F, getSize().width, getSize().height, Color.default())
        texture.draw(matrices, provider, getPosition().x, getPosition().y, getSize().width, getSize().height)

        if (provider is VertexConsumerProvider.Immediate) provider.draw()

        label?.let {
            Drawings.getTextRenderer()?.drawWithShadow(matrices, label, getPosition().x + (getSize().width / 2 - Texts.width(label!!) / 2), getPosition().y + (getSize().height / 2 - Texts.height() / 2), color("button.label").toInt()) // 0xFCFCFC
        }

        super.drawWidget(matrices, provider)
    }
}