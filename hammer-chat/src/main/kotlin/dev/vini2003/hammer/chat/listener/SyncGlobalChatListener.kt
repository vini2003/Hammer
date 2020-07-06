package dev.vini2003.hammer.chat.listener

import dev.vini2003.hammer.chat.common.packet.ToggleGlobalChatPacket
import dev.vini2003.hammer.chat.registry.common.HCNetworking
import dev.vini2003.hammer.chat.registry.common.HCValues
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler

object SyncGlobalChatListener : ServerPlayConnectionEvents.Join {
	override fun onPlayReady(handler: ServerPlayNetworkHandler, sender: PacketSender, server: MinecraftServer) {
		val packet = ToggleGlobalChatPacket(HCValues.SHOW_GLOBAL_CHAT)
		val buf = packet.toPacketByteBuf()
		
		ServerPlayNetworking.send(handler.player, HCNetworking.TOGGLE_GLOBAL_CHAT, buf)
	}
}