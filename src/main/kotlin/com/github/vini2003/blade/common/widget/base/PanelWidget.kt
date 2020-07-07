package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.client.utilities.Drawings
import com.github.vini2003.blade.client.utilities.Layers
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack

class PanelWidget : AbstractWidget() {
    override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
        if (hidden) return

        Drawings.drawPanel(matrices, provider, Layers.getFlat(), getPosition().x, getPosition().y, getSize().width, getSize().height, style().asColor("panel.shadow"), style().asColor("panel.background"), style().asColor("panel.highlight"), style().asColor("panel.outline"))

        super.drawWidget(matrices, provider)

    }
}