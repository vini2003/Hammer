package dev.vini2003.hammer.gui.common.packet.sync

import dev.vini2003.hammer.gui.common.screen.handler.BaseScreenHandler
import dev.vini2003.hammer.common.packet.Packet
import kotlinx.serialization.Serializable

@Serializable
data class SyncScreenHandlerPacket(val syncId: Int, val width: Int, val height: Int) : Packet<SyncScreenHandlerPacket>() {
	override fun receive(context: ServerContext) {
		context.server.playerManager.playerList.map { player ->
			player.currentScreenHandler
		}.filterIsInstance<BaseScreenHandler>().firstOrNull { handler ->
			handler.syncId == syncId
		}?.also { handler ->
			handler.slots.clear()
			handler.widgets.clear()
			
			handler.initialize(width, height)
		}
	}
}