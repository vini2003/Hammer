package dev.vini2003.hammer.gravity.registry.client

import dev.vini2003.hammer.common.packet.Packet
import dev.vini2003.hammer.gravity.common.packet.SyncGravitiesPacket
import dev.vini2003.hammer.gravity.registry.common.HGNetworking.SYNC_GRAVITIES
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

object HGNetworking {
	fun init() {
		ClientPlayNetworking.registerGlobalReceiver(SYNC_GRAVITIES, Packet.clientHandler<SyncGravitiesPacket>())
	}
}