package dev.vini2003.hammer.chat.client.command

import dev.vini2003.hammer.chat.registry.common.HCValues
import dev.vini2003.hammer.command.client.command.ClientRootCommand
import dev.vini2003.hammer.command.common.util.extension.command
import dev.vini2003.hammer.command.common.util.extension.execute
import net.minecraft.text.TranslatableText

val TOGGLE_WARNINGS_COMMAND = ClientRootCommand {
	command("toggle_warnings") {
		execute {
			HCValues.SHOW_WARNINGS = !HCValues.SHOW_WARNINGS
			
			source.sendFeedback(TranslatableText("command.hammer.toggle_warnings", if (HCValues.SHOW_WARNINGS) "enabled" else "disabled"))
		}
	}
}