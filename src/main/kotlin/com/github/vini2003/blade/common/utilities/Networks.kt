package com.github.vini2003.blade.common.utilities

import com.github.vini2003.blade.Blade
import com.github.vini2003.blade.common.handler.BaseScreenHandler
import com.github.vini2003.blade.common.widget.WidgetCollection
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier
import java.util.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class Networks {
    companion object {
        val INITIALIZE = Blade.identifier("initialize")

        val WIDGET_UPDATE = Blade.identifier("update")
        val MOUSE_MOVE = Blade.identifier("mouse_move")
        val MOUSE_CLICK = Blade.identifier("mouse_click")
        val MOUSE_RELEASE = Blade.identifier("mouse_release")
        val MOUSE_DRAG = Blade.identifier("mouse_drag")
        val MOUSE_SCROLL = Blade.identifier("mouse_scroll")
        val KEY_PRESS = Blade.identifier("key_press")
        val KEY_RELEASE = Blade.identifier("key_release")
        val CHAR_TYPE = Blade.identifier("char_type")
        val FOCUS_GAIN = Blade.identifier("focus_gain")
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

        fun initialize() {
        }

        fun toServer(id: Identifier, buf: PacketByteBuf) {
            ClientSidePacketRegistry.INSTANCE.sendToServer(id, buf)
        }

        fun ofInitialize(syncId: Int, width: Int, height: Int): PacketByteBuf {
            val buf = PacketByteBuf(Unpooled.buffer())
            buf.writeInt(syncId)
            buf.writeInt(width)
            buf.writeInt(height)
            return buf
        }

        fun ofMouseMove(syncId: Int, hash: Int, x: Float, y: Float): PacketByteBuf {
            val buf = PacketByteBuf(Unpooled.buffer())
            buf.writeInt(syncId)
            buf.writeIdentifier(MOUSE_MOVE)
            buf.writeInt(hash)
            buf.writeFloat(x)
            buf.writeFloat(y)
            return buf
        }

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

        fun ofCharType(syncId: Int, hash: Int, character: Char, keyCode: Int): PacketByteBuf {
            val buf = PacketByteBuf(Unpooled.buffer())
            buf.writeInt(syncId)
            buf.writeIdentifier(CHAR_TYPE)
            buf.writeInt(hash)
            buf.writeChar(character.toInt())
            buf.writeInt(keyCode)
            return buf
        }

        fun ofFocusGain(syncId: Int, hash: Int): PacketByteBuf {
            val buf = PacketByteBuf(Unpooled.buffer())
            buf.writeInt(syncId)
            buf.writeIdentifier(FOCUS_GAIN)
            buf.writeInt(hash)
            return buf
        }

        fun ofFocusRelease(syncId: Int, hash: Int): PacketByteBuf {
            val buf = PacketByteBuf(Unpooled.buffer())
            buf.writeInt(syncId)
            buf.writeIdentifier(FOCUS_RELEASE)
            buf.writeInt(hash)
            return buf
        }
    }
}