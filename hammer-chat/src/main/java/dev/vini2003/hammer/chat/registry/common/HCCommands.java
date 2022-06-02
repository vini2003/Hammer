package dev.vini2003.hammer.chat.registry.common;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.vini2003.hammer.chat.api.common.channel.Channel;
import dev.vini2003.hammer.chat.api.common.manager.ChannelManager;
import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.TranslatableText;

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
				}).executes(context -> {
					var channelName = StringArgumentType.getString(context, "channel_name");
					var channel = ChannelManager.getChannelByName(channelName);
					
					var source = context.getSource();
					var player = source.getPlayer();
					
					if (!channel.isIn(player)) {
						channel.addTo(player);
					}
					
					return Command.SINGLE_SUCCESS;
				})
			).then(
					CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
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
					}).executes(context -> {
						var channelName = StringArgumentType.getString(context, "channel_name");
						var channel = ChannelManager.getChannelByName(channelName);
						
						var source = context.getSource();
						var player = source.getPlayer();
						
						if (channel.isIn(player)) {
							channel.removeFrom(player);
						}
						
						return Command.SINGLE_SUCCESS;
					})
			).then(
					CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
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
			
			channelNode.then(channelJoinNode);
			channelNode.then(channelLeaveNode);
			channelNode.then(channelCreateNode);
			channelNode.then(channelDeleteNode);
			
			dispatcher.register(channelNode);
		});
		
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(
					CommandManager.literal("show_global_chat").requires(source -> {
						return source.hasPermissionLevel(4);
					}).then(
							CommandManager.argument("players", EntityArgumentType.players()).then(
									CommandManager.argument("state", BoolArgumentType.bool()).executes(context -> {
										var players = EntityArgumentType.getPlayers(context, "players");
										var state = BoolArgumentType.getBool(context, "state");
										
										var source = context.getSource();

										for (var player : players) {
											ChatUtil.setShowGlobalChat(player, state);
											
											source.sendFeedback(new TranslatableText("command.hammer.show_global_chat.other", state ? "enabled" : "disabled", player.getDisplayName()), true);
										}
										
										return Command.SINGLE_SUCCESS;
									})
							)
					).then(
							CommandManager.argument("state", BoolArgumentType.bool()).executes(context -> {
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
					CommandManager.literal("show_chat").requires(source -> {
						return source.hasPermissionLevel(4);
					}).then(
							CommandManager.argument("players", EntityArgumentType.players()).then(
									CommandManager.argument("state", BoolArgumentType.bool()).executes(context -> {
										var players = EntityArgumentType.getPlayers(context, "players");
										var state = BoolArgumentType.getBool(context, "state");
										
										var source = context.getSource();
										
										for (var player : players) {
											ChatUtil.setShowChat(player, state);
											
											source.sendFeedback(new TranslatableText("command.hammer.show_chat.other", state ? "enabled" : "disabled", player.getDisplayName()), true);
										}
										
										return Command.SINGLE_SUCCESS;
									})
							)
					).then(
							CommandManager.argument("state", BoolArgumentType.bool()).executes(context -> {
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
					CommandManager.literal("show_command_feedback").requires(source -> {
						return source.hasPermissionLevel(4);
					}).then(
							CommandManager.argument("players", EntityArgumentType.players()).then(
									CommandManager.argument("state", BoolArgumentType.bool()).executes(context -> {
										var players = EntityArgumentType.getPlayers(context, "players");
										var state = BoolArgumentType.getBool(context, "state");
										
										var source = context.getSource();
										
										for (var player : players) {
											ChatUtil.setShowCommandFeedback(player, state);
											
											source.sendFeedback(new TranslatableText("command.hammer.show_command_feedback.other", state ? "enabled" : "disabled", player.getDisplayName()), true);
										}
										
										return Command.SINGLE_SUCCESS;
									})
							)
					).then(
							CommandManager.argument("state", BoolArgumentType.bool()).executes(context -> {
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
					CommandManager.literal("show_warnings").requires(source -> {
						return source.hasPermissionLevel(4);
					}).then(
							CommandManager.argument("players", EntityArgumentType.players()).then(
									CommandManager.argument("state", BoolArgumentType.bool()).executes(context -> {
										var players = EntityArgumentType.getPlayers(context, "players");
										var state = BoolArgumentType.getBool(context, "state");
										
										var source = context.getSource();

										for (var player : players) {
											ChatUtil.setShowWarnings(player, state);
											
											source.sendFeedback(new TranslatableText("command.hammer.show_warnings.other", state ? "enabled" : "disabled", player.getDisplayName()), true);
										}
										
										return Command.SINGLE_SUCCESS;
									})
							)
					).then(
							CommandManager.argument("state", BoolArgumentType.bool()).executes(context -> {
								var state = BoolArgumentType.getBool(context, "state");
								
								var source = context.getSource();
								
								ChatUtil.setShowWarnings(context.getSource().getPlayer(), state);
								
								source.sendFeedback(new TranslatableText("command.hammer.show_warnings.self", state ? "enabled" : "disabled"), true);
								
								return Command.SINGLE_SUCCESS;
							})
					)
			);
		});
		
		CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
			dispatcher.register(
					CommandManager.literal("mute").then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var players = EntityArgumentType.getPlayers(context, "players");
								
								var source = context.getSource();
								
								for (var player : players) {
									ChatUtil.setMuted(player, true);
									
									source.sendFeedback(new TranslatableText("command.hammer.mute.other", player.getDisplayName()), true);
								}
								
								return Command.SINGLE_SUCCESS;
							})
					).executes(context -> {
						var source = context.getSource();
						var player = source.getPlayer();
						
						ChatUtil.setMuted(player, true);
						
						source.sendFeedback(new TranslatableText("command.hammer.mute.self"), true);
						
						return Command.SINGLE_SUCCESS;
					})
			);
		}));
		
		CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
			dispatcher.register(
					CommandManager.literal("unmute").then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var players = EntityArgumentType.getPlayers(context, "players");
								
								var source = context.getSource();
								
								for (var player : players) {
									ChatUtil.setMuted(player, false);
									
									source.sendFeedback(new TranslatableText("command.hammer.unmute.other", player.getDisplayName()), true);
								}
								
								return Command.SINGLE_SUCCESS;
							})
					).executes(context -> {
						var source = context.getSource();
						var player = source.getPlayer();
						
						ChatUtil.setMuted(player, false);
						
						source.sendFeedback(new TranslatableText("command.hammer.unmute.self"), true);
						
						return Command.SINGLE_SUCCESS;
					})
			);
		}));
	}
}
