package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.Blade
import com.github.vini2003.blade.client.data.PartitionedTexture
import com.github.vini2003.blade.client.utilities.Drawings
import com.github.vini2003.blade.client.utilities.Layers
import com.github.vini2003.blade.common.handler.BaseScreenHandler
import com.github.vini2003.blade.common.widget.OriginalWidgetCollection
import com.github.vini2003.blade.common.widget.WidgetCollection
import com.github.vini2003.blade.common.widget.wrapper.SafeSlot
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.inventory.Inventory

class SlotWidget(private val slot: Int, private val inventory: Inventory) : AbstractWidget() {
    var backendSlot: SafeSlot? = null

    var texture = PartitionedTexture(Blade.identifier("textures/widget/slot.png"), 18F, 18F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F)

    override fun onAdded(original: OriginalWidgetCollection, immediate: WidgetCollection) {
        super.onAdded(original, immediate)
        backendSlot = SafeSlot(inventory, slot, slotX(), slotY())
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

        backendSlot!!.x = slotX()
        backendSlot!!.y = slotY()
    }

    private fun slotX(): Int {
        return (getPosition().x + (if (getSize().width <= 18) 1F else getSize().width / 2F - 9F)).toInt()
    }

    private fun slotY(): Int {
        return (getPosition().y + (if (getSize().height <= 18) 1F else getSize().height / 2F - 9F)).toInt()
    }

    override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
        if (hidden) return

        texture.draw(matrices, provider, getPosition().x, getPosition().y, getSize().width, getSize().height)

        super.drawWidget(matrices, provider)
    }
}