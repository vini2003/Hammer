package dev.vini2003.hammer.gravity.common.packet

import dev.vini2003.hammer.common.packet.Packet
import dev.vini2003.hammer.common.util.serializer.RegistryKeySerializer
import dev.vini2003.hammer.common.util.serializer.UnsupportedSerializer
import dev.vini2003.hammer.gravity.common.manager.GravityManager
import kotlinx.serialization.Serializable
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.World

@Serializable
data class SyncGravitiesPacket(val gravities: Map<@Serializable(with = RegistryKeySerializer::class) RegistryKey<@Serializable(with = UnsupportedSerializer::class) World>, Float>) : Packet<SyncGravitiesPacket>() {
	override fun receive(context: ClientContext) {
		context.client.executeTask {
			GravityManager.clear(context.client.world!!)
			
			gravities.forEach { (key, gravity) -> GravityManager.set(key, gravity, context.client.world!!) }
		}
	}
}