package dev.vini2003.hammer.util.registry.client

import dev.vini2003.hammer.common.packet.Packet
import dev.vini2003.hammer.util.common.packet.speed.FlySpeedUpdatePacket
import dev.vini2003.hammer.util.common.packet.speed.WalkSpeedUpdatePacket
import dev.vini2003.hammer.util.registry.common.HUNetworking.FLY_SPEED_UPDATE
import dev.vini2003.hammer.util.registry.common.HUNetworking.WALK_SPEED_UPDATE
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

object HUNetworking {
	fun init() {
		ClientPlayNetworking.registerGlobalReceiver(WALK_SPEED_UPDATE, Packet.clientHandler<WalkSpeedUpdatePacket>())
		ClientPlayNetworking.registerGlobalReceiver(FLY_SPEED_UPDATE, Packet.clientHandler<FlySpeedUpdatePacket>())
	}
}