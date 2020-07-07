package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.client.utilities.Colors
import com.github.vini2003.blade.client.utilities.Drawings
import com.github.vini2003.blade.client.utilities.Layers
import com.github.vini2003.blade.common.widget.OriginalWidgetCollection
import com.github.vini2003.blade.common.widget.WidgetCollection
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import org.apache.commons.lang3.reflect.FieldUtils

class SlotWidget(private val slot: Int, private val inventory: Int) : AbstractWidget() {
    private var backendSlot: Slot? = null

    override fun onAdded(original: OriginalWidgetCollection, immediate: WidgetCollection) {
        super.onAdded(original, immediate)
        backendSlot = Slot(original.getInventory(inventory), slot, 0, 0);

        if (original is ScreenHandler) {
            original.slots.add(backendSlot)
        }
    }

    override fun onRemoved(original: OriginalWidgetCollection, immediate: WidgetCollection) {
        super.onRemoved(original, immediate)

        if (original is ScreenHandler) {
            original.slots.remove(backendSlot)
        }
    }

    override fun onLayoutChanged() {
        super.onLayoutChanged()

        if (backendSlot == null) return

        val xField = FieldUtils.getField(Slot::class.java, "x")
        val yField = FieldUtils.getField(Slot::class.java, "y")

        xField.isAccessible = true
        yField.isAccessible = true

        xField.set(backendSlot, getPosition().x.toInt())
        yField.set(backendSlot, getPosition().y.toInt())
    }

    override fun drawWidget(matrices: MatrixStack, provider: VertexConsumerProvider) {
        if (hidden) return
        super.drawWidget(matrices, provider)

        Drawings.drawBeveledPanel(matrices, provider, Layers.getFlat(), getPosition().x - 1 - ((getSize().width - 18F) / 2), getPosition().y - 1 - ((getSize().height - 18F) / 2), getSize().width, getSize().height, Colors.SLOT_TOP_LEFT, Colors.SLOT_BACKGROUND, Colors.SLOT_BOTTOM_RIGHT)
    }
}