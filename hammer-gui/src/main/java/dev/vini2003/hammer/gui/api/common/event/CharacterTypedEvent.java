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
public record CharacterTypedEvent(char character, int keyModifiers) implements Event {
	@Override
	public EventType type() {
		return EventType.CHARACTER_TYPED;
	}
	
	@Override
	public PacketByteBuf writeToBuf() {
		var buf = PacketByteBufs.create();
		
		buf.writeEnumConstant(type());
		buf.writeChar(character);
		buf.writeInt(keyModifiers);
		
		return buf;
	}
	
	public static CharacterTypedEvent readFromBuf(PacketByteBuf buf) {
		return new CharacterTypedEvent(buf.readChar(), buf.readInt());
	}
}
