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

package dev.vini2003.hammer.util.impl.common.command.speed

import com.mojang.brigadier.context.CommandContext
import dev.vini2003.hammer.command.api.common.command.ServerRootCommand
import dev.vini2003.hammer.command.api.common.util.extension.*
import dev.vini2003.hammer.core.api.common.util.BufUtils
import dev.vini2003.hammer.util.impl.common.packet.speed.FlySpeedUpdatePacket
import dev.vini2003.hammer.util.registry.common.HUNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.TranslatableText

val FLY_SPEED_COMMAND = ServerRootCommand {
	command("fly_speed") {
		requires { source -> source.hasPermissionLevel(4) }
		
		val execute: CommandContext<ServerCommandSource>.(Int, Collection<ServerPlayerEntity>) -> Unit =
			{ speed, players ->
				players.forEach { player ->
					player.abilities.flySpeed = speed / 20.0F
					
					val packet = FlySpeedUpdatePacket(speed / 20.0F)
					val buf = BufUtils.toPacketByteBuf(packet)
					
					ServerPlayNetworking.send(player, HUNetworking.FLY_SPEED_UPDATE, buf)
					
					if (player == source.player) {
						source.sendFeedback(TranslatableText("command.hammer.fly_speed.self", String.format("%.2f", speed / 20.0F)), true)
					} else {
						source.sendFeedback(TranslatableText("command.hammer.fly_speed.other", player.displayName, String.format("%.2f", speed / 20.0F)), true)
					}
				}
			}
		
		int("speed", {
			execute {
				execute(getInt("speed"), listOf(source.player))
			}
			
			argument("players", EntityArgumentType.players()) {
				execute {
					execute(getInt("speed"), EntityArgumentType.getPlayers(this, "players"))
				}
			}
		}, min = 1, max = 20)
	}
}