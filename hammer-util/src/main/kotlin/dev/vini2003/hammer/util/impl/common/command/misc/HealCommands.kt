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

package dev.vini2003.hammer.util.impl.common.command.misc

import com.mojang.brigadier.context.CommandContext
import dev.vini2003.hammer.command.api.common.command.ServerRootCommand
import dev.vini2003.hammer.command.api.common.util.extension.argument
import dev.vini2003.hammer.command.api.common.util.extension.command
import dev.vini2003.hammer.command.api.common.util.extension.execute
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.TranslatableText

val HEAL_COMMAND = ServerRootCommand {
	val execute: CommandContext<ServerCommandSource>.(Boolean, Collection<ServerPlayerEntity>) -> Unit =
		{ state, players ->
			players.forEach { player ->
				player.health = player.maxHealth
				
				if (player == source.player) {
					source.sendFeedback(TranslatableText("command.hammer.heal.self"), true)
				} else {
					source.sendFeedback(TranslatableText("command.hammer.heal.other", player.displayName), true)
				}
			}
		}
	
	command("heal") {
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