package dev.vini2003.hammer.gravity.common.manager

import com.google.common.collect.Maps
import dev.vini2003.hammer.common.util.BufUtils
import dev.vini2003.hammer.gravity.common.packet.SyncGravitiesPacket
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
	private fun syncWith(world: World) {
		val packet = SyncGravitiesPacket(GRAVITIES)
		val buf = BufUtils.toPacketByteBuf(packet)
		
		world.server?.playerManager?.playerList?.forEach { player ->
			ServerPlayNetworking.send(player, HGNetworking.SYNC_GRAVITIES, PacketByteBufs.duplicate(buf))
		}
	}
	
	@JvmStatic
	private fun syncWith(player: ServerPlayerEntity) {
		val packet = SyncGravitiesPacket(GRAVITIES)
		val buf = BufUtils.toPacketByteBuf(packet)
		
		ServerPlayNetworking.send(player, HGNetworking.SYNC_GRAVITIES, PacketByteBufs.duplicate(buf))
	}
	
	@JvmStatic
	fun clear(world: World? = null) {
		GRAVITIES.clear()
		
		if (world != null) {
			if (!world.isClient) {
				syncWith(world)
			}
		}
	}
	
	@JvmStatic
	fun set(key: RegistryKey<World>, gravity: Float, world: World? = null) {
		GRAVITIES[key] = gravity
		
		if (world != null) {
			if (!world.isClient) {
				syncWith(world)
			}
		}
	}
	
	@JvmStatic
	fun remove(key: RegistryKey<World>, world: World? = null) {
		GRAVITIES.remove(key)
		
		if (world != null) {
			if (!world.isClient) {
				syncWith(world)
			}
		}
	}
	
	@JvmStatic
	fun get(key: RegistryKey<World>): Float? {
		return GRAVITIES[key]?.times(0.08F)
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