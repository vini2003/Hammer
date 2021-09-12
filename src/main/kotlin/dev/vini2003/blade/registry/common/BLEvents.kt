package dev.vini2003.blade.registry.common

import dev.vini2003.blade.common.handler.BaseScreenHandler
import dev.vini2003.blade.common.util.Networks
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf

class BLEvents {
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