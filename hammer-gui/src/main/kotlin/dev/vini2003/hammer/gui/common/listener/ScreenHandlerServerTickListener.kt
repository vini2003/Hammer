package dev.vini2003.hammer.gui.common.listener

import dev.vini2003.hammer.gui.common.screen.handler.BaseScreenHandler
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.MinecraftServer

object ScreenHandlerServerTickListener : ServerTickEvents.EndTick {
	override fun onEndTick(server: MinecraftServer) {
		server.playerManager.playerList.map { player ->
			player.currentScreenHandler
		}.filterIsInstance<BaseScreenHandler>().forEach { handler ->
			handler.tick()
		}
	}
}