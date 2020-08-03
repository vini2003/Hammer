package com.github.vini2003.blade.common.widget

import com.github.vini2003.blade.common.widget.base.AbstractWidget
import net.minecraft.inventory.Inventory

interface WidgetCollection {
    fun getWidgets(): Collection<AbstractWidget>

    fun addWidget(widget: AbstractWidget) {
        if (this is AbstractWidget) this.onLayoutChanged()
    }

    fun removeWidget(widget: AbstractWidget) {
        if (this is AbstractWidget) this.onLayoutChanged()
    }
}