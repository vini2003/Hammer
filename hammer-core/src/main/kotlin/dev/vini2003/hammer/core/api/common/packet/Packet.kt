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

package dev.vini2003.hammer.core.api.common.packet

import dev.vini2003.hammer.core.api.common.util.BufUtils
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity

/**
 * A [Packet] is serializable class whose contents are serialized and deserialized
 * through ProtoBuf.
 *
 * A packet should extend [Packet], passing its own type as `T`, and implement the
 * respective receiver method for its receiving side.
 *
 * A packet should be sent by sending a [PacketByteBuf] serialized with [BufUtils.toPacketByteBuf],
 * and received by receiving a [Packet] deserialized with [BufUtils.fromPacketByteBuf].
 *
 * A packet should register a [clientHandler] or [serverHandler], depending on its receiving
 * side - **however**, due to a Kotlin compiler issue, you **must** copy [clientHandler] and
 * [serverHandler] to a utility class in your own project and use **that** function instead!
 */
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
}