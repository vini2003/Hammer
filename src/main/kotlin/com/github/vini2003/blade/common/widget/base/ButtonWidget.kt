package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.Blade
import com.github.vini2003.blade.client.utilities.Drawings
import com.github.vini2003.blade.client.utilities.Layers
import com.github.vini2003.blade.client.utilities.Texts
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class ButtonWidget(private val clickAction: (ButtonWidget) -> Unit) : AbstractWidget() {
    var textureOn: Identifier = Blade.identifier("textures/widget/button_on.png")
    var textureOff: Identifier = Blade.identifier("textures/widget/button_off.png")
    var textureDisabled: Identifier = Blade.identifier("textures/widget/button_disabled.png")

    var disabled: Boolean = false

    var label: Text? = null

    override fun onMouseClicked(x: Float, y: Float, button: Int) {
        super.onMouseClicked(x, y, button)

        clickAction.invoke(this)
    }

    override fun getTooltip(): List<Text> {
        return emptyList()
    }

    override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
        if (hidden) return

        val texture: Identifier = if (disabled) textureDisabled else if (focused) textureOn else textureOff

        Drawings.drawTexturedQuad(matrices, provider, Layers.get(texture), getPosition().x, getPosition().y, getSize().width, getSize().height, texture)

        label?.let {
            Drawings.getTextRenderer()?.draw(matrices, label, getPosition().x + getSize().width / 2 - Texts.width(label!!), getPosition().y + getSize().height / 2 - Texts.height(), style().asColor("button.label").toInt()) // 0xFCFCFC
        }

        super.drawWidget(matrices, provider)
    }
}