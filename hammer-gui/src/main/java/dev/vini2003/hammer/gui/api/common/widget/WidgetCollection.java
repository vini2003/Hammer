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
	 * Returns all widgets attached to this collection, including
	 * recursively searching for children's collections and so on.
	 */
	default Collection<Widget> getAllChildren() {
		var widgets = new ArrayList<Widget>();
		
		for (var widget : widgets) {
			widgets.add(widget);
			
			if (widget instanceof WidgetCollection collection) {
				widgets.addAll(collection.getAllChildren());
			}
		}
		
		return widgets;
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
	
	interface Root {
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
		 * The attached screen handler's ID..
		 */
		int getId();
		
		/**
		 * The attached screen handler.
		 */
		BaseScreenHandler getScreenHandler();
		
		/**
		 * Called when any widget has its position or
		 * size changed; or a widget is added or removed.
		 */
		void onLayoutChanged();
	}
}
