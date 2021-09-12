package dev.vini2003.blade

import dev.vini2003.blade.registry.client.BLEvents
import dev.vini2003.blade.registry.client.BLCommands
import dev.vini2003.blade.registry.client.BLScreens
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.loader.api.FabricLoader

object BLClient : ClientModInitializer {
	override fun onInitializeClient() {
		BLEvents.init()
		
		if (FabricLoader.getInstance().isDevelopmentEnvironment) {
			BLCommands.init()
			BLScreens.initialize()
		}
	}
}