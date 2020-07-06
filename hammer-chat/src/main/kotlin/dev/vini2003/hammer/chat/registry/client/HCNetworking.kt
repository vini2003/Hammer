package dev.vini2003.hammer.chat.registry.client

import dev.vini2003.hammer.chat.common.packet.ToggleChatPacket
import dev.vini2003.hammer.chat.common.packet.ToggleFeedbackPacket
import dev.vini2003.hammer.chat.common.packet.ToggleGlobalChatPacket
import dev.vini2003.hammer.chat.common.packet.ToggleWarningsPacket
import dev.vini2003.hammer.chat.registry.common.HCNetworking.TOGGLE_CHAT
import dev.vini2003.hammer.chat.registry.common.HCNetworking.TOGGLE_FEEDBACK
import dev.vini2003.hammer.chat.registry.common.HCNetworking.TOGGLE_GLOBAL_CHAT
import dev.vini2003.hammer.chat.registry.common.HCNetworking.TOGGLE_WARNINGS
import dev.vini2003.hammer.common.packet.Packet
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

object HCNetworking {
	fun init() {
		ClientPlayNetworking.registerGlobalReceiver(TOGGLE_CHAT, Packet.clientHandler<ToggleChatPacket>())
		ClientPlayNetworking.registerGlobalReceiver(TOGGLE_GLOBAL_CHAT, Packet.clientHandler<ToggleGlobalChatPacket>())
		ClientPlayNetworking.registerGlobalReceiver(TOGGLE_FEEDBACK, Packet.clientHandler<ToggleFeedbackPacket>())
		ClientPlayNetworking.registerGlobalReceiver(TOGGLE_WARNINGS, Packet.clientHandler<ToggleWarningsPacket>())
	}
}