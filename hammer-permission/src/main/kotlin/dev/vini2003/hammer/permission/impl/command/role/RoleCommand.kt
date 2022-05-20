/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.permission.impl.command.role

import dev.vini2003.hammer.core.api.client.util.InstanceUtil.FABRIC
import dev.vini2003.hammer.command.api.common.command.ServerRootCommand
import dev.vini2003.hammer.command.api.common.util.extension.*
import dev.vini2003.hammer.permission.api.common.manager.RoleManager
import dev.vini2003.hammer.permission.api.common.util.extension.hasRole
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