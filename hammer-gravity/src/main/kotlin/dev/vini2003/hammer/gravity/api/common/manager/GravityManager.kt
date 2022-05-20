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

package dev.vini2003.hammer.gravity.api.common.manager

import com.google.common.collect.Maps
import dev.vini2003.hammer.gravity.impl.common.packet.SyncGravitiesPacket
import dev.vini2003.hammer.gravity.registry.common.HGNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.World

object GravityManager {
	private val GRAVITIES: MutableMap<RegistryKey<World>, Float> = Maps.newConcurrentMap()
	
	@JvmStatic
	fun get(world: World): Float {
		return GRAVITIES.getOrDefault(world.registryKey, 0.08F)
	}
	
	@JvmStatic
	fun get(key: RegistryKey<World>): Float {
		return GRAVITIES.getOrDefault(key, 0.08F)
	}
	
	@JvmStatic
	fun set(world: World, gravity: Float) {
		GRAVITIES[world.registryKey] = gravity
		
		if (!world.isClient) {
			sync()
		}
	}
	
	@JvmStatic
	fun set(key: RegistryKey<World>, gravity: Float) {
		GRAVITIES[key] = gravity
	}
	
	@JvmStatic
	fun reset(world: World) {
		GRAVITIES.remove(world.registryKey)
		
		if (!world.isClient) {
			sync()
		}
	}
	
	@JvmStatic
	fun reset() {
		GRAVITIES.clear()
	}
	
	@JvmStatic
	fun reset(key: RegistryKey<World>) {
		GRAVITIES.remove(key)
	}
	
	private fun sync() {
		try {
			if (InstanceUtils.IS_SERVER) {
				syncOnServer()
			} else {
				syncOnClient()
			}
		} catch (e: Exception) {
			return
		}
	}
	
	private fun syncOnClient() {
		val players = InstanceUtils.CLIENT?.server?.playerManager?.playerList ?: return
		
		syncWith(players)
	}
	
	private fun syncOnServer() {
		val players = InstanceUtils.SERVER?.playerManager?.playerList ?: return
		
		syncWith(players)
	}
	
	private fun syncWith(players: List<ServerPlayerEntity>) {
		val packet = SyncGravitiesPacket(GRAVITIES)
		val buf = BufUtils.toPacketByteBuf(packet)
		
		players.forEach { player ->
			ServerPlayNetworking.send(player, HGNetworking.SYNC_GRAVITIES, PacketByteBufs.duplicate(buf))
		}
	}
	
	private fun syncWith(player: ServerPlayerEntity) {
		val packet = SyncGravitiesPacket(GRAVITIES)
		val buf = BufUtils.toPacketByteBuf(packet)
		
		ServerPlayNetworking.send(player, HGNetworking.SYNC_GRAVITIES, PacketByteBufs.duplicate(buf))
	}
	
	/**
	 * [ServerPlayConnectionEvents.Join]
	 */
	object ServerPlayConnectionEventsJoinListener : ServerPlayConnectionEvents.Join {
		override fun onPlayReady(handler: ServerPlayNetworkHandler, sender: PacketSender, server: MinecraftServer) {
			syncWith(handler.player)
		}
	}
}