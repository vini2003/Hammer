package dev.vini2003.hammer.`interface`.common.widget

import dev.vini2003.hammer.`interface`.common.screen.handler.BaseScreenHandler

interface WidgetCollection {
	val widgets: MutableList<Widget>
	
	val allWidgets: MutableList<Widget>
		get() = (widgets + widgets.map { if (it is WidgetCollection) it.allWidgets else mutableListOf(it) }.flatten()).toMutableList()
	
	operator fun plusAssign(widget: Widget) {
		add(widget)
	}
	
	fun add(widget: Widget) {
		widgets += widget
		
		if (this is Widget) {
			onLayoutChanged()
			
			if (handled != null) {
				widget.onAdded(handled!!, this)
			}
			
			widget.parent = this
		}
	}
	
	operator fun minusAssign(widget: Widget) {
		remove(widget)
	}
	
	fun remove(widget: Widget) {
		widgets -= widget
		
		if (this is Widget) {
			onLayoutChanged()
			
			if (handled != null) {
				widget.onRemoved(handled!!, this)
			}
			
			widget.parent = this
		}
	}
	
	interface Handled : WidgetCollection {
		val id: Int?
		
		val client: Boolean
		
		val handler: BaseScreenHandler?
		
		fun onLayoutChanged()
	}
}