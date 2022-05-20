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

package dev.vini2003.hammer.chat.impl.common.command

import dev.vini2003.hammer.chat.impl.common.packet.ToggleGlobalChatPacket
import dev.vini2003.hammer.chat.registry.common.HCNetworking
import dev.vini2003.hammer.chat.registry.common.HCValues
import dev.vini2003.hammer.command.api.common.command.ServerRootCommand
import dev.vini2003.hammer.command.api.common.util.extension.command
import dev.vini2003.hammer.command.api.common.util.extension.execute
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.text.TranslatableText

val TOGGLE_GLOBAL_CHAT_COMMAND = ServerRootCommand {
	command("toggle_global_chat") {
		execute {
			HCValues.SHOW_GLOBAL_CHAT = !HCValues.SHOW_GLOBAL_CHAT
			
			source.sendFeedback(TranslatableText("command.hammer.toggle_global_chat", if (HCValues.SHOW_GLOBAL_CHAT) "enabled" else "disabled"), true)
			
			val packet = ToggleGlobalChatPacket(HCValues.SHOW_GLOBAL_CHAT)
			val buf = BufUtils.toPacketByteBuf(packet)
			
			source.server.playerManager.playerList.forEach { player ->
				ServerPlayNetworking.send(player, HCNetworking.TOGGLE_GLOBAL_CHAT, PacketByteBufs.duplicate(buf))
			}
		}
	}
}