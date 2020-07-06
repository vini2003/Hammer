package dev.vini2003.hammer.chat.common.packet

import dev.vini2003.hammer.chat.registry.common.HCValues
import dev.vini2003.hammer.common.packet.Packet
import kotlinx.serialization.Serializable

@Serializable
class ToggleGlobalChatPacket(val state: Boolean) : Packet<ToggleGlobalChatPacket>() {
	override fun receive(context: ClientContext) {
		HCValues.SHOW_GLOBAL_CHAT = state
	}
}