package dev.vini2003.hammer.chat.common.util

import dev.vini2003.hammer.chat.common.packet.ToggleChatPacket
import dev.vini2003.hammer.chat.common.packet.ToggleFeedbackPacket
import dev.vini2003.hammer.chat.common.packet.ToggleGlobalChatPacket
import dev.vini2003.hammer.chat.common.packet.ToggleWarningsPacket
import dev.vini2003.hammer.chat.registry.common.HCNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.network.ServerPlayerEntity

object ChatUtils {
	@JvmStatic
	fun toggleChat(player: ServerPlayerEntity, state: Boolean) {
		val packet = ToggleChatPacket(state)
		val buf = packet.toPacketByteBuf()
		
		ServerPlayNetworking.send(player, HCNetworking.TOGGLE_CHAT, buf)
	}
	
	@JvmStatic
	fun toggleGlobalChat(player: ServerPlayerEntity, state: Boolean) {
		val packet = ToggleGlobalChatPacket(state)
		val buf = packet.toPacketByteBuf()
		
		ServerPlayNetworking.send(player, HCNetworking.TOGGLE_GLOBAL_CHAT, buf)
	}
	
	@JvmStatic
	fun toggleFeedback(player: ServerPlayerEntity, state: Boolean) {
		val packet = ToggleFeedbackPacket(state)
		val buf = packet.toPacketByteBuf()
		
		ServerPlayNetworking.send(player, HCNetworking.TOGGLE_FEEDBACK, buf)
	}
	
	@JvmStatic
	fun toggleWarnings(player: ServerPlayerEntity, state: Boolean) {
		val packet = ToggleWarningsPacket(state)
		val buf = packet.toPacketByteBuf()
		
		ServerPlayNetworking.send(player, HCNetworking.TOGGLE_WARNINGS, buf)
	}
}