/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.gui.api.common.widget

import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler

/**
 * A [BaseWidgetCollection] is an interface that [WidgetYeet]s which
 * hold other widgets should implement.
 */
interface BaseWidgetCollection {
	/**
	 * Returns widgets directly attached to this collection.
	 */
	val widgets: MutableList<BaseWidget>
	
	/**
	 * Returns all widgets attached to this collection, including
	 * recursively searching for children's collections and so on.
	 */
	val allWidgets: MutableList<BaseWidget>
		get() = (widgets + widgets.map { widget ->
			if (widget is BaseWidgetCollection) {
				widget.allWidgets
			} else {
				mutableListOf(widget)
			}
		}.flatten()).toMutableList()
	
	/**
	 * Adds a widget to this collection.
	 *
	 * @param widget the widget.
	 */
	operator fun plusAssign(widget: BaseWidget) {
		add(widget)
	}
	
	/**
	 * Adds a widget to this collection.
	 *
	 * @param widget the widget.
	 */
	fun add(widget: BaseWidget) {
		widgets += widget
		
		if (this is BaseWidget) {
			onLayoutChanged()
			
			if (handled != null) {
				widget.onAdded(handled!!, this)
			}
			
			widget.parent = this
		}
	}
	
	/**
	 * Removes a widget from this collection.
	 *
	 * @param widget the widget.
	 */
	operator fun minusAssign(widget: BaseWidget) {
		remove(widget)
	}
	
	/**
	 * Removes a widget from this collection.
	 *
	 * @param widget the widget.
	 */
	fun remove(widget: BaseWidget) {
		widgets -= widget
		
		if (this is BaseWidget) {
			onLayoutChanged()
			
			if (handled != null) {
				widget.onRemoved(handled!!, this)
			}
			
			widget.parent = this
		}
	}
	
	/**
	 * A [Handled] is a widget collection that is directly attached
	 * to a screen or a screen handler.
	 */
	interface Handled : BaseWidgetCollection {
		/**
		 * Whether this is attached on the client or not.
		 */
		@Suppress("INAPPLICABLE_JVM_NAME")
		@get:JvmName("isClient")
		val client: Boolean
		
		/**
		 * The attached screen handler's ID, if any.
		 */
		val id: Int?
		
		/**
		 * The attached screen handler, if any.
		 */
		val handler: BaseScreenHandler?
		
		fun onLayoutChanged()
	}
}