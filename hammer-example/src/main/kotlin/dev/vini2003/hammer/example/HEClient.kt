package dev.vini2003.hammer.example

import dev.vini2003.hammer.example.registry.client.HEScreens
import net.fabricmc.api.ClientModInitializer

object HEClient : ClientModInitializer {
	override fun onInitializeClient() {
		HEScreens.init()
	}
}