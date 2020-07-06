package dev.vini2003.hammer.`interface`.common.packet.widget

import dev.vini2003.hammer.`interface`.common.screen.handler.BaseScreenHandler
import dev.vini2003.hammer.common.packet.Packet
import kotlinx.serialization.Serializable

@Serializable
data class FocusGainedPacket(val syncId: Int, val widgetHash: Int) : Packet<FocusGainedPacket>() {
	override fun receive(context: ServerContext) {
		context.server.playerManager.playerList.map { player ->
			player.currentScreenHandler
		}.filterIsInstance<BaseScreenHandler>().firstOrNull { handler ->
			handler.syncId == syncId
		}?.widgets?.firstOrNull { widget ->
			widget.hash == widgetHash
		}?.onFocusGained()
	}
}