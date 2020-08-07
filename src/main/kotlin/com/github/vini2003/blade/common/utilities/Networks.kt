package com.github.vini2003.blade.common.utilities

import com.github.vini2003.blade.Blade
import com.github.vini2003.blade.common.handler.BaseScreenHandler
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

class Networks {
	companion object {
		@JvmStatic
		val INITIALIZE = Blade.identifier("initialize")

		@JvmStatic
		val WIDGET_UPDATE = Blade.identifier("update")

		@JvmStatic
		val MOUSE_MOVE = Blade.identifier("mouse_move")

		@JvmStatic
		val MOUSE_CLICK = Blade.identifier("mouse_click")

		@JvmStatic
		val MOUSE_RELEASE = Blade.identifier("mouse_release")

		@JvmStatic
		val MOUSE_DRAG = Blade.identifier("mouse_drag")

		@JvmStatic
		val MOUSE_SCROLL = Blade.identifier("mouse_scroll")

		@JvmStatic
		val KEY_PRESS = Blade.identifier("key_press")

		@JvmStatic
		val KEY_RELEASE = Blade.identifier("key_release")

		@JvmStatic
		val CHAR_TYPE = Blade.identifier("char_type")

		@JvmStatic
		val FOCUS_GAIN = Blade.identifier("focus_gain")

		@JvmStatic
		val FOCUS_RELEASE = Blade.identifier("focus_release")

		init {
			ServerSidePacketRegistry.INSTANCE.register(WIDGET_UPDATE) { context, buf ->
				val syncId = buf.readInt()
				val id = buf.readIdentifier()

				buf.retain()

				context.taskQueue.execute {
					context.player.server!!.playerManager.playerList.forEach {
						if (it.currentScreenHandler.syncId == syncId && it.currentScreenHandler is BaseScreenHandler) {
							(it.currentScreenHandler as BaseScreenHandler).handlePacket(id, buf)
						}
					}
				}
			}

			ServerSidePacketRegistry.INSTANCE.register(INITIALIZE) { context, buf ->
				val syncId = buf.readInt()

				buf.retain()

				context.taskQueue.execute {
					context.player.server!!.playerManager.playerList.forEach { it ->
						if (it.currentScreenHandler.syncId == syncId && it.currentScreenHandler is BaseScreenHandler) {
							(it.currentScreenHandler as BaseScreenHandler).also {
								it.slots.clear()
								it.widgets.clear()
								it.initialize(buf.readInt(), buf.readInt())
							}
						}
					}
				}
			}
		}

		@JvmStatic
		fun initialize() {
		}

		@JvmStatic
		fun toServer(id: Identifier, buf: PacketByteBuf) {
			ClientSidePacketRegistry.INSTANCE.sendToServer(id, buf)
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
			buf.writeIdentifier(MOUSE_MOVE)
			buf.writeInt(hash)
			buf.writeFloat(x)
			buf.writeFloat(y)
			return buf
		}

		@JvmStatic
		fun ofMouseClick(syncId: Int, hash: Int, x: Float, y: Float, button: Int): PacketByteBuf {
			val buf = PacketByteBuf(Unpooled.buffer())
			buf.writeInt(syncId)
			buf.writeIdentifier(MOUSE_CLICK)
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
			buf.writeIdentifier(MOUSE_RELEASE)
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
			buf.writeIdentifier(MOUSE_DRAG)
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
			buf.writeIdentifier(MOUSE_SCROLL)
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
			buf.writeIdentifier(KEY_PRESS)
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
			buf.writeIdentifier(KEY_RELEASE)
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
			buf.writeIdentifier(CHAR_TYPE)
			buf.writeInt(hash)
			buf.writeChar(character.toInt())
			buf.writeInt(keyCode)
			return buf
		}

		@JvmStatic
		fun ofFocusGain(syncId: Int, hash: Int): PacketByteBuf {
			val buf = PacketByteBuf(Unpooled.buffer())
			buf.writeInt(syncId)
			buf.writeIdentifier(FOCUS_GAIN)
			buf.writeInt(hash)
			return buf
		}

		@JvmStatic
		fun ofFocusRelease(syncId: Int, hash: Int): PacketByteBuf {
			val buf = PacketByteBuf(Unpooled.buffer())
			buf.writeInt(syncId)
			buf.writeIdentifier(FOCUS_RELEASE)
			buf.writeInt(hash)
			return buf
		}
	}
}