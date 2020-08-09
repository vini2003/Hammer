package com.github.vini2003.blade.common.widget

import com.github.vini2003.blade.common.widget.base.AbstractWidget

interface WidgetCollection {
	val widgets: MutableList<AbstractWidget>

	val allWidgets: MutableList<AbstractWidget>
		get() = (widgets + widgets.map { if (it is WidgetCollection) it.allWidgets else mutableListOf(it) }.flatten()).toMutableList()

	fun addWidget(widget: AbstractWidget) {
		widgets.add(widget)
		if (this is AbstractWidget) {
			this.onLayoutChanged()
			this.original?.also { widget.onAdded(it, this) }
		}
	}

	fun removeWidget(widget: AbstractWidget) {
		widgets.remove(widget)
		if (this is AbstractWidget) {
			this.onLayoutChanged()
			this.original?.also { widget.onRemoved(it, this) }
		}
	}
}