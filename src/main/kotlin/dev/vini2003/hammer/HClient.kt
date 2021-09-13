package dev.vini2003.hammer

import dev.vini2003.hammer.registry.client.HEvents
import dev.vini2003.hammer.registry.client.HCommands
import dev.vini2003.hammer.registry.client.HScreens
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.api.FabricLoader

object HClient : ClientModInitializer {
	override fun onInitializeClient() {
		HEvents.init()
		
		if (FabricLoader.getInstance().isDevelopmentEnvironment) {
			HCommands.init()
			HScreens.initialize()
		}
	}
}