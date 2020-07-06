package dev.vini2003.hammer.util.common.command.misc

import com.mojang.brigadier.context.CommandContext
import dev.vini2003.hammer.command.common.command.ServerRootCommand
import dev.vini2003.hammer.command.common.util.extension.argument
import dev.vini2003.hammer.command.common.util.extension.command
import dev.vini2003.hammer.command.common.util.extension.execute
import dev.vini2003.hammer.common.util.extension.frozen
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.TranslatableText

val FREEZE_COMMANDS = ServerRootCommand {
	val execute: CommandContext<ServerCommandSource>.(Boolean, Collection<ServerPlayerEntity>) -> Unit =
		{ state, players ->
			players.forEach { player ->
				player.frozen = state
				
				val stateType = if (state) "freeze" else "unfreeze"
				
				if (player == source.player) {
					source.sendFeedback(TranslatableText("command.hammer.${stateType}.self", player.displayName), true)
				} else {
					source.sendFeedback(TranslatableText("command.hammer.${stateType}.other", player.displayName), true)
				}
			}
		}
	
	command("freeze") {
		requires { source -> source.hasPermissionLevel(4) }
		
		execute {
			execute(true, listOf(source.player))
		}
		
		argument("players", EntityArgumentType.players()) {
			execute {
				execute(true, EntityArgumentType.getPlayers(this, "players"))
			}
		}
	}
	
	command("unfreeze") {
		requires { source -> source.hasPermissionLevel(4) }
		
		execute {
			execute(false, listOf(source.player))
		}
		
		argument("players", EntityArgumentType.players()) {
			execute {
				execute(false, EntityArgumentType.getPlayers(this, "players"))
			}
		}
	}
}