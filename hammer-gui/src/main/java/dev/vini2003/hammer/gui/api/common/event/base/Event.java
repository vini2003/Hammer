package dev.vini2003.hammer.gui.api.common.event.base;

import dev.vini2003.hammer.gui.api.common.event.*;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import net.minecraft.network.PacketByteBuf;

public interface Event {
	EventType type();
	
	PacketByteBuf writeToBuf();
	
	static Event fromBuf(PacketByteBuf buf) {
		var type = buf.readEnumConstant(EventType.class);
		
		var event = switch (type) {
			case NONE -> null; // TODO: Fix!
			case CHARACTER_TYPED -> CharacterTypedEvent.readFromBuf(buf);
			case FOCUS_GAINED -> FocusGainedEvent.readFromBuf(buf);
			case FOCUS_RELEASED -> FocusReleasedEvent.readFromBuf(buf);
			case KEY_PRESSED -> KeyPressedEvent.readFromBuf(buf);
			case KEY_RELEASED -> KeyReleasedEvent.readFromBuf(buf);
			case LAYOUT_CHANGED -> LayoutChangedEvent.readFromBuf(buf);
			case MOUSE_CLICKED -> MouseClickedEvent.readFromBuf(buf);
			case MOUSE_DRAGGED -> MouseDraggedEvent.readFromBuf(buf);
			case MOUSE_MOVED -> MouseMovedEvent.readFromBuf(buf);
			case MOUSE_RELEASED -> MouseReleasedEvent.readFromBuf(buf);
			case MOUSE_SCROLLED -> MouseScrolledEvent.readFromBuf(buf);
		};
		
		return event;
	}
}
