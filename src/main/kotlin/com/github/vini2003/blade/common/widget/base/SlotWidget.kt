package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.common.handler.BaseScreenHandler
import com.github.vini2003.blade.common.widget.OriginalWidgetCollection
import com.github.vini2003.blade.common.widget.WidgetCollection
import com.github.vini2003.blade.common.widget.wrapper.SafeSlot
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.inventory.Inventory

class SlotWidget(private val slot: Int, private val inventory: Inventory) : AbstractWidget() {
    var backendSlot: SafeSlot? = null

    override fun onAdded(original: OriginalWidgetCollection, immediate: WidgetCollection) {
        super.onAdded(original, immediate)
        backendSlot = SafeSlot(inventory, slot, this.getPosition().x.toInt(), this.getPosition().y.toInt())
        backendSlot!!.index = slot

        if (original is BaseScreenHandler) {
            original.addSlot(backendSlot)
        }
    }

    override fun onRemoved(original: OriginalWidgetCollection, immediate: WidgetCollection) {
        super.onRemoved(original, immediate)

        if (original is BaseScreenHandler) {
            original.removeSlot(backendSlot)
        }
    }

    override fun onLayoutChanged() {
        super.onLayoutChanged()

        if (backendSlot == null) return

        backendSlot!!.x = getPosition().x.toInt()
        backendSlot!!.y = getPosition().y.toInt()
    }

    override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
        if (hidden) return

        //Drawings.drawBeveledPanel(matrices, provider, Layers.getFlat(), getPosition().x - 1 - ((getSize().width - 18F) / 2), getPosition().y - 1 - ((getSize().height - 18F) / 2), getSize().width, getSize().height, style().asColor("slot.top_left"), style().asColor("slot.background"), style().asColor("slot.bottom_right"))

        super.drawWidget(matrices, provider)
    }
}