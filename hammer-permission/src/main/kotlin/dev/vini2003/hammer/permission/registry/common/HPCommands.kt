package dev.vini2003.hammer.permission.registry.common

import dev.vini2003.hammer.permission.common.command.role.ROLE_COMMAND
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback

object HPCommands {
	fun init() {
		CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
			ROLE_COMMAND.register(dispatcher)
		}
	}
}