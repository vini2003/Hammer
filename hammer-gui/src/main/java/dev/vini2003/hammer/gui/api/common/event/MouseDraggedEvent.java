package dev.vini2003.hammer.gui.api.common.event;

import dev.vini2003.hammer.gui.api.common.event.base.Event;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

/**
 * <p><b>This event is synchronized if  {@link Widget#shouldSync(EventType)} is <code>true</code> for the given {@link Event#type()}.</b></p>
 *
 * <p><b>This event is disapatched even when the mouse is outside the associated widget's boundaries - for that, check the widget's {@link Widget#isFocused()}!</b></p>
 */
public record MouseDraggedEvent(float x, float y, int button, double deltaX, double deltaY) implements Event {
	@Override
	public EventType type() {
		return EventType.MOUSE_DRAGGED;
	}
	
	@Override
	public PacketByteBuf writeToBuf() {
		var buf = PacketByteBufs.create();
		
		buf.writeEnumConstant(type());
		buf.writeFloat(x);
		buf.writeFloat(y);
		buf.writeInt(button);
		buf.writeDouble(deltaX);
		buf.writeDouble(deltaY);
		
		return buf;
	}
	
	public static MouseDraggedEvent readFromBuf(PacketByteBuf buf) {
		return new MouseDraggedEvent(buf.readFloat(), buf.readFloat(), buf.readInt(), buf.readDouble(), buf.readDouble());
	}
}
