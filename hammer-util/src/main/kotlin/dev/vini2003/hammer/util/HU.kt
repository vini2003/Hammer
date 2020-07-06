package dev.vini2003.hammer.util

import dev.vini2003.hammer.util.registry.common.HUNetworking
import net.fabricmc.api.ModInitializer

object HU : ModInitializer {
	override fun onInitialize() {
		HUNetworking.init()
	}
}