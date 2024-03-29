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

package dev.vini2003.hammer.registry.common;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.vini2003.hammer.core.registry.common.HCConfig;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;

import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static net.minecraft.command.argument.EntityArgumentType.getPlayers;
import static net.minecraft.command.argument.EntityArgumentType.players;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class HUCommands {
	private static boolean requiresOp(ServerCommandSource source) {
		return source.hasPermissionLevel(4);
	}
	
	// Enables or disables the End.
	private static int executeToggleEnd(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		
		HCConfig.ENABLE_END = !HCConfig.ENABLE_END;
		
		source.sendFeedback(() -> Text.translatable("command.hammer." + (HCConfig.ENABLE_END ? "enable" : "disable") + "_end"), true);
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Enables or disables the Nether.
	private static int executeToggleNether(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		
		HCConfig.ENABLE_NETHER = !HCConfig.ENABLE_NETHER;
		
		source.sendFeedback(() -> Text.translatable("command.hammer." + (HCConfig.ENABLE_NETHER ? "enable" : "disable") + "_nether"), true);
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Freezes players.
	private static int executeFreeze(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var source = context.getSource();

		for (var otherPlayer : players) {
			otherPlayer.hammer$setFrozen(true);
			
			source.sendFeedback(() -> Text.translatable("command.hammer.freeze.other", otherPlayer.getDisplayName()), true);
			otherPlayer.getCommandSource().sendFeedback(() -> Text.translatable("command.hammer.freeze.self"), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Freezes a player.
	private static int executeFreezePlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeFreeze(context, ImmutableList.of(player));
	}
	
	// Freezes the specified players.
	private static int executeFreezePlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeFreeze(context, getPlayers(context, "players"));
	}
	
	// Unfreezes players.
	private static int executeUnfreeze(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var source = context.getSource();

		for (var otherPlayer : players) {
			otherPlayer.hammer$setFrozen(false);
			
			source.sendFeedback(() -> Text.translatable("command.hammer.unfreeze.other", otherPlayer.getDisplayName()), true);
			otherPlayer.getCommandSource().sendFeedback(() -> Text.translatable("command.hammer.unfreeze.self"), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Unfreezes a player.
	private static int executeUnfreezePlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeUnfreeze(context, ImmutableList.of(player));
	}
	
	// Unfreezes the specified players.
	private static int executeUnfreezePlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeUnfreeze(context, getPlayers(context, "players"));
	}
	
	// Heals players.
	private static int executeHeal(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var source = context.getSource();
		
		
		for (var otherPlayer : players) {
			otherPlayer.setHealth(otherPlayer.getMaxHealth());
			
			source.sendFeedback(() -> Text.translatable("command.hammer.heal.other", otherPlayer.getDisplayName()), true);
			otherPlayer.getCommandSource().sendFeedback(() -> Text.translatable("command.hammer.heal.self", otherPlayer.getDisplayName()), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Heals a player.
	private static int executeHealPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeHeal(context, ImmutableList.of(player));
	}
	
	// Heals the specified players.
	private static int executeHealPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeHeal(context, getPlayers(context, "players"));
	}
	
	// Satiates players.
	private static int executeSatiate(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var source = context.getSource();
		
		for (var otherPlayer : players) {
			otherPlayer.getHungerManager().setFoodLevel(20);
			otherPlayer.getHungerManager().setSaturationLevel(20.0F);
			
			source.sendFeedback(() -> Text.translatable("command.hammer.satiate.other", otherPlayer.getDisplayName()), true);
			otherPlayer.getCommandSource().sendFeedback(() -> Text.translatable("command.hammer.satiate.self", otherPlayer.getDisplayName()), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Satiates a player.
	private static int executeSatiatePlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeSatiate(context, ImmutableList.of(player));
	}
	
	// Satiates the specified players.
	private static int executeSatiatePlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeSatiate(context, getPlayers(context, "players"));
	}
	
	// Toggles whether the specified players are allowed to move or not.
	private static int executeAllowMovement(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var source = context.getSource();
		
		var allowMovement = getBool(context, "allowMovement");
		
		for (var otherPlayer : players) {
			otherPlayer.hammer$setAllowMovement(allowMovement);
			
			source.sendFeedback(() -> Text.translatable("command.hammer.allow_movement.other", otherPlayer.getDisplayName(), allowMovement ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case")), true);
			otherPlayer.getCommandSource().sendFeedback(() -> Text.translatable("command.hammer.allow_movement.self", allowMovement ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case")), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Toggles whether the specified player is allowed to move or not.
	private static int executeAllowMovementPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeAllowMovement(context, ImmutableList.of(player));
	}
	
	// Toggles whether the specified players are allowed to move or not.
	private static int executeAllowMovementPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeAllowMovement(context, getPlayers(context, "players"));
	}
	
	// Toggles whether the specified players are allowed to interact or not.
	private static int executeAllowInteraction(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var source = context.getSource();
		
		var allowInteraction = getBool(context, "allowInteraction");
		
		for (var otherPlayer : players) {
			otherPlayer.hammer$setAllowInteraction(allowInteraction);
			
			source.sendFeedback(() -> Text.translatable("command.hammer.allow_interaction.other", otherPlayer.getDisplayName(), allowInteraction ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case")), true);
			otherPlayer.getCommandSource().sendFeedback(() -> Text.translatable("command.hammer.allow_interaction.self", allowInteraction ? Text.translatable("text.hammer.enabled.lower_case") : Text.translatable("text.hammer.disabled.lower_case")), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Toggles whether the specified player is allowed to interact or not.
	private static int executeAllowInteractionPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeAllowInteraction(context, ImmutableList.of(player));
	}
	
	// Toggles whether the specified players are allowed to interact or not.
	private static int executeAllowInteractionPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeAllowInteraction(context, getPlayers(context, "players"));
	}
	
	
	// Sets players' fly speed.
	private static int executeFlySpeed(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var source = context.getSource();
		
		var speed = getInteger(context, "speed");
		
		var buf = PacketByteBufs.create();
		buf.writeInt(speed);
		
		for (var otherPlayer : players) {
			otherPlayer.getAbilities().setFlySpeed(speed / 20.0F);
			
			ServerPlayNetworking.send(otherPlayer, dev.vini2003.hammer.registry.common.HUNetworking.FLY_SPEED_UPDATE, PacketByteBufs.duplicate(buf));
			
			source.sendFeedback(() -> Text.translatable("command.hammer.fly_speed.other", otherPlayer.getDisplayName(), speed), true);
			otherPlayer.getCommandSource().sendFeedback(() -> Text.translatable("command.hammer.fly_speed.self", otherPlayer.getDisplayName(), speed), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Sets a player's fly speed.
	private static int executeFlySpeedPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeFlySpeed(context, ImmutableList.of(player));
	}
	
	// Sets the fly speed of the specified players.
	private static int executeFlySpeedPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeFlySpeed(context, getPlayers(context, "players"));
	}
	
	// Sets players' walk speed.
	private static int executeWalkSpeed(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var source = context.getSource();
		
		var speed = getInteger(context, "speed");
		
		var buf = PacketByteBufs.create();
		buf.writeInt(speed);
		
		for (var otherPlayer : players) {
			otherPlayer.getAbilities().setWalkSpeed(speed / 20.0F);
			
			ServerPlayNetworking.send(otherPlayer, dev.vini2003.hammer.registry.common.HUNetworking.WALK_SPEED_UPDATE, PacketByteBufs.duplicate(buf));
			
			source.sendFeedback(() -> Text.translatable("command.hammer.walk_speed.other", otherPlayer.getDisplayName(), speed), true);
			otherPlayer.getCommandSource().sendFeedback(() -> Text.translatable("command.hammer.walk_speed.self", otherPlayer.getDisplayName(), speed), false);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Sets a player's walk speed.
	private static int executeWalkSpeedPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeWalkSpeed(context, ImmutableList.of(player));
	}
	
	// Sets the walk speed of the specified players.
	private static int executeWalkSpeedPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeWalkSpeed(context, getPlayers(context, "players"));
	}
	
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, dedicated) -> {
			var toggleEndNode =
					literal("toggle_end")
							.requires(HUCommands::requiresOp)
							.executes(HUCommands::executeToggleEnd);
			
			var toggleNetherNode =
					literal("toggle_end")
							.requires(HUCommands::requiresOp)
							.executes(HUCommands::executeToggleNether);
			
			var freezeNode =
					literal("freeze")
							.requires(HUCommands::requiresOp)
									.then(
											argument("players", players())
													.executes(HUCommands::executeFreezePlayers)
									)
							.executes(HUCommands::executeFreezePlayer);
			
			var unfreezeNode =
					literal("unfreeze")
							.requires(HUCommands::requiresOp)
									.then(
											argument("players", players())
													.executes(HUCommands::executeUnfreezePlayers)
									)
							.executes(HUCommands::executeUnfreezePlayer);
			
			var allowMovementNode = literal("allow_movement")
					.requires(HUCommands::requiresOp)
							.then(
									argument("players", players())
											.executes(HUCommands::executeAllowMovementPlayers)
							)
					.executes(HUCommands::executeAllowMovementPlayer);
			
			var allowInteractionNode = literal("allow_interaction")
					.requires(HUCommands::requiresOp)
							.then(
									argument("players", players())
											.executes(HUCommands::executeAllowInteractionPlayers)
							)
					.executes(HUCommands::executeAllowInteractionPlayer);
			
			var healNode =
					literal("heal")
							.requires(HUCommands::requiresOp)
									.then(
											argument("players", players())
													.executes(HUCommands::executeHealPlayers)
									)
							.executes(HUCommands::executeHealPlayer);
			
			var satiateNode =
					literal("satiate")
							.requires(HUCommands::requiresOp)
									.then(
											argument("players", players())
													.executes(HUCommands::executeSatiatePlayers)
									)
							.executes(HUCommands::executeSatiatePlayer);
			
			var flySpeedNode =
					literal("fly_speed")
							.requires(HUCommands::requiresOp)
									.then(
											argument("players", players())
													.executes(HUCommands::executeFlySpeedPlayers)
									)
							.executes(HUCommands::executeFlySpeedPlayer);
			
			var walkSpeedNode =
					literal("walk_speed")
							.requires(HUCommands::requiresOp)
									.then(
											argument("players", players())
													.executes(HUCommands::executeWalkSpeedPlayers)
									)
							.executes(HUCommands::executeWalkSpeedPlayer);
			
			dispatcher.register(toggleEndNode);
			dispatcher.register(toggleNetherNode);
			
			dispatcher.register(freezeNode);
			dispatcher.register(unfreezeNode);

			dispatcher.register(allowMovementNode);
			dispatcher.register(allowInteractionNode);
			
			dispatcher.register(healNode);
			dispatcher.register(satiateNode);
			
			dispatcher.register(flySpeedNode);
			dispatcher.register(walkSpeedNode);
		});
	}
}
