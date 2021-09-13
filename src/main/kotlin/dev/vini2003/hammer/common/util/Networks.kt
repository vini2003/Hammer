package dev.vini2003.hammer.common.util

import dev.vini2003.hammer.H
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

object Networks {
	@JvmStatic
	val Initialize = H.id("initialize")
	
	@JvmStatic
	val WidgetUpdate = H.id("update")
	
	@JvmStatic
	val MouseMoved = H.id("mouse_move")
	
	@JvmStatic
	val MouseClicked = H.id("mouse_click")
	
	@JvmStatic
	val MouseReleased = H.id("mouse_release")
	
	@JvmStatic
	val MouseDragged = H.id("mouse_drag")
	
	@JvmStatic
	val MouseScrolled = H.id("mouse_scroll")
	
	@JvmStatic
	val KeyPressed = H.id("key_press")
	
	@JvmStatic
	val KeyReleased = H.id("key_release")
	
	@JvmStatic
	val CharacterTyped = H.id("char_type")
	
	@JvmStatic
	val FocusGained = H.id("focus_gain")
	
	@JvmStatic
	val FocusReleased = H.id("focus_release")
	
	@JvmStatic
	fun toServer(id: Identifier, buf: PacketByteBuf) {
		ClientPlayNetworking.send(id, buf)
	}
	
	@JvmStatic
	fun ofInitialize(syncId: Int, width: Int, height: Int): PacketByteBuf {
		val buf = PacketByteBuf(Unpooled.buffer())
		buf.writeInt(syncId)
		buf.writeInt(width)
		buf.writeInt(height)
		return buf
	}
	
	@JvmStatic
	fun ofMouseMove(syncId: Int, hash: Int, x: Float, y: Float): PacketByteBuf {
		val buf = PacketByteBuf(Unpooled.buffer())
		buf.writeInt(syncId)
		buf.writeIdentifier(MouseMoved)
		buf.writeInt(hash)
		buf.writeFloat(x)
		buf.writeFloat(y)
		return buf
	}
	
	@JvmStatic
	fun ofMouseClick(syncId: Int, hash: Int, x: Float, y: Float, button: Int): PacketByteBuf {
		val buf = PacketByteBuf(Unpooled.buffer())
		buf.writeInt(syncId)
		buf.writeIdentifier(MouseClicked)
		buf.writeInt(hash)
		buf.writeFloat(x)
		buf.writeFloat(y)
		buf.writeInt(button)
		return buf
	}
	
	@JvmStatic
	fun ofMouseRelease(syncId: Int, hash: Int, x: Float, y: Float, button: Int): PacketByteBuf {
		val buf = PacketByteBuf(Unpooled.buffer())
		buf.writeInt(syncId)
		buf.writeIdentifier(MouseReleased)
		buf.writeInt(hash)
		buf.writeFloat(x)
		buf.writeFloat(y)
		buf.writeInt(button)
		return buf
	}
	
	@JvmStatic
	fun ofMouseDrag(syncId: Int, hash: Int, x: Float, y: Float, button: Int, deltaX: Double, deltaY: Double): PacketByteBuf {
		val buf = PacketByteBuf(Unpooled.buffer())
		buf.writeInt(syncId)
		buf.writeIdentifier(MouseDragged)
		buf.writeInt(hash)
		buf.writeFloat(x)
		buf.writeFloat(y)
		buf.writeInt(button)
		buf.writeDouble(deltaX)
		buf.writeDouble(deltaY)
		return buf
	}
	
	@JvmStatic
	fun ofMouseScroll(syncId: Int, hash: Int, x: Float, y: Float, deltaY: Double): PacketByteBuf {
		val buf = PacketByteBuf(Unpooled.buffer())
		buf.writeInt(syncId)
		buf.writeIdentifier(MouseScrolled)
		buf.writeInt(hash)
		buf.writeFloat(x)
		buf.writeFloat(y)
		buf.writeDouble(deltaY)
		return buf
	}
	
	@JvmStatic
	fun ofKeyPress(syncId: Int, hash: Int, keyCode: Int, scanCode: Int, keyModifiers: Int): PacketByteBuf {
		val buf = PacketByteBuf(Unpooled.buffer())
		buf.writeInt(syncId)
		buf.writeIdentifier(KeyPressed)
		buf.writeInt(hash)
		buf.writeInt(keyCode)
		buf.writeInt(scanCode)
		buf.writeInt(keyModifiers)
		return buf
	}
	
	@JvmStatic
	fun ofKeyRelease(syncId: Int, hash: Int, keyCode: Int, scanCode: Int, keyModifiers: Int): PacketByteBuf {
		val buf = PacketByteBuf(Unpooled.buffer())
		buf.writeInt(syncId)
		buf.writeIdentifier(KeyReleased)
		buf.writeInt(hash)
		buf.writeInt(keyCode)
		buf.writeInt(scanCode)
		buf.writeInt(keyModifiers)
		return buf
	}
	
	@JvmStatic
	fun ofCharType(syncId: Int, hash: Int, character: Char, keyCode: Int): PacketByteBuf {
		val buf = PacketByteBuf(Unpooled.buffer())
		buf.writeInt(syncId)
		buf.writeIdentifier(CharacterTyped)
		buf.writeInt(hash)
		buf.writeChar(character.toInt())
		buf.writeInt(keyCode)
		return buf
	}
	
	@JvmStatic
	fun ofFocusGain(syncId: Int, hash: Int): PacketByteBuf {
		val buf = PacketByteBuf(Unpooled.buffer())
		buf.writeInt(syncId)
		buf.writeIdentifier(FocusGained)
		buf.writeInt(hash)
		return buf
	}
	
	@JvmStatic
	fun ofFocusRelease(syncId: Int, hash: Int): PacketByteBuf {
		val buf = PacketByteBuf(Unpooled.buffer())
		buf.writeInt(syncId)
		buf.writeIdentifier(FocusReleased)
		buf.writeInt(hash)
		return buf
	}
}