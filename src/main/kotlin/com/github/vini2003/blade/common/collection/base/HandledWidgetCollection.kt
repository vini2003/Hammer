package com.github.vini2003.blade.common.collection.base

import com.github.vini2003.blade.common.handler.BaseScreenHandler

interface HandledWidgetCollection : WidgetCollection {
	val handler: BaseScreenHandler

	fun onLayoutChanged()
}