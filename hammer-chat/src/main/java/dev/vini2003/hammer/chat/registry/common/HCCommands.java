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

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.vini2003.hammer.chat.api.common.channel.Channel;
import dev.vini2003.hammer.chat.api.common.manager.ChannelManager;
import dev.vini2003.hammer.chat.api.common.util.ChannelUtil;
import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import dev.vini2003.hammer.permission.api.common.manager.RoleManager;
import dev.vini2003.hammer.permission.api.common.util.RoleUtil;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import net.minecraft.util.Formatting;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.minecraft.command.argument.EntityArgumentType.*;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class HCCommands {
	private static CompletableFuture<Suggestions> suggestChannels(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
		for (var channel : ChannelManager.channels()) {
			builder.suggest(channel.getName());
		}
		
		return builder.buildFuture();
	}
	
	private static CompletableFuture<Suggestions> suggestChannelsIn(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		for (var channel : ChannelManager.channels()) {
			if (channel.isIn(player)) {
				builder.suggest(channel.getName());
			}
		}
		
		return builder.buildFuture();
	}
	
	private static boolean requiresOp(ServerCommandSource source) {
		return source.hasPermissionLevel(4);
	}
	
	// Lists channels.
	private static int executeChannelList(CommandContext<ServerCommandSource> context, ServerPlayerEntity player) {
		var source = context.getSource();
		
		var channels = ChannelManager.channels();
		
		var text = "";
		
		for (var channel : channels) {
			if (channel.isIn(player)) {
				text += "§a⚪§r" + channel.getName() + "§r, ";
			} else if (channel.getRole().isIn(player)) {
				text += "§e⚪§r" + channel.getName() + "§r, ";
			}
		}
		
		if (text.length() > 0) {
			text = text.substring(0, text.length() - 2);
		}
		
		if (player == source.getPlayer()) {
			source.sendFeedback(Text.translatable("text.hammer.channel.list.self", text), false);
		} else {
			source.sendFeedback(Text.translatable("text.hammer.channel.list.other", text), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Lists channels for the command sender.
	private static int executeChannelListPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		try {
			var target = getPlayer(context, "player");
			return executeChannelList(context, target);
		} catch (CommandSyntaxException exception) {
			return executeChannelList(context, player);
		}
	}
	
	// Leaves a channel.
	private static int executeChannelLeave(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var channelName = getString(context, "channel_name");
		var channel = ChannelManager.getChannelByName(channelName);
		
		for (var player : players) {
			if (channel.isIn(player)) {
				channel.removeFrom(player);
				
				ChannelUtil.setSelected(player, ChannelUtil.getPreviousSelected(player));
			}
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Leaves a channel for the command sender.
	private static int executeChannelLeavePlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		return executeChannelLeave(context, ImmutableList.of(player));
	}
	
	// Leaves a channel for the specified players.
	private static int executeChannelLeavePlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeChannelLeave(context, getPlayers(context, "players"));
	}
	
	// Joins a channel.
	private static int executeChannelJoin(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var channelName = getString(context, "channel_name");
		var channel = ChannelManager.getChannelByName(channelName);
		
		for (var player : players) {
			if (!channel.isIn(player) && (channel.getRole() == null || channel.getRole().isIn(player))) {
				channel.addTo(player);
				
				ChannelUtil.setSelected(player, channel);
			}
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Joins a channel for the command sender.
	private static int executeChannelJoinPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		return executeChannelJoin(context, ImmutableList.of(player));
	}
	
	// Joins a channel for the specified players.
	private static int executeChannelJoinPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeChannelJoin(context, getPlayers(context, "players"));
	}
	
	// Creates a channel.
	private static int executeChannelCreate(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var channelName = getString(context, "channel_name");
		var channel = new Channel(channelName);
		
		ChannelManager.add(channel);
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Deletes a channel.
	private static int executeChannelDelete(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var channelName = getString(context, "channel_name");
		var channel = ChannelManager.getChannelByName(channelName);
		
		ChannelManager.remove(channel);
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Sets the selected channel.
	private static int executeChannelSelect(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var source = context.getSource();

		var channelName = getString(context, "channel_name");
		var channel = ChannelManager.getChannelByName(channelName);
		
		for (var otherPlayer : players) {
			if (channel.isIn(otherPlayer)) {
				if (requiresOp(otherPlayer.getCommandSource())) {
					channel.addTo(otherPlayer);
				}
			}
			
			ChannelUtil.setSelected(otherPlayer, channel);
			
			source.sendFeedback(Text.translatable("text.hammer.channel.select.other", Text.literal("#" + channel.getName()).formatted(Formatting.DARK_GRAY), otherPlayer.getDisplayName()), false);
			otherPlayer.getCommandSource().sendFeedback(Text.translatable("text.hammer.channel.select.self", Text.literal("#" + channel.getName()).formatted(Formatting.DARK_GRAY), otherPlayer.getDisplayName()), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Sets the selected channel for the command sender.
	private static int executeChannelSelectPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		return executeChannelSelect(context, ImmutableList.of(player));
	}
	
	// Sets the selected channel for the specified players.
	private static int executeChannelSelectPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeChannelSelect(context, getPlayers(context, "players"));
	}
	
	// Enables or disables the global chat.
	private static int executeShowGlobalChat(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var server = source.getServer();
		
		var playerManager = server.getPlayerManager();
		var players = playerManager.getPlayerList();
		
		boolean state;
		
		try {
			state = BoolArgumentType.getBool(context, "state");
		} catch (Exception e) {
			state = !ChatUtil.shouldShowGlobalChat(players.iterator().next());
		}
		
		for (var player : players) {
			ChatUtil.setShowGlobalChat(player, state);
		}
		
		source.sendFeedback(Text.translatable("command.hammer.show_global_chat", state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case")), true);
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Enables or disables the chat.
	private static int executeShowChat(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		boolean state;
		
		try {
			state = BoolArgumentType.getBool(context, "state");
		} catch (Exception e) {
			state = !ChatUtil.shouldShowChat(players.iterator().next());
		}
		
		var source = context.getSource();

		for (var otherPlayer : players) {
			ChatUtil.setShowChat(otherPlayer, state);
			
			source.sendFeedback(Text.translatable("command.hammer.show_chat.other", state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), true);
			otherPlayer.getCommandSource().sendFeedback(Text.translatable("command.hammer.show_chat.self", state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Enables or disables the chat for the command sender.
	private static int executeShowChatPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		return executeShowChat(context, ImmutableList.of(player));
	}
	
	// Enables or disables the chat for the specified players.
	private static int executeShowChatPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeShowChat(context, getPlayers(context, "players"));
	}
	
	// Enables or disables the warnings.
	private static int executeShowWarnings(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		boolean state;
		
		try {
			state = BoolArgumentType.getBool(context, "state");
		} catch (Exception e) {
			state = !ChatUtil.shouldShowWarnings(players.iterator().next());
		}
		
		var source = context.getSource();

		for (var otherPlayer : players) {
			ChatUtil.setShowWarnings(otherPlayer, state);
			
			source.sendFeedback(Text.translatable("command.hammer.show_warnings.other", state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), true);
			otherPlayer.getCommandSource().sendFeedback(Text.translatable("command.hammer.show_warnings.self", state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Enables or disables the warnings for the command sender.
	private static int executeShowWarningsPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		return executeShowWarnings(context, ImmutableList.of(player));
	}
	
	// Enables or disables the warnings for the specified players.
	private static int executeShowWarningsPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeShowWarnings(context, getPlayers(context, "players"));
	}
	
	// Enables or disables direct messages.
	private static int executeShowDirectMessages(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		boolean state;
		
		try {
			state = BoolArgumentType.getBool(context, "state");
		} catch (Exception e) {
			state = !ChatUtil.shouldShowDirectMessages(players.iterator().next());
		}
		
		var source = context.getSource();
		
		for (var otherPlayer : players) {
			ChatUtil.setShowDirectMessages(otherPlayer, state);
			
			source.sendFeedback(Text.translatable("command.hammer.show_direct_messages.other", state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), true);
			otherPlayer.getCommandSource().sendFeedback(Text.translatable("command.hammer.show_direct_messages.self", state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Enables or disables direct messages for the command sender.
	private static int executeShowDirectMessagesPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		return executeShowDirectMessages(context, ImmutableList.of(player));
	}
	
	// Enables or disables direct messages for the specified players.
	private static int executeShowDirectMessagesPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeShowDirectMessages(context, getPlayers(context, "players"));
	}
	
	// Enables or disables command feedback.
	private static int executeShowCommandFeedback(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		boolean state;
		
		try {
			state = BoolArgumentType.getBool(context, "state");
		} catch (Exception e) {
			state = !ChatUtil.shouldShowCommandFeedback(players.iterator().next());
		}
		
		var source = context.getSource();

		for (var otherPlayer : players) {
			ChatUtil.setShowCommandFeedback(otherPlayer, state);

			source.sendFeedback(Text.translatable("command.hammer.show_command_feedback.other", state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), true);
			otherPlayer.getCommandSource().sendFeedback(Text.translatable("command.hammer.show_command_feedback.self", state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Enables or disables command feedback for the command sender.
	private static int executeShowCommandFeedbackPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		return executeShowCommandFeedback(context, ImmutableList.of(player));
	}
	
	// Enables or disables command feedback for the specified players.
	private static int executeShowCommandFeedbackPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeShowCommandFeedback(context, getPlayers(context, "players"));
	}
	
	// Enables or disables fast chat fade.
	private static int executeFastChatFade(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		boolean state;
		
		try {
			state = BoolArgumentType.getBool(context, "state");
		} catch (Exception e) {
			state = !ChatUtil.hasFastChatFade(players.iterator().next());
		}
		
		var source = context.getSource();

		for (var otherPlayer : players) {
			ChatUtil.setFastChatFade(otherPlayer, state);
			
			source.sendFeedback(Text.translatable("command.hammer.fast_chat_fade.other", state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), true);
			otherPlayer.getCommandSource().sendFeedback(Text.translatable("command.hammer.fast_chat_fade.self", state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Enables or disables fast chat fade for the command sender.
	private static int executeFastChatFadePlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		return executeFastChatFade(context, ImmutableList.of(player));
	}
	
	// Enables or disables fast chat fade for the specified players.
	private static int executeFastChatFadePlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeFastChatFade(context, getPlayers(context, "players"));
	}
	
	// Mutes players.
	private static int executeMute(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var source = context.getSource();

		for (var otherPlayer : players) {
			ChatUtil.setMuted(otherPlayer, true);
			
			source.sendFeedback(Text.translatable("command.hammer.mute.other", otherPlayer.getDisplayName()), true);
			otherPlayer.getCommandSource().sendFeedback(Text.translatable("command.hammer.mute.self", otherPlayer.getDisplayName()), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Mutes the specified players.
	private static int executeMutePlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeMute(context, getPlayers(context, "players"));
	}
	
	// Unmutes players.
	private static int executeUnmute(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var source = context.getSource();

		for (var otherPlayer : players) {
			ChatUtil.setMuted(otherPlayer, false);
			
			source.sendFeedback(Text.translatable("command.hammer.unmute.other", otherPlayer.getDisplayName()), true);
			otherPlayer.getCommandSource().sendFeedback(Text.translatable("command.hammer.unmute.self", otherPlayer.getDisplayName()), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Unmutes the specified players.
	private static int executeUnmutePlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeUnmute(context, getPlayers(context, "players"));
	}
	
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, dedicated) -> {
			var channelNode =
					literal("channel")
							.then(
									argument("channel_name", word())
											.suggests(HCCommands::suggestChannels)
											.executes(HCCommands::executeChannelJoinPlayer)
							);
			
			var chNode =
					literal("ch")
							.then(
									argument("channel_name", word())
											.suggests(HCCommands::suggestChannels)
											.executes(HCCommands::executeChannelJoinPlayer)
							);
			
			var channelListNode =
					literal("list")
							.then(
									argument("player", player())
											.executes(HCCommands::executeChannelListPlayer)
							)
							.executes(HCCommands::executeChannelListPlayer);
			
			var channelJoinNode =
					literal("join")
							.requires(HCCommands::requiresOp)
							.then(
									argument("channel_name", word())
											.suggests(HCCommands::suggestChannels)
											.then(
													argument("players", players())
															.executes(HCCommands::executeChannelJoinPlayers)
											).executes(HCCommands::executeChannelJoinPlayer)
							);
			
			var channelLeaveNode =
					literal("leave")
							.requires(HCCommands::requiresOp)
							.then(
									argument("channel_name", word())
											.suggests(HCCommands::suggestChannelsIn)
											.then(
													argument("players", players())
															.executes(HCCommands::executeChannelLeavePlayers)
											).executes(HCCommands::executeChannelLeavePlayer)
							);
			
			var channelCreateNode =
					literal("create")
							.requires(HCCommands::requiresOp)
							.then(
									argument("channel_name", word())
											.executes(HCCommands::executeChannelCreate)
							);
			
			var channelDeleteNode =
					literal("delete")
							.requires(HCCommands::requiresOp)
							.then(
									argument("channel_name", word())
											.executes(HCCommands::executeChannelDelete)
							);
			
			var channelSelectNode =
					literal("select").then(
							argument("channel_name", word())
									.suggests(HCCommands::suggestChannelsIn)
									.then(
											argument("players", players())
												.requires(HCCommands::requiresOp)
												.executes(HCCommands::executeChannelSelectPlayers)
									).executes(HCCommands::executeChannelSelectPlayer)
			);
			
			var showGlobalChatNode =
					literal("global_chat")
							.requires(HCCommands::requiresOp)
							.then(
									argument("state", bool())
											.executes(HCCommands::executeShowGlobalChat)
							);
			
			var showChatNode =
					literal("chat")
							.then(
									argument("state", bool())
											.then(
													argument("players", players())
															.executes(HCCommands::executeShowChatPlayers)
											)
											.executes(HCCommands::executeShowChatPlayer)
							);
			
			var showWarningsNode =
					literal("warnings")
							.then(
									argument("state", bool())
											.then(
													argument("players", players())
															.executes(HCCommands::executeShowWarningsPlayers)
											)
											.executes(HCCommands::executeShowWarningsPlayer)
							);
			
			var showCommandFeedbackNode =
					literal("command_feedback")
							.then(
									argument("state", bool())
											.then(
													argument("players", players())
															.executes(HCCommands::executeShowCommandFeedbackPlayers)
											)
											.executes(HCCommands::executeShowCommandFeedbackPlayer)
							);
			
			var showDirectMessagesNode =
					literal("direct_messages")
							.then(
									argument("state", bool())
											.then(
													argument("players", players())
															.executes(HCCommands::executeShowDirectMessagesPlayers)
											)
											.executes(HCCommands::executeShowDirectMessagesPlayer)
							);
			
			var fastChatFadeNode =
					literal("fast_chat_fade")
							.requires(HCCommands::requiresOp)
							.then(
									argument("state", bool())
											.then(
													argument("players", players())
															.executes(HCCommands::executeFastChatFadePlayers)
											)
											.executes(HCCommands::executeFastChatFadePlayer)
							);
			
			var muteNode =
					literal("mute")
							.requires(HCCommands::requiresOp)
							.then(
									argument("players", players())
											.executes(HCCommands::executeMutePlayers)
							);
			
			var unmuteNode =
					literal("unmute")
							.requires(HCCommands::requiresOp)
							.then(
									argument("players", players())
											.executes(HCCommands::executeUnmutePlayers)
							);
			
			chNode.then(channelListNode);
			chNode.then(channelJoinNode);
			chNode.then(channelLeaveNode);
			chNode.then(channelCreateNode);
			chNode.then(channelDeleteNode);
			chNode.then(channelSelectNode);
			
			channelNode.then(channelListNode);
			channelNode.then(channelJoinNode);
			channelNode.then(channelLeaveNode);
			channelNode.then(channelCreateNode);
			channelNode.then(channelDeleteNode);
			channelNode.then(channelSelectNode);
			
			dispatcher.register(showGlobalChatNode);
			dispatcher.register(showChatNode);
			dispatcher.register(showWarningsNode);
			dispatcher.register(showCommandFeedbackNode);
			dispatcher.register(showDirectMessagesNode);
			
			dispatcher.register(fastChatFadeNode);

			dispatcher.register(chNode);
			
			dispatcher.register(channelNode);
			
			dispatcher.register(muteNode);
			dispatcher.register(unmuteNode);
		});
		
	}
}
