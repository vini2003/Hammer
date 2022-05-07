package dev.vini2003.hammer.example

import dev.vini2003.hammer.example.registry.common.HECommands
import dev.vini2003.hammer.example.registry.common.HEScreenHandlers
import net.fabricmc.api.ModInitializer

object HE : ModInitializer {
	override fun onInitialize() {
		HECommands.init()
		HEScreenHandlers.init()
	}
}