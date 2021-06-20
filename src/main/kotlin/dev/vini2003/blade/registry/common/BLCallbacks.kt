package dev.vini2003.blade.registry.common

import dev.vini2003.blade.common.handler.BaseScreenHandler
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents

class BLCallbacks {
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