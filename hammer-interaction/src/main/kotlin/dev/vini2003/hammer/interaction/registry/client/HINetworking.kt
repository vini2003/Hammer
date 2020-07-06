package dev.vini2003.hammer.interaction.registry.client

import dev.vini2003.hammer.common.packet.Packet
import dev.vini2003.hammer.interaction.common.packet.sync.SyncInteractionRulesPacket
import dev.vini2003.hammer.interaction.registry.common.HINetworking.SYNC_INTERACTION_RULES
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

object HINetworking {
	fun init() {
		ClientPlayNetworking.registerGlobalReceiver(SYNC_INTERACTION_RULES, Packet.clientHandler<SyncInteractionRulesPacket>())
	}
}