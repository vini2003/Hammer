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
import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import dev.vini2003.hammer.core.api.common.util.PlayerUtil;
import dev.vini2003.hammer.core.registry.common.HCConfig;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;


public class HUCommands {
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, dedicated) -> {
			dispatcher.register(
					CommandManager.literal("toggle_end").requires(source -> source.hasPermissionLevel(4)).executes(context -> {
						var source = context.getSource();
						
						HCConfig.ENABLE_END = !HCConfig.ENABLE_END;
						
						source.sendFeedback(Text.translatable("command.hammer." + (HCConfig.ENABLE_END ? "enable" : "disable") + "_end"), true);
						
						return Command.SINGLE_SUCCESS;
					})
			);
			
			dispatcher.register(
					CommandManager.literal("toggle_nether").requires(source -> source.hasPermissionLevel(4)).executes(context -> {
						var source = context.getSource();
						
						HCConfig.ENABLE_NETHER = !HCConfig.ENABLE_NETHER;
						
						source.sendFeedback(Text.translatable("command.hammer." + (HCConfig.ENABLE_NETHER ? "enable" : "disable") + "_nether"), true);
						
						return Command.SINGLE_SUCCESS;
					})
			);
			
			dispatcher.register(
					CommandManager.literal("mute").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var source = context.getSource();
								
								var players = EntityArgumentType.getPlayers(context, "players");
								
								for (var player : players) {
									ChatUtil.setMuted(player, true);
									
									source.sendFeedback(Text.translatable("command.hammer.mute.other", player.getDisplayName()), true);
								}
								
								return Command.SINGLE_SUCCESS;
							})
					).executes(context -> {
						var source = context.getSource();
						var player = source.getPlayer();
						
						ChatUtil.setMuted(player, true);
						
						source.sendFeedback(Text.translatable("command.hammer.mute.self"), true);
						
						return Command.SINGLE_SUCCESS;
					})
			);
			
			dispatcher.register(
					CommandManager.literal("unmute").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var source = context.getSource();
								
								var players = EntityArgumentType.getPlayers(context, "players");
								
								for (var player : players) {
									ChatUtil.setMuted(player, false);
									
									source.sendFeedback(Text.translatable("command.hammer.unmute.other", player.getDisplayName()), true);
								}
								
								return Command.SINGLE_SUCCESS;
							})
					).executes(context -> {
						var source = context.getSource();
						var player = source.getPlayer();
						
						ChatUtil.setMuted(player, false);
						
						source.sendFeedback(Text.translatable("command.hammer.unmute.self"), true);
						
						return Command.SINGLE_SUCCESS;
					})
			);
			
			dispatcher.register(
					CommandManager.literal("freeze").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var source = context.getSource();
								
								var players = EntityArgumentType.getPlayers(context, "players");
								
								for (var player : players) {
									PlayerUtil.setFrozen(player, true);
									
									source.sendFeedback(Text.translatable("command.hammer.freeze.other", player.getDisplayName()), true);
								}
								
								return Command.SINGLE_SUCCESS;
							})
					).executes(context -> {
						var source = context.getSource();
						var player = source.getPlayer();
						
						PlayerUtil.setFrozen(player, true);
						
						source.sendFeedback(Text.translatable("command.hammer.freeze.self"), true);
						
						return Command.SINGLE_SUCCESS;
					})
			);
			
			dispatcher.register(
					CommandManager.literal("unfreeze").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var source = context.getSource();
								
								var players = EntityArgumentType.getPlayers(context, "players");
								
								for (var player : players) {
									PlayerUtil.setFrozen(player, false);
									
									source.sendFeedback(Text.translatable("command.hammer.unfreeze.other", player.getDisplayName()), true);
								}
								
								return Command.SINGLE_SUCCESS;
							})
					).executes(context -> {
						var source = context.getSource();
						var player = source.getPlayer();
						
						PlayerUtil.setFrozen(player, false);
						
						source.sendFeedback(Text.translatable("command.hammer.unfreeze.self"), true);
						
						return Command.SINGLE_SUCCESS;
					})
			);
			
			dispatcher.register(
					CommandManager.literal("heal").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var source = context.getSource();
								
								var players = EntityArgumentType.getPlayers(context, "players");
								
								for (var player : players) {
									player.setHealth(player.getMaxHealth());

									source.sendFeedback(Text.translatable("command.hammer.heal.other", player.getDisplayName()), true);
								}
								
								return Command.SINGLE_SUCCESS;
							})
					).executes(context -> {
						var source = context.getSource();
						var player = source.getPlayer();
						
						player.setHealth(player.getMaxHealth());
						
						source.sendFeedback(Text.translatable("command.hammer.heal.self"), true);
						
						return Command.SINGLE_SUCCESS;
					})
			);
			
			dispatcher.register(
					CommandManager.literal("satiate").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var source = context.getSource();
								
								var players = EntityArgumentType.getPlayers(context, "players");
								
								for (var player : players) {
									player.getHungerManager().setFoodLevel(20);
									player.getHungerManager().setSaturationLevel(20.0F);
									
									source.sendFeedback(Text.translatable("command.hammer.satiate.other", player.getDisplayName()), true);
								}
								
								return Command.SINGLE_SUCCESS;
							})
					).executes(context -> {
						var source = context.getSource();
						var player = source.getPlayer();
						
						player.getHungerManager().setFoodLevel(20);
						player.getHungerManager().setSaturationLevel(20.0F);
						
						source.sendFeedback(Text.translatable("command.hammer.satiate.self"), true);
						
						return Command.SINGLE_SUCCESS;
					})
			);
			
			dispatcher.register(
					CommandManager.literal("fly_speed").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("speed", IntegerArgumentType.integer(1, 200)).then(
									CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
										var source = context.getSource();
										
										var speed = IntegerArgumentType.getInteger(context, "speed");
										var players = EntityArgumentType.getPlayers(context, "players");
										
										var buf = PacketByteBufs.create();
										buf.writeInt(speed);
										
										for (var player : players) {
											player.getAbilities().setFlySpeed(speed / 20.0F);
											
											ServerPlayNetworking.send(player, HUNetworking.FLY_SPEED_UPDATE, PacketByteBufs.duplicate(buf));
											
											source.sendFeedback(Text.translatable("command.hammer.fly_speed.other", player.getDisplayName(), speed), true);
										}
										
										return Command.SINGLE_SUCCESS;
									})
							).executes(context -> {
								var source = context.getSource();
								var player = source.getPlayer();
								
								var speed = IntegerArgumentType.getInteger(context, "speed");
								var players = EntityArgumentType.getPlayers(context, "players");
								
								var buf = PacketByteBufs.create();
								buf.writeInt(speed);
								
								player.getAbilities().setFlySpeed(speed / 20.0F);
								
								ServerPlayNetworking.send(player, HUNetworking.FLY_SPEED_UPDATE, PacketByteBufs.duplicate(buf));
								
								source.sendFeedback(Text.translatable("command.hammer.fly_speed.self", speed), true);
								
								return Command.SINGLE_SUCCESS;
							})
					)
			);
			
			dispatcher.register(
					CommandManager.literal("walk_speed").requires(source -> source.hasPermissionLevel(4)).then(
							CommandManager.argument("speed", IntegerArgumentType.integer(1, 200)).then(
									CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
										var source = context.getSource();
										
										var speed = IntegerArgumentType.getInteger(context, "speed");
										var players = EntityArgumentType.getPlayers(context, "players");
										
										var buf = PacketByteBufs.create();
										buf.writeInt(speed);
										
										for (var player : players) {
											player.getAbilities().setWalkSpeed(speed / 20.0F);
											
											ServerPlayNetworking.send(player, HUNetworking.WALK_SPEED_UPDATE, PacketByteBufs.duplicate(buf));
											
											source.sendFeedback(Text.translatable("command.hammer.walk_speed.other", player.getDisplayName(), speed), true);
										}
										
										return Command.SINGLE_SUCCESS;
									})
							).executes(context -> {
								var source = context.getSource();
								var player = source.getPlayer();
								
								var speed = IntegerArgumentType.getInteger(context, "speed");
								var players = EntityArgumentType.getPlayers(context, "players");
								
								var buf = PacketByteBufs.create();
								buf.writeInt(speed);
								
								player.getAbilities().setWalkSpeed(speed / 20.0F);
								
								ServerPlayNetworking.send(player, HUNetworking.WALK_SPEED_UPDATE, PacketByteBufs.duplicate(buf));
								
								source.sendFeedback(Text.translatable("command.hammer.walk_speed.self", speed), true);
								
								return Command.SINGLE_SUCCESS;
							})
					)
			);
		});
	}
}
