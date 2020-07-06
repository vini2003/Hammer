package dev.vini2003.hammer.util.common.command.speed

import com.mojang.brigadier.context.CommandContext
import dev.vini2003.hammer.command.common.command.ServerRootCommand
import dev.vini2003.hammer.command.common.util.extension.*
import dev.vini2003.hammer.common.util.BufUtils
import dev.vini2003.hammer.util.common.packet.speed.FlySpeedUpdatePacket
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