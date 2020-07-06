package dev.vini2003.hammer.chat.client.command

import dev.vini2003.hammer.chat.registry.common.HCValues
import dev.vini2003.hammer.command.client.command.ClientRootCommand
import dev.vini2003.hammer.command.common.util.extension.command
import dev.vini2003.hammer.command.common.util.extension.execute
import net.minecraft.text.TranslatableText

val TOGGLE_FEEDBACK_COMMAND = ClientRootCommand {
	command("toggle_feedback") {
		execute {
			HCValues.SHOW_FEEDBACK = !HCValues.SHOW_FEEDBACK
			
			source.sendFeedback(TranslatableText("command.hammer.toggle_feedback", if (HCValues.SHOW_FEEDBACK) "enabled" else "disabled"))
		}
	}
}