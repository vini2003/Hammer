package dev.vini2003.hammer.util

import dev.vini2003.hammer.util.registry.client.HUNetworking
import net.fabricmc.api.ClientModInitializer

object HUClient : ClientModInitializer {
	override fun onInitializeClient() {
		HUNetworking.init()
	}
}