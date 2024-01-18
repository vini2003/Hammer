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
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.vini2003.hammer.chat.api.common.channel.Channel;
import dev.vini2003.hammer.chat.api.common.manager.ChannelManager;
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

// TODO: Add feedback for joining/leaving channels.
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
		
		if (player == null) {
			return Suggestions.empty();
		}
		
		for (var channel : ChannelManager.channels()) {
			if (player.hammer$isInChannel(channel)) {
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
		
		var ref = new Object() {
			String text = "";
		};
		
		for (var channel : channels) {
			if (player.hammer$isInChannel(channel)) {
				ref.text += "§a⚪§r" + channel.getName() + "§r, ";
			} else if (player.hammer$hasRole(channel.getRole())) {
				ref.text += "§e⚪§r" + channel.getName() + "§r, ";
			}
		}
		
		if (ref.text.length() > 0) {
			ref.text = ref.text.substring(0, ref.text.length() - 2);
		}
		
		if (player == source.getPlayer()) {
			source.sendFeedback(Text.translatable("text.hammer.channel.list.self", ref.text), false);
		} else {
			source.sendFeedback(Text.translatable("text.hammer.channel.list.other", ref.text), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Lists channels for the command sender.
	private static int executeChannelListPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		try {
			var target = getPlayer(context, "player");
			return executeChannelList(context, target);
		} catch (CommandSyntaxException exception) {
			return executeChannelList(context, player);
		}
	}
	
	// Leaves a channel.
	private static int executeChannelLeave(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var channelName = getString(context, "channel");
		var channel = ChannelManager.getChannelByName(channelName);
		
		for (var player : players) {
			if (player.hammer$isInChannel(channel)) {
				player.hammer$leaveChannel(channel);
				
				player.hammer$setSelectedChannel(player.hammer$getPreviousSelectedChannel());
			}
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Leaves a channel for the command sender.
	private static int executeChannelLeavePlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeChannelLeave(context, ImmutableList.of(player));
	}
	
	// Leaves a channel for the specified players.
	private static int executeChannelLeavePlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeChannelLeave(context, getPlayers(context, "players"));
	}
	
	// Joins a channel.
	private static int executeChannelJoin(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var channelName = getString(context, "channel");
		var channel = ChannelManager.getChannelByName(channelName);
		
		for (var player : players) {
			if (!player.hammer$isInChannel(channel) && (channel.getRole() == null || player.hammer$hasRole(channel.getRole()))) {
				player.hammer$joinChannel(channel);
				
				player.hammer$setSelectedChannel(channel);
			}
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Joins a channel for the command sender.
	private static int executeChannelJoinPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeChannelJoin(context, ImmutableList.of(player));
	}
	
	// Joins a channel for the specified players.
	private static int executeChannelJoinPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeChannelJoin(context, getPlayers(context, "players"));
	}
	
	// Creates a channel.
	private static int executeChannelCreate(CommandContext<ServerCommandSource> context) {
		var channelName = getString(context, "channel");
		var channel = new Channel(channelName);
		
		ChannelManager.add(channel);
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Deletes a channel.
	private static int executeChannelDelete(CommandContext<ServerCommandSource> context) {
		var channelName = getString(context, "channel");
		var channel = ChannelManager.getChannelByName(channelName);
		
		ChannelManager.remove(channel);
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Sets the selected channel.
	private static int executeChannelSelect(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var source = context.getSource();

		var channelName = getString(context, "channel");
		var channel = ChannelManager.getChannelByName(channelName);
		
		for (var otherPlayer : players) {
			if (otherPlayer.hammer$isInChannel(channel)) {
				if (requiresOp(otherPlayer.getCommandSource())) {
					otherPlayer.hammer$joinChannel(channel);
				}
			}
			
			otherPlayer.hammer$setSelectedChannel(channel);
			
			source.sendFeedback(Text.translatable("text.hammer.channel.select.other", Text.literal("#" + channel.getName()).formatted(Formatting.DARK_GRAY), otherPlayer.getDisplayName()), false);
			otherPlayer.getCommandSource().sendFeedback(Text.translatable("text.hammer.channel.select.self", Text.literal("#" + channel.getName()).formatted(Formatting.DARK_GRAY), otherPlayer.getDisplayName()), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Sets the selected channel for the command sender.
	private static int executeChannelSelectPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeChannelSelect(context, ImmutableList.of(player));
	}
	
	// Sets the selected channel for the specified players.
	private static int executeChannelSelectPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeChannelSelect(context, getPlayers(context, "players"));
	}
	
	// Enables or disables the chat.
	private static int executeShowChat(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var ref = new Object() {
			boolean state;
		};
		
		try {
			ref.state = BoolArgumentType.getBool(context, "state");
		} catch (Exception e) {
			ref.state = !players.iterator().next().hammer$shouldShowChat();
		}
		
		var source = context.getSource();

		for (var otherPlayer : players) {
			otherPlayer.hammer$setShowChat(ref.state);
			
			source.sendFeedback(Text.translatable("command.hammer.show_chat.other", ref.state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), true);
			otherPlayer.getCommandSource().sendFeedback(Text.translatable("command.hammer.show_chat.self", ref.state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Enables or disables the chat for the command sender.
	private static int executeShowChatPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeShowChat(context, ImmutableList.of(player));
	}
	
	// Enables or disables the chat for the specified players.
	private static int executeShowChatPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeShowChat(context, getPlayers(context, "players"));
	}
	
	// Enables or disables the warnings.
	private static int executeShowWarnings(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var ref = new Object() {
			boolean state;
		};
		
		try {
			ref.state = BoolArgumentType.getBool(context, "state");
		} catch (Exception e) {
			ref.state = !players.iterator().next().hammer$shouldShowWarnings();
		}
		
		var source = context.getSource();

		for (var otherPlayer : players) {
			otherPlayer.hammer$setShowWarnings(ref.state);
			
			source.sendFeedback(Text.translatable("command.hammer.show_warnings.other", ref.state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), true);
			otherPlayer.getCommandSource().sendFeedback(Text.translatable("command.hammer.show_warnings.self", ref.state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Enables or disables the warnings for the command sender.
	private static int executeShowWarningsPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeShowWarnings(context, ImmutableList.of(player));
	}
	
	// Enables or disables the warnings for the specified players.
	private static int executeShowWarningsPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeShowWarnings(context, getPlayers(context, "players"));
	}
	
	// Enables or disables direct messages.
	private static int executeShowDirectMessages(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var ref = new Object() {
			boolean state;
		};
		
		try {
			ref.state = BoolArgumentType.getBool(context, "state");
		} catch (Exception e) {
			ref.state = !players.iterator().next().hammer$shouldShowDirectMessages();
		}
		
		var source = context.getSource();
		
		for (var otherPlayer : players) {
			otherPlayer.hammer$setShowDirectMessages(ref.state);
			
			source.sendFeedback(Text.translatable("command.hammer.show_direct_messages.other", ref.state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), true);
			otherPlayer.getCommandSource().sendFeedback(Text.translatable("command.hammer.show_direct_messages.self", ref.state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Enables or disables direct messages for the command sender.
	private static int executeShowDirectMessagesPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeShowDirectMessages(context, ImmutableList.of(player));
	}
	
	// Enables or disables direct messages for the specified players.
	private static int executeShowDirectMessagesPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeShowDirectMessages(context, getPlayers(context, "players"));
	}
	
	// Enables or disables command feedback.
	private static int executeShowCommandFeedback(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var ref = new Object() {
			boolean state;
		};
		
		try {
			ref.state = BoolArgumentType.getBool(context, "state");
		} catch (Exception e) {
			ref.state = !players.iterator().next().hammer$shouldShowCommandFeedback();
		}
		
		var source = context.getSource();

		for (var otherPlayer : players) {
			otherPlayer.hammer$setShowCommandFeedback(ref.state);

			source.sendFeedback(Text.translatable("command.hammer.show_command_feedback.other", ref.state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), true);
			otherPlayer.getCommandSource().sendFeedback(Text.translatable("command.hammer.show_command_feedback.self", ref.state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Enables or disables command feedback for the command sender.
	private static int executeShowCommandFeedbackPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeShowCommandFeedback(context, ImmutableList.of(player));
	}
	
	// Enables or disables command feedback for the specified players.
	private static int executeShowCommandFeedbackPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeShowCommandFeedback(context, getPlayers(context, "players"));
	}
	
	// Enables or disables fast chat fade.
	private static int executeFastChatFade(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var ref = new Object() {
			boolean state;
		};
		
		try {
			ref.state = BoolArgumentType.getBool(context, "state");
		} catch (Exception e) {
			ref.state = !players.iterator().next().hammer$hasFastChatFade();
		}
		
		var source = context.getSource();

		for (var otherPlayer : players) {
			otherPlayer.hammer$setFastChatFade(ref.state);
			
			source.sendFeedback(Text.translatable("command.hammer.fast_chat_fade.other", ref.state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), true);
			otherPlayer.getCommandSource().sendFeedback(Text.translatable("command.hammer.fast_chat_fade.self", ref.state ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case"), otherPlayer.getDisplayName()), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Enables or disables fast chat fade for the command sender.
	private static int executeFastChatFadePlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
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
			otherPlayer.hammer$setMuted(true);
			
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
			otherPlayer.hammer$setMuted(false);
			
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
		CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
			var channelNode =
					literal("channel")
							.then(
									argument("channel", word())
											.suggests(HCCommands::suggestChannels)
											.executes(HCCommands::executeChannelJoinPlayer)
							);
			
			var chNode =
					literal("ch")
							.then(
									argument("channel", word())
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
									argument("channel", word())
											.suggests(HCCommands::suggestChannels)
											.then(
													argument("players", players())
															.executes(HCCommands::executeChannelJoinPlayers)
											)
											.executes(HCCommands::executeChannelJoinPlayer)
							);
			
			var channelLeaveNode =
					literal("leave")
							.requires(HCCommands::requiresOp)
							.then(
									argument("channel", word())
											.suggests(HCCommands::suggestChannelsIn)
											.then(
													argument("players", players())
															.executes(HCCommands::executeChannelLeavePlayers)
											)
											.executes(HCCommands::executeChannelLeavePlayer)
							);
			
			var channelCreateNode =
					literal("create")
							.requires(HCCommands::requiresOp)
							.then(
									argument("channel", word())
											.executes(HCCommands::executeChannelCreate)
							);
			
			var channelDeleteNode =
					literal("delete")
							.requires(HCCommands::requiresOp)
							.then(
									argument("channel", word())
											.executes(HCCommands::executeChannelDelete)
							);
			
			var channelSelectNode =
					literal("select")
							.then(
									argument("channel", word())
											.suggests(HCCommands::suggestChannelsIn)
											.then(
													argument("players", players())
														.requires(HCCommands::requiresOp)
														.executes(HCCommands::executeChannelSelectPlayers)
											).executes(HCCommands::executeChannelSelectPlayer)
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
