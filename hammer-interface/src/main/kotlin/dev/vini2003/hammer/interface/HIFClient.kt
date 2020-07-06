package dev.vini2003.hammer.`interface`

import dev.vini2003.hammer.`interface`.registry.client.HIFEvents
import net.fabricmc.api.ClientModInitializer

object HIFClient : ClientModInitializer {
	override fun onInitializeClient() {
		HIFEvents.init()
	}
}