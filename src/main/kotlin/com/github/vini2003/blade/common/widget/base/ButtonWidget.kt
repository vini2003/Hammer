package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.Blade
import com.github.vini2003.blade.client.utilities.Drawings
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class ButtonWidget(private val clickAction: () -> Unit) : AbstractWidget() {
    var textureOn: Identifier = Blade.identifier("textures/widget/button_on.png")
    var textureOff: Identifier = Blade.identifier("textures/widget/button_off.png")
    var textureDisabled: Identifier = Blade.identifier("textures/widget/button_disabled.png")

    var disabled: Boolean = false

    override fun onMouseClicked(x: Float, y: Float, button: Int) {
        super.onMouseClicked(x, y, button)

        clickAction.invoke()
    }

    override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
        if (hidden) return
        super.drawWidget(matrices, provider)

        Drawings.drawTexturedQuad(matrices, provider, getPosition().x, getPosition().y, getSize().width, getSize().height, if (disabled) textureDisabled else if (focused) textureOn else textureOff)
    }
}