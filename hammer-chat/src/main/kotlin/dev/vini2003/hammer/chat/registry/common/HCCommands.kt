package dev.vini2003.hammer.chat.registry.common

import dev.vini2003.hammer.chat.common.command.TOGGLE_GLOBAL_CHAT_COMMAND
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback

object HCCommands {
	fun init() {
		CommandRegistrationCallback.EVENT.register { dispatcher, dedicated ->
			TOGGLE_GLOBAL_CHAT_COMMAND.register(dispatcher)
		}
	}
}