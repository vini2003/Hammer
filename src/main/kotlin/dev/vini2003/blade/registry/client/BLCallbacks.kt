package dev.vini2003.blade.registry.client

import dev.vini2003.blade.common.handler.BaseScreenHandler
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents

class BLCallbacks {
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