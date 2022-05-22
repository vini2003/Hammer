package dev.vini2003.hammer.gui.api.common.event;

import dev.vini2003.hammer.gui.api.common.event.base.Event;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import net.minecraft.network.PacketByteBuf;

public record AddedEvent(WidgetCollection.Root rootCollection, WidgetCollection collection) implements Event {
	@Override
	public EventType type() {
		return EventType.ADDED;
	}
	
	@Override
	public PacketByteBuf writeToBuf() {
		return null;
	}
}
