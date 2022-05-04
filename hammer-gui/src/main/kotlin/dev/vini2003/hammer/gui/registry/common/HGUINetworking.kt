package dev.vini2003.hammer.gui.registry.common

import dev.vini2003.hammer.H
import dev.vini2003.hammer.gui.common.packet.sync.SyncScreenHandlerPacket
import dev.vini2003.hammer.gui.common.packet.widget.*
import dev.vini2003.hammer.common.packet.Packet
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object HGUINetworking {
	@JvmField
	val SYNC_SCREEN_HANDLER = H.id("sync_screen_handler")
	
	@JvmField
	val CHARACTER_TYPED = H.id("character_typed")
	@JvmField
	val FOCUS_GAINED = H.id("focus_gained")
	@JvmField
	val FOCUS_RELEASED = H.id("focus_released")
	@JvmField
	val KEY_PRESSED = H.id("key_pressed")
	@JvmField
	val KEY_RELEASED = H.id("key_released")
	@JvmField
	val MOUSE_CLICKED = H.id("mouse_clicked")
	@JvmField
	val MOUSE_DRAGGED = H.id("mouse_dragged")
	@JvmField
	val MOUSE_MOVED = H.id("mouse_moved")
	@JvmField
	val MOUSE_RELEASED = H.id("mouse_released")
	@JvmField
	val MOUSE_SCROLLED = H.id("mouse_scrolled")
	
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