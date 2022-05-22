package dev.vini2003.hammer.gui.api.common.event;

import dev.vini2003.hammer.gui.api.common.event.base.Event;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public record LayoutChangedEvent() implements Event {
	@Override
	public EventType type() {
		return EventType.LAYOUT_CHANGED;
	}
	
	@Override
	public PacketByteBuf writeToBuf() {
		var buf = PacketByteBufs.create();
		
		buf.writeEnumConstant(type());
		
		return buf;
	}
	
	public static LayoutChangedEvent readFromBuf(PacketByteBuf buf) {
		return new LayoutChangedEvent();
	}
}
