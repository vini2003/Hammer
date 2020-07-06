package dev.vini2003.hammer.`interface`


import dev.vini2003.hammer.`interface`.registry.common.HIFEvents
import dev.vini2003.hammer.`interface`.registry.common.HIFNetworking
import net.fabricmc.api.ModInitializer

object HIF : ModInitializer {
	override fun onInitialize() {
		HIFNetworking.init()
		HIFEvents.init()
	}
}