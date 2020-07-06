package dev.vini2003.hammer.common.packet

import dev.vini2003.hammer.common.util.BufUtils
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity

abstract class Packet<in T> {
	companion object {
		// Copy method to utility class if using outside library!
		inline fun <reified T : Packet<T>> clientHandler(): ClientPlayNetworking.PlayChannelHandler {
			return ClientPlayNetworking.PlayChannelHandler { client, handler, buf, sender ->
				val packet = BufUtils.fromPacketByteBuf<T>(buf)
				
				client.execute {
					packet.receive(ClientContext(client, handler, sender))
				}
			}
		}
		
		// Copy method to utility class if using outside library!
		inline fun <reified T : Packet<T>> serverHandler(): ServerPlayNetworking.PlayChannelHandler {
			return ServerPlayNetworking.PlayChannelHandler { server, player, handler, buf, sender ->
				val packet = BufUtils.fromPacketByteBuf<T>(buf)
				
				server.execute {
					packet.receive(ServerContext(server, player, handler, sender))
				}
			}
		}
	}
	
	class ClientContext(val client: MinecraftClient, val handler: ClientPlayNetworkHandler, val sender: PacketSender)
	class ServerContext(val server: MinecraftServer, val player: ServerPlayerEntity, val handler: ServerPlayNetworkHandler, val sender: PacketSender)
	
	open fun receive(context: ClientContext) = Unit
	open fun receive(context: ServerContext) = Unit
	
	inline fun toPacketByteBuf(): PacketByteBuf {
		return BufUtils.toPacketByteBuf(this)
	}
}