package dev.vini2003.hammer.interaction.common.packet.sync

import dev.vini2003.hammer.common.packet.Packet
import dev.vini2003.hammer.common.util.serializer.UnsupportedSerializer
import dev.vini2003.hammer.interaction.common.interaction.InteractionRule
import dev.vini2003.hammer.interaction.common.manager.InteractionRuleManager
import kotlinx.serialization.Serializable

@Serializable
data class SyncInteractionRulesPacket(val rules: List<InteractionRule<@Serializable(with = UnsupportedSerializer::class) Any>>) : Packet<SyncInteractionRulesPacket>() {
	// Ignore is used to differentiate the two constructors, due to having the same JVM signature after type erasure.
	constructor(rules: List<InteractionRule<*>>, ignore: Byte = 0) : this(rules.map { key -> key as InteractionRule<Any> })
	
	override fun receive(context: ClientContext) {
		context.client.executeTask {
			InteractionRuleManager.clear(context.client.player!!)
			
			rules.forEach { rule -> InteractionRuleManager.add(context.client.player!!, rule) }
		}
	}
}