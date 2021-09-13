package dev.vini2003.hammer.registry.client

import dev.vini2003.hammer.common.screen.handler.BaseScreenHandler
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents

class HEvents {
	companion object {
		fun init() {
			ClientTickEvents.END_CLIENT_TICK.register { client ->
				val player = client.player
				
				if (player != null) {
					val screenHandler = player.currentScreenHandler
					
					if (screenHandler is BaseScreenHandler) {
						screenHandler.tick()
					}
				}
			}
		}
	}
}