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

package dev.vini2003.hammer.util.registry.common;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.vini2003.hammer.core.api.common.util.PlayerUtil;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;

public class HUEvents {
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(
					CommandManager.literal("freeze").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var players = EntityArgumentType.getPlayers(context, "players");
								
								for (var player : players) {
									PlayerUtil.setFrozen(player, true);
								}
								
								return Command.SINGLE_SUCCESS;
							})
					)
			);
			
			dispatcher.register(
					CommandManager.literal("unfreeze").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var players = EntityArgumentType.getPlayers(context, "players");
								
								for (var player : players) {
									PlayerUtil.setFrozen(player, false);
								}
								
								return Command.SINGLE_SUCCESS;
							})
					)
			);
			
			dispatcher.register(
					CommandManager.literal("heal").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var players = EntityArgumentType.getPlayers(context, "players");
								
								for (var player : players) {
									player.setHealth(player.getMaxHealth());
								}
								
								return Command.SINGLE_SUCCESS;
							})
					)
			);
			
			dispatcher.register(
					CommandManager.literal("satiate").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var players = EntityArgumentType.getPlayers(context, "players");
								
								for (var player : players) {
									player.getHungerManager().setFoodLevel(20);
									player.getHungerManager().setSaturationLevel(20.0F);
								}
								
								return Command.SINGLE_SUCCESS;
							})
					)
			);
			
			dispatcher.register(
					CommandManager.literal("fly_speed").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("speed", IntegerArgumentType.integer(1, 200)).then(
									CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
										var speed = IntegerArgumentType.getInteger(context, "speed");
										var players = EntityArgumentType.getPlayers(context, "players");
										
										var buf = PacketByteBufs.create();
										buf.writeInt(speed);
										
										for (var player : players) {
											player.getAbilities().setFlySpeed(speed / 20.0F);
											
											ServerPlayNetworking.send(player, HUNetworking.FLY_SPEED_UPDATE, PacketByteBufs.duplicate(buf));
										}
										
										return Command.SINGLE_SUCCESS;
									})
							)
					)
			);
			
			dispatcher.register(
					CommandManager.literal("walk_speed").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("speed", IntegerArgumentType.integer(1, 200)).then(
									CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
										var speed = IntegerArgumentType.getInteger(context, "speed");
										var players = EntityArgumentType.getPlayers(context, "players");
										
										var buf = PacketByteBufs.create();
										buf.writeInt(speed);
										
										for (var player : players) {
											player.getAbilities().setWalkSpeed(speed / 20.0F);
											
											ServerPlayNetworking.send(player, HUNetworking.WALK_SPEED_UPDATE, PacketByteBufs.duplicate(buf));
										}
										
										return Command.SINGLE_SUCCESS;
									})
							)
					)
			);
		});
	}
}
