package dev.vini2003.hammer.chat.client.command

import dev.vini2003.hammer.chat.registry.common.HCValues
import dev.vini2003.hammer.command.client.command.ClientRootCommand
import dev.vini2003.hammer.command.common.util.extension.command
import dev.vini2003.hammer.command.common.util.extension.execute
import net.minecraft.text.TranslatableText

val TOGGLE_CHAT_COMMAND = ClientRootCommand {
	command("toggle_chat") {
		execute {
			HCValues.SHOW_CHAT = !HCValues.SHOW_CHAT
			
			source.sendFeedback(TranslatableText("command.hammer.toggle_chat", if (HCValues.SHOW_CHAT) "enabled" else "disabled"))
		}
	}
}