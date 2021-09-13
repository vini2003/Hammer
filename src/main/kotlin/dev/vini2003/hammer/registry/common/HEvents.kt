package dev.vini2003.hammer.registry.common

import dev.vini2003.hammer.common.screen.handler.BaseScreenHandler
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents

class HEvents {
	companion object {
		@JvmStatic
		fun init() {
			ServerTickEvents.END_SERVER_TICK.register { server ->
				server.playerManager.playerList.forEach { player ->
					val screenHandler = player.currentScreenHandler
					
					if (screenHandler is BaseScreenHandler) {
						screenHandler.tick()
					}
				}
			}
		}
	}
}