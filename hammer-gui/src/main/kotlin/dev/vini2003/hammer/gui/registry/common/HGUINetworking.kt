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

package dev.vini2003.hammer.gui.registry.common

import dev.vini2003.hammer.core.HC
import dev.vini2003.hammer.gui.impl.common.packet.sync.SyncScreenHandlerPacket
import dev.vini2003.hammer.core.api.common.packet.Packet
import dev.vini2003.hammer.gui.impl.common.packet.widget.*
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object HGUINetworking {
	@JvmField
	val SYNC_SCREEN_HANDLER = HC.id("sync_screen_handler")
	
	@JvmField
	val CHARACTER_TYPED = HC.id("character_typed")
	@JvmField
	val FOCUS_GAINED = HC.id("focus_gained")
	@JvmField
	val FOCUS_RELEASED = HC.id("focus_released")
	@JvmField
	val KEY_PRESSED = HC.id("key_pressed")
	@JvmField
	val KEY_RELEASED = HC.id("key_released")
	@JvmField
	val MOUSE_CLICKED = HC.id("mouse_clicked")
	@JvmField
	val MOUSE_DRAGGED = HC.id("mouse_dragged")
	@JvmField
	val MOUSE_MOVED = HC.id("mouse_moved")
	@JvmField
	val MOUSE_RELEASED = HC.id("mouse_released")
	@JvmField
	val MOUSE_SCROLLED = HC.id("mouse_scrolled")
	
	fun init() {
		ServerPlayNetworking.registerGlobalReceiver(SYNC_SCREEN_HANDLER, Packet.serverHandler<SyncScreenHandlerPacket>())
		
		ServerPlayNetworking.registerGlobalReceiver(CHARACTER_TYPED, Packet.serverHandler<CharacterTypedPacket>())
		ServerPlayNetworking.registerGlobalReceiver(FOCUS_GAINED, Packet.serverHandler<FocusGainedPacket>())
		ServerPlayNetworking.registerGlobalReceiver(FOCUS_RELEASED, Packet.serverHandler<FocusReleasedPacket>())
		ServerPlayNetworking.registerGlobalReceiver(KEY_PRESSED, Packet.serverHandler<KeyPressedPacket>())
		ServerPlayNetworking.registerGlobalReceiver(KEY_RELEASED, Packet.serverHandler<KeyReleasedPacket>())
		ServerPlayNetworking.registerGlobalReceiver(MOUSE_CLICKED, Packet.serverHandler<MouseClickedPacket>())
		ServerPlayNetworking.registerGlobalReceiver(MOUSE_DRAGGED, Packet.serverHandler<MouseDraggedPacket>())
		ServerPlayNetworking.registerGlobalReceiver(MOUSE_MOVED, Packet.serverHandler<MouseMovedPacket>())
		ServerPlayNetworking.registerGlobalReceiver(MOUSE_RELEASED, Packet.serverHandler<MouseReleasedPacket>())
		ServerPlayNetworking.registerGlobalReceiver(MOUSE_SCROLLED, Packet.serverHandler<MouseScrolledPacket>())
	}
}