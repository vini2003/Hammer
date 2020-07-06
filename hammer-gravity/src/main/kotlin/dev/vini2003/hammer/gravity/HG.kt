package dev.vini2003.hammer.gravity

import dev.vini2003.hammer.gravity.registry.common.HGCommands
import dev.vini2003.hammer.gravity.registry.common.HGEvents
import dev.vini2003.hammer.gravity.registry.common.HGNetworking
import net.fabricmc.api.ModInitializer

object HG : ModInitializer {
	override fun onInitialize() {
		HGNetworking.init()
		HGCommands.init()
		HGEvents.init()
	}
}