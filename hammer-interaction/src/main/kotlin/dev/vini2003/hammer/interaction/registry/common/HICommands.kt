package dev.vini2003.hammer.interaction.registry.common

import dev.vini2003.hammer.interaction.common.command.interaction.INTERACTION_COMMAND
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback

object HICommands {
	fun init() {
		CommandRegistrationCallback.EVENT.register { dispatcher, dedicated ->
			INTERACTION_COMMAND.register(dispatcher)
		}
	}
}