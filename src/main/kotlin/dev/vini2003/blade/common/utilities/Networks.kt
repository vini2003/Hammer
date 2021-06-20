package dev.vini2003.blade.common.utilities

import dev.vini2003.blade.BL
import dev.vini2003.blade.common.handler.BaseScreenHandler
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

class Networks {
	companion object {
		@JvmStatic
		val Initialize = BL.id("initialize")

		@JvmStatic
		val WidgetUpdate = BL.id("update")

		@JvmStatic
		val MouseMoved = BL.id("mouse_move")

		@JvmStatic
		val MouseClicked = BL.id("mouse_click")

		@JvmStatic
		val MouseReleased = BL.id("mouse_release")

		@JvmStatic
		val MouseDragged = BL.id("mouse_drag")

		@JvmStatic
		val MouseScrolled = BL.id("mouse_scroll")

		@JvmStatic
		val KeyPressed = BL.id("key_press")

		@JvmStatic
		val KeyReleased = BL.id("key_release")

		@JvmStatic
		val CharacterTyped = BL.id("char_type")

		@JvmStatic
		val FocusGained = BL.id("focus_gain")

		@JvmStatic
		val FocusReleased = BL.id("focus_release")

		init {
			ServerPlayNetworking.registerGlobalReceiver(WidgetUpdate) { server, player, handler, buf, responseSender ->
				val syncId = buf.readInt()
				val id = buf.readIdentifier()
				
				buf.retain()

				server.execute {
					server!!.playerManager.playerList.forEach {
						if (it.currentScreenHandler.syncId == syncId && it.currentScreenHandler is BaseScreenHandler) {
							(it.currentScreenHandler as BaseScreenHandler).handlePacket(id, PacketByteBuf(buf.copy()))
						}
					}
				}
			}

			ServerPlayNetworking.registerGlobalReceiver(Initialize) { server, player, handler, buf, responseSender ->
				val syncId = buf.readInt()
				val width = buf.readInt()
				val height = buf.readInt()

				buf.retain()

				server.execute {
					server.playerManager.playerList.forEach { it ->
						if (it.currentScreenHandler.syncId == syncId && it.currentScreenHandler is BaseScreenHandler) {
							(it.currentScreenHandler as BaseScreenHandler).also {
								it.slots.clear()
								it.widgets.clear()
								it.initialize(width, height)
							}
						}
					}
				}
			}
		}

		@JvmStatic
		fun init() {
		}

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
}