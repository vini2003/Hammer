package dev.vini2003.hammer.gui.common.packet.widget

import dev.vini2003.hammer.gui.common.screen.handler.BaseScreenHandler
import dev.vini2003.hammer.common.packet.Packet
import kotlinx.serialization.Serializable

@Serializable
data class MouseDraggedPacket(val syncId: Int, val widgetHash: Int, val x: Float, val y: Float, val button: Int, val deltaX: Double, val deltaY: Double) : Packet<MouseDraggedPacket>() {
	override fun receive(context: ServerContext) {
		context.server.playerManager.playerList.map { player ->
			player.currentScreenHandler
		}.filterIsInstance<BaseScreenHandler>().firstOrNull { handler ->
			handler.syncId == syncId
		}?.widgets?.firstOrNull { widget ->
			widget.hash == widgetHash
		}?.onMouseDragged(x, y, button, deltaX, deltaY)
	}
}