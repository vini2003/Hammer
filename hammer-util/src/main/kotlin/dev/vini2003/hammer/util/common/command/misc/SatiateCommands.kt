package dev.vini2003.hammer.util.common.command.misc

import com.mojang.brigadier.context.CommandContext
import dev.vini2003.hammer.command.common.command.ServerRootCommand
import dev.vini2003.hammer.command.common.util.extension.argument
import dev.vini2003.hammer.command.common.util.extension.command
import dev.vini2003.hammer.command.common.util.extension.execute
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.TranslatableText

val SATIATE_COMMAND = ServerRootCommand {
	val execute: CommandContext<ServerCommandSource>.(Boolean, Collection<ServerPlayerEntity>) -> Unit =
		{ state, players ->
			players.forEach { player ->
				player.hungerManager.foodLevel = 20
				player.hungerManager.saturationLevel = 20.0F
				
				if (player == source.player) {
					source.sendFeedback(TranslatableText("command.hammer.satiate.self"), true)
				} else {
					source.sendFeedback(TranslatableText("command.hammer.satiate.other", player.displayName), true)
				}
			}
		}
	
	command("satiate") {
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
}