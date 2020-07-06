package dev.vini2003.hammer.chat.registry.client

import dev.vini2003.hammer.chat.client.command.TOGGLE_CHAT_COMMAND
import dev.vini2003.hammer.chat.client.command.TOGGLE_FEEDBACK_COMMAND
import dev.vini2003.hammer.chat.client.command.TOGGLE_WARNINGS_COMMAND
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.DISPATCHER

object HCCommands {
	fun init() {
		TOGGLE_CHAT_COMMAND.register(DISPATCHER)
		TOGGLE_FEEDBACK_COMMAND.register(DISPATCHER)
		TOGGLE_WARNINGS_COMMAND.register(DISPATCHER)
	}
}