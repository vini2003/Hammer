package dev.vini2003.blade.common.collection

import dev.vini2003.blade.common.collection.base.WidgetCollection
import dev.vini2003.blade.common.widget.base.AbstractWidget
import dev.vini2003.blade.common.widget.base.TabWidget

class TabWidgetCollection(private val number: Int) : AbstractWidget(), WidgetCollection {
	override val widgets: MutableList<AbstractWidget> = mutableListOf()

	override fun addWidget(widget: AbstractWidget) {
		super.addWidget(widget)
		widget.hidden = widget.hidden || parent != null && (parent as TabWidget).selected != number
	}
}