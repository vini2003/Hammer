package com.github.vini2003.blade.common.widget.base

import com.github.vini2003.blade.common.data.Position
import com.github.vini2003.blade.common.data.Size
import com.github.vini2003.blade.common.widget.OriginalWidgetCollection
import com.github.vini2003.blade.common.widget.WidgetCollection
import net.minecraft.inventory.Inventory

class SlotListWidget(
    private val heightInSlots: Int,
    private val widthInSlots: Int,
    private val maximumSlots: Int,
    private val inventory: Inventory
) : AbstractWidget(), WidgetCollection {
    private val widgets = mutableListOf<AbstractWidget>()

    private var row = 0

    override fun getWidgets(): Collection<AbstractWidget> {
        return widgets
    }

    override fun addWidget(widget: AbstractWidget) {
        widgets.add(widget)
        original?.let { widget.onAdded(it, this) }

        widgets.forEach { _ ->
            widget.onLayoutChanged()
        }
    }

    override fun removeWidget(widget: AbstractWidget) {
        widgets.remove(widget)
        original?.let { widget.onRemoved(it, this) }

        widgets.forEach { _ ->
            widget.onLayoutChanged()
        }
    }

    override fun onAdded(original: OriginalWidgetCollection, immediate: WidgetCollection) {
        super.onAdded(original, immediate)

        for (h in 0 until heightInSlots) {
            for (w in 0 until widthInSlots) {
                if ((inventory.size() >= h + w)) {
                    val slot = SlotWidget(h + w, inventory)
                    slot.setPosition(Position({ this.getPosition().x + w * 18F }, { this.getPosition().y + h * 18F }))
                    slot.setSize(Size({ 18F }, { 18F }))
                    widgets.add(slot)
                    immediate.addWidget(slot)
                }
            }
        }
    }

    override fun onMouseScrolled(x: Float, y: Float, deltaY: Double) {
        super.onMouseScrolled(x, y, deltaY)

        if (isWithin(x, y)) {
            if (deltaY > 0 && row > 0) {
                --row

                widgets.forEach {
                    it as SlotWidget

                    val slot = it.backendSlot!!

                    if (slot.id - widthInSlots >= 0) {
                        slot.canInsert = true
                        slot.index = slot.index - widthInSlots
                    } else {
                        slot.canInsert = false
                        slot.index = slot.index - widthInSlots
                    }
                }
            } else if (deltaY < 0 && row < maximumSlots) {
                ++row

                widgets.forEach {
                    it as SlotWidget

                    val slot = it.backendSlot!!

                    if (slot.id + widthInSlots <= inventory.size()) {
                        slot.canInsert = true
                        slot.index = slot.index + widthInSlots

                    } else {
                        slot.canInsert = false
                        slot.index = slot.index + widthInSlots
                    }
                }
            }
        }
    }
}