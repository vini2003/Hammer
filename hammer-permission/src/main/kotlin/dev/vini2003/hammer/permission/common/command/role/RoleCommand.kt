package dev.vini2003.hammer.permission.common.command.role

import dev.vini2003.hammer.client.util.InstanceUtils.FABRIC
import dev.vini2003.hammer.command.common.command.ServerRootCommand
import dev.vini2003.hammer.command.common.util.extension.*
import dev.vini2003.hammer.permission.common.manager.RoleManager
import dev.vini2003.hammer.permission.common.util.extension.hasRole
import net.minecraft.text.TranslatableText

val ROLE_COMMAND = ServerRootCommand {
	command("role") {
		requires { source -> source.hasPermissionLevel(4) || FABRIC.isDevelopmentEnvironment}
		
		literal("operator") {
			execute {
				val playerManager = source.server.playerManager
				val gameProfile = source.player.gameProfile
				
				if (!playerManager.isOperator(gameProfile)) {
					playerManager.addToOperators(gameProfile)
					
					source.sendFeedback(TranslatableText("command.hammer.role", "Added", "operator"), true)
				} else {
					playerManager.removeFromOperators(gameProfile)
					
					source.sendFeedback(TranslatableText("command.hammer.role", "Remove", "operator"), true)
				}
			}
		}
		
		greedyString("role") {
			suggests { _, builder ->
				RoleManager.roles().forEach { role -> builder.suggest(role.name) }

				builder.buildFuture()
			}

			execute {
				val name = getString("role")
				
				val role = RoleManager.getRoleByName(name)
				
				if (role != null) {
					if (source.player.hasRole(role)) {
						role.addTo(source.player)
						
						source.sendFeedback(TranslatableText("command.hammer.role", "Added", role.name), true)
					} else {
						role.removeFrom(source.player)
						
						source.sendFeedback(TranslatableText("command.hammer.role", "Removed", role.name), true)
					}
				}
			}
		}
	}
}