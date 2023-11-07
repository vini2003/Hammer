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

package dev.vini2003.hammer.gui.api.common.widget;

import dev.vini2003.hammer.gui.api.common.event.AddedEvent;
import dev.vini2003.hammer.gui.api.common.event.LayoutChangedEvent;
import dev.vini2003.hammer.gui.api.common.event.RemovedEvent;
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;

import java.util.ArrayList;
import java.util.Collection;

public interface WidgetCollection {
	/**
	 * Returns widgets directly attached to this collection.
	 */
	Collection<Widget> getChildren();
	
	/**
	 * Returns all widgets attached to this collection, including recursively searching for children's collections and so on.
	 */
	default Collection<Widget> getAllChildren() {
		var allChildren = new ArrayList<Widget>();
		
		for (var child : getChildren()) {
			allChildren.add(child);
			
			if (child instanceof WidgetCollection collection) {
				allChildren.addAll(collection.getAllChildren());
			}
		}
		
		return allChildren;
	}
	
	/**
	 * Adds a widget to this collection.
	 *
	 * @param child the widget.
	 */
	default void add(Widget child) {
		getChildren().add(child);
		
		if (this instanceof Widget widget) {
			widget.dispatchEvent(new LayoutChangedEvent());
			
			if (widget.getRootCollection() != null) {
				child.dispatchEvent(new AddedEvent(widget.getRootCollection(), this));
			}
			
			child.setParent(widget);
		}
	}
	
	default void add(Widget child, float offsetX, float offsetY) {
		if (this instanceof Widget parent) {
			child.setPosition(parent, offsetX, offsetY);
			
			add(child);
		}
	}
	
	/**
	 * Removes a widget from this collection.
	 *
	 * @param child the widget.
	 */
	default void remove(Widget child) {
		getChildren().remove(child);
		
		if (this instanceof Widget widget) {
			widget.dispatchEvent(new LayoutChangedEvent());
			
			if (widget.getRootCollection() != null) {
				child.dispatchEvent(new RemovedEvent(widget.getRootCollection(), this));
			}
			
			child.setParent(null);
		}
	}
	
	interface Root extends WidgetCollection {
		/**
		 * Whether this is attached on the client or not.
		 */
		boolean isClient();
		
		/**
		 * Whether this is attached to a screen or not.
		 */
		default boolean isScreen() {
			return getScreenHandler() == null;
		}
		
		/**
		 * Whether this is attached to a screen handler or not.
		 */
		default boolean isScreenHandler() {
			return getScreenHandler() != null;
		}
		
		/**
		 * The attached screen handler's sync ID.
		 */
		default int getSyncId() {
			return -1;
		}
		
		/**
		 * The attached screen handler.
		 */
		default BaseScreenHandler getScreenHandler() {
			return null;
		}
		
		/**
		 * Called when any widget has its position or size changed; or a widget is added or removed.
		 */
		void onLayoutChanged();
	}
}
