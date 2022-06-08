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

package dev.vini2003.hammer.chat.registry.common;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.vini2003.hammer.chat.api.common.channel.Channel;
import dev.vini2003.hammer.chat.api.common.manager.ChannelManager;
import dev.vini2003.hammer.chat.api.common.util.ChannelUtil;
import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

public class HCCommands {
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			var channelNode = CommandManager.literal("channel");
			
			var channelJoinNode = CommandManager.literal("join").requires(source -> {
				return source.hasPermissionLevel(4);
			}).then(
				CommandManager.argument("channel_name", StringArgumentType.word()).suggests((context, builder) -> {
					for (var channel : ChannelManager.channels()) {
						builder.suggest(channel.getName());
					}
				
					return builder.buildFuture();
				}).then(
						CommandManager.argument("players", EntityArgumentType.players()).requires(source -> {
							return source.hasPermissionLevel(4);
						}).executes(context -> {
							var channelName = StringArgumentType.getString(context, "channel_name");
							var channel = ChannelManager.getChannelByName(channelName);
							
							var players = EntityArgumentType.getPlayers(context, "players");
							
							for (var player : players) {
								if (!channel.isIn(player)) {
									channel.addTo(player);
								}
							}
							
							return Command.SINGLE_SUCCESS;
						})
				).executes(context -> {
					var channelName = StringArgumentType.getString(context, "channel_name");
					var channel = ChannelManager.getChannelByName(channelName);
					
					var source = context.getSource();
					var player = source.getPlayer();
					
					if (!channel.isIn(player)) {
						channel.addTo(player);
					}
					
					return Command.SINGLE_SUCCESS;
				})
			);
			
			var channelLeaveNode = CommandManager.literal("leave").requires(source -> {
				return source.hasPermissionLevel(4);
			}).then(
					CommandManager.argument("channel_name", StringArgumentType.word()).suggests((context, builder) -> {
						var source = context.getSource();
						var player = source.getPlayer();
						
						for (var channel : ChannelManager.channels()) {
							if (channel.isIn(player)) {
								builder.suggest(channel.getName());
							}
						}
						
						return builder.buildFuture();
					}).then(
							CommandManager.argument("players", EntityArgumentType.players()).requires(source -> {
								return source.hasPermissionLevel(4);
							}).executes(context -> {
								var channelName = StringArgumentType.getString(context, "channel_name");
								var channel = ChannelManager.getChannelByName(channelName);
								
								var players = EntityArgumentType.getPlayers(context, "players");
								
								for (var player : players) {
									if (channel.isIn(player)) {
										channel.removeFrom(player);
									}
								}
								
								return Command.SINGLE_SUCCESS;
							})
					).executes(context -> {
						var channelName = StringArgumentType.getString(context, "channel_name");
						var channel = ChannelManager.getChannelByName(channelName);
						
						var source = context.getSource();
						var player = source.getPlayer();
						
						if (channel.isIn(player)) {
							channel.removeFrom(player);
						}
						
						return Command.SINGLE_SUCCESS;
					})
			);
			
			var channelCreateNode = CommandManager.literal("create").requires(source -> {
				return source.hasPermissionLevel(4);
			}).then(
					CommandManager.argument("channel_name", StringArgumentType.word()).executes(context -> {
						var channelName = StringArgumentType.getString(context, "channel_name");
						var channel = new Channel(channelName);
						
						ChannelManager.add(channel);
						
						return Command.SINGLE_SUCCESS;
					})
			);
			
			var channelDeleteNode = CommandManager.literal("delete").requires(source -> {
				return source.hasPermissionLevel(4);
			}).then(
					CommandManager.argument("channel_name", StringArgumentType.word()).executes(context -> {
						var channelName = StringArgumentType.getString(context, "channel_name");
						var channel = ChannelManager.getChannelByName(channelName);
						
						ChannelManager.remove(channel);
						
						return Command.SINGLE_SUCCESS;
					})
			);
			
			var channelSelectNode = CommandManager.literal("select").then(
				CommandManager.argument("channel_name", StringArgumentType.word()).suggests((context, builder) -> {
					var source = context.getSource();
					var player = source.getPlayer();
					
					for (var channel : ChannelManager.channels()) {
						if (channel.isIn(player)) {
							builder.suggest(channel.getName());
						}
					}
					
					return builder.buildFuture();
				}).then(
						CommandManager.argument("players", EntityArgumentType.players()).requires(source -> {
							return source.hasPermissionLevel(4);
						}).executes(context -> {
							var source = context.getSource();

							var channelName = StringArgumentType.getString(context, "channel_name");
							var channel = ChannelManager.getChannelByName(channelName);
							
							var players = EntityArgumentType.getPlayers(context, "players");
							
							for (var player : players) {
								source.sendFeedback(new TranslatableText("text.hammer.channel.select.other", new LiteralText("#" + channel.getName()).formatted(Formatting.DARK_GRAY), player.getDisplayName()), false);
								
								ChannelUtil.setSelected(player, channel);
							}
							
							return Command.SINGLE_SUCCESS;
						})
				).executes(context -> {
					var source = context.getSource();
					var player = source.getPlayer();
					
					var channelName = StringArgumentType.getString(context, "channel_name");
					var channel = ChannelManager.getChannelByName(channelName);
					
					if (channel != null) {
						source.sendFeedback(new TranslatableText("text.hammer.channel.select.self", new LiteralText("#" + channel.getName()).formatted(Formatting.DARK_GRAY)), false);
						
						ChannelUtil.setSelected(player, channel);
					}
					
					return Command.SINGLE_SUCCESS;
				})
			);
			
			channelNode.then(channelJoinNode);
			channelNode.then(channelLeaveNode);
			channelNode.then(channelCreateNode);
			channelNode.then(channelDeleteNode);
			channelNode.then(channelSelectNode);
			
			dispatcher.register(channelNode);
		});
		
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(
					CommandManager.literal("show_global_chat").then(
							CommandManager.argument("state", BoolArgumentType.bool()).then(
									CommandManager.argument("players", EntityArgumentType.players()).requires(source -> {
										return source.hasPermissionLevel(4);
									}).executes(context -> {
										var players = EntityArgumentType.getPlayers(context, "players");
										var state = BoolArgumentType.getBool(context, "state");
										
										var source = context.getSource();
										
										for (var player : players) {
											ChatUtil.setShowGlobalChat(player, state);
											
											source.sendFeedback(new TranslatableText("command.hammer.show_global_chat.other", state ? "enabled" : "disabled", player.getDisplayName()), true);
										}
										
										return Command.SINGLE_SUCCESS;
									})
							).executes(context -> {
								var state = BoolArgumentType.getBool(context, "state");
								
								var source = context.getSource();
								
								ChatUtil.setShowGlobalChat(context.getSource().getPlayer(), state);
								
								source.sendFeedback(new TranslatableText("command.hammer.show_global_chat.self", state ? "enabled" : "disabled"), true);
								
								return Command.SINGLE_SUCCESS;
							})
					)
			);
		});
		
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(
					CommandManager.literal("show_chat").then(
							CommandManager.argument("state", BoolArgumentType.bool()).then(
									CommandManager.argument("players", EntityArgumentType.players()).requires(source -> {
										return source.hasPermissionLevel(4);
									}).executes(context -> {
										var players = EntityArgumentType.getPlayers(context, "players");
										var state = BoolArgumentType.getBool(context, "state");
										
										var source = context.getSource();
										
										for (var player : players) {
											ChatUtil.setShowChat(player, state);
											
											source.sendFeedback(new TranslatableText("command.hammer.show_chat.other", state ? "enabled" : "disabled", player.getDisplayName()), true);
										}
										
										return Command.SINGLE_SUCCESS;
									})
							).executes(context -> {
								var state = BoolArgumentType.getBool(context, "state");
								
								var source = context.getSource();
								
								ChatUtil.setShowChat(context.getSource().getPlayer(), state);
								
								source.sendFeedback(new TranslatableText("command.hammer.show_chat.self", state ? "enabled" : "disabled"), true);
								
								return Command.SINGLE_SUCCESS;
							})
					)
			);
		});
		
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(
					CommandManager.literal("show_command_feedback").then(
							CommandManager.argument("state", BoolArgumentType.bool()).then(
									CommandManager.argument("players", EntityArgumentType.players()).requires(source -> {
										return source.hasPermissionLevel(4);
									}).executes(context -> {
										var players = EntityArgumentType.getPlayers(context, "players");
										var state = BoolArgumentType.getBool(context, "state");
										
										var source = context.getSource();
										
										for (var player : players) {
											ChatUtil.setShowCommandFeedback(player, state);
											
											source.sendFeedback(new TranslatableText("command.hammer.show_command_feedback.other", state ? "enabled" : "disabled", player.getDisplayName()), true);
										}
										
										return Command.SINGLE_SUCCESS;
									})
							).executes(context -> {
								var state = BoolArgumentType.getBool(context, "state");
								
								var source = context.getSource();
								
								ChatUtil.setShowCommandFeedback(context.getSource().getPlayer(), state);
								
								source.sendFeedback(new TranslatableText("command.hammer.show_command_feedback.self", state ? "enabled" : "disabled"), true);
								
								return Command.SINGLE_SUCCESS;
							})
					)
			);
		});
		
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(
					CommandManager.literal("show_warnings").then(
							CommandManager.argument("state", BoolArgumentType.bool()).then(
									CommandManager.argument("players", EntityArgumentType.players()).requires(source -> {
										return source.hasPermissionLevel(4);
									}).executes(context -> {
										var players = EntityArgumentType.getPlayers(context, "players");
										var state = BoolArgumentType.getBool(context, "state");
										
										var source = context.getSource();
										
										for (var player : players) {
											ChatUtil.setShowWarnings(player, state);
											
											source.sendFeedback(new TranslatableText("command.hammer.show_warnings.other", state ? "enabled" : "disabled", player.getDisplayName()), true);
										}
										
										return Command.SINGLE_SUCCESS;
									})
							).executes(context -> {
								var state = BoolArgumentType.getBool(context, "state");
								
								var source = context.getSource();
								
								ChatUtil.setShowWarnings(context.getSource().getPlayer(), state);
								
								source.sendFeedback(new TranslatableText("command.hammer.show_warnings.self", state ? "enabled" : "disabled"), true);
								
								return Command.SINGLE_SUCCESS;
							})
					)
			);
		});
	}
}
