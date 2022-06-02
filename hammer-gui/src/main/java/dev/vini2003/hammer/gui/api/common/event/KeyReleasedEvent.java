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
public record KeyReleasedEvent(int keyCode, int scanCode, int keyModifiers) implements Event {
	@Override
	public EventType type() {
		return EventType.KEY_RELEASED;
	}
	
	@Override
	public PacketByteBuf writeToBuf() {
		var buf = PacketByteBufs.create();
		
		buf.writeEnumConstant(type());
		buf.writeInt(keyCode);
		buf.writeInt(scanCode);
		buf.writeInt(keyModifiers);
		
		return buf;
	}
	
	public static KeyReleasedEvent readFromBuf(PacketByteBuf buf) {
		return new KeyReleasedEvent(buf.readInt(), buf.readInt(), buf.readInt());
	}
}
