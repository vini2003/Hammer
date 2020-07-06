package dev.vini2003.hammer.util.common.packet.speed

import dev.vini2003.hammer.common.packet.Packet
import kotlinx.serialization.Serializable

@Serializable
data class FlySpeedUpdatePacket(val speed: Float) : Packet<FlySpeedUpdatePacket>() {
	override fun receive(context: ClientContext) {
		context.client.player?.abilities?.flySpeed = speed
	}
}