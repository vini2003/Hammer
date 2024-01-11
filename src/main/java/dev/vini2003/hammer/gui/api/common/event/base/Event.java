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

package dev.vini2003.hammer.gui.api.common.event.base;

import dev.vini2003.hammer.gui.api.common.event.*;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import net.minecraft.network.PacketByteBuf;

public interface Event {
	EventType type();
	
	void writeToBuf(PacketByteBuf buf);
	
	static Event fromBuf(PacketByteBuf buf) {
		var type = buf.readEnumConstant(EventType.class);
		
		return switch (type) {
			case NONE, ADDED, REMOVED -> null;
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
	}
}
