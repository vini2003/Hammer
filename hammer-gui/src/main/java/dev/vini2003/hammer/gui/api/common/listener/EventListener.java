package dev.vini2003.hammer.gui.api.common.listener;

import dev.vini2003.hammer.gui.api.common.event.base.Event;

public interface EventListener<T extends Event> {
	void dispatchEvent(T event);
}
