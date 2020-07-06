package dev.vini2003.hammer.`interface`.client.listener

import dev.vini2003.hammer.`interface`.common.screen.handler.BaseScreenHandler
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient

object ScreenHandlerClientTickListener : ClientTickEvents.EndTick {
	override fun onEndTick(client: MinecraftClient) {
		val player = client.player
		
		if (player != null) {
			val screenHandler = player.currentScreenHandler
			
			if (screenHandler is BaseScreenHandler) {
				screenHandler.tick()
			}
		}
	}
}