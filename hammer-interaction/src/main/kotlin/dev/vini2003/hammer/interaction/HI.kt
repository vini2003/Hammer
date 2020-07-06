package dev.vini2003.hammer.interaction


import dev.vini2003.hammer.interaction.registry.common.HICommands
import dev.vini2003.hammer.interaction.registry.common.HIEvents
import dev.vini2003.hammer.interaction.registry.common.HINetworking
import net.fabricmc.api.ModInitializer

object HI : ModInitializer {
	override fun onInitialize() {
		HICommands.init()
		HIEvents.init()
		HINetworking.init()
	}
}