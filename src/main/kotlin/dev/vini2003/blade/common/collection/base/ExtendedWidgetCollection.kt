package dev.vini2003.blade.common.collection.base

import dev.vini2003.blade.common.handler.BaseScreenHandler

interface ExtendedWidgetCollection : WidgetCollection {
	val id: Int?
	
	val client: Boolean
	
	val handler: BaseScreenHandler?
	
	fun onLayoutChanged()
}