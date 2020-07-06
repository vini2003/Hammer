package dev.vini2003.hammer.gravity

import dev.vini2003.hammer.gravity.registry.client.HGNetworking
import net.fabricmc.api.ClientModInitializer

object HGClient : ClientModInitializer {
	override fun onInitializeClient() {
		HGNetworking.init()
	}
}