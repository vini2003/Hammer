package dev.vini2003.hammer.gravity.registry.common

import dev.vini2003.hammer.gravity.common.command.gravity.GRAVITY_COMMAND
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback

object HGCommands {
	fun init() {
		CommandRegistrationCallback.EVENT.register { dispatcher, dedicated ->
			GRAVITY_COMMAND.register(dispatcher)
		}
	}
}