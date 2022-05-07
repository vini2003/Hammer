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

package dev.vini2003.hammer.chat.api.common.util

import dev.vini2003.hammer.chat.impl.common.packet.ToggleChatPacket
import dev.vini2003.hammer.chat.impl.common.packet.ToggleFeedbackPacket
import dev.vini2003.hammer.chat.impl.common.packet.ToggleGlobalChatPacket
import dev.vini2003.hammer.chat.impl.common.packet.ToggleWarningsPacket
import dev.vini2003.hammer.chat.registry.common.HCNetworking
import dev.vini2003.hammer.core.api.common.util.BufUtils
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.network.ServerPlayerEntity
import org.jetbrains.annotations.Contract

object ChatUtils {
	@JvmStatic
	@Contract(mutates = "player")
	fun toggleChat(player: ServerPlayerEntity, state: Boolean) {
		val packet = ToggleChatPacket(state)
		val buf = BufUtils.toPacketByteBuf(packet)
		
		ServerPlayNetworking.send(player, HCNetworking.TOGGLE_CHAT, buf)
	}
	
	@JvmStatic
	@Contract(mutates = "player")
	fun toggleGlobalChat(player: ServerPlayerEntity, state: Boolean) {
		val packet = ToggleGlobalChatPacket(state)
		val buf = BufUtils.toPacketByteBuf(packet)
		
		ServerPlayNetworking.send(player, HCNetworking.TOGGLE_GLOBAL_CHAT, buf)
	}
	
	@JvmStatic
	@Contract(mutates = "player")
	fun toggleFeedback(player: ServerPlayerEntity, state: Boolean) {
		val packet = ToggleFeedbackPacket(state)
		val buf = BufUtils.toPacketByteBuf(packet)
		
		ServerPlayNetworking.send(player, HCNetworking.TOGGLE_FEEDBACK, buf)
	}
	
	@JvmStatic
	@Contract(mutates = "player")
	fun toggleWarnings(player: ServerPlayerEntity, state: Boolean) {
		val packet = ToggleWarningsPacket(state)
		val buf = BufUtils.toPacketByteBuf(packet)
		
		ServerPlayNetworking.send(player, HCNetworking.TOGGLE_WARNINGS, buf)
	}
}