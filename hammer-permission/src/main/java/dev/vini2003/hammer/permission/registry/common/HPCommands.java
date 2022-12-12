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

package dev.vini2003.hammer.permission.registry.common;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.vini2003.hammer.permission.api.common.manager.RoleManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.minecraft.command.argument.EntityArgumentType.getPlayers;
import static net.minecraft.command.argument.EntityArgumentType.players;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

// TODO: Add feedback for joining/leaving roles.
public class HPCommands {
	private static CompletableFuture<Suggestions> suggestRoles(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
		for (var role : RoleManager.roles()) {
			builder.suggest(role.getName());
		}
		
		return builder.buildFuture();
	}
	
	private static boolean requiresOp(ServerCommandSource source) {
		return source.hasPermissionLevel(4);
	}
	
	// Joins a role.
	private static int executeRoleJoin(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var roleName = getString(context, "role");
		var role = RoleManager.getRoleByName(roleName);
		
		for (var player : players) {
			if (!role.isIn(player)) {
				role.addTo(player);
			}
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Joins a role for the command sender.
	private static int executeRoleJoinPlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeRoleJoin(context, ImmutableList.of(player));
	}
	
	// Joins a role for the specified players.
	private static int executeRoleJoinPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeRoleJoin(context, getPlayers(context, "players"));
	}
	
	// Leaves a role.
	private static int executeRoleLeave(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players) {
		var roleName = getString(context, "role");
		var role = RoleManager.getRoleByName(roleName);
		
		for (var player : players) {
			if (role.isIn(player)) {
				role.removeFrom(player);
			}
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Leaves a role for the command sender.
	private static int executeRoleLeavePlayer(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeRoleLeave(context, ImmutableList.of(player));
	}
	
	// Leaves a role for the specified players.
	private static int executeRoleLeavePlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeRoleLeave(context, getPlayers(context, "players"));
	}
	
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, dedicated) -> {
			var roleNode = literal("role");
			
			var roleJoinNode =
					literal("join")
							.requires(HPCommands::requiresOp)
							.then(
									argument("role", word())
											.suggests(HPCommands::suggestRoles)
											.then(
													argument("players", players())
															.executes(HPCommands::executeRoleJoinPlayers)
											)
											.executes(HPCommands::executeRoleJoinPlayer)
							);
			
			var roleLeaveNode =
					literal("leave")
							.requires(HPCommands::requiresOp)
							.then(
									argument("role", word())
											.suggests(HPCommands::suggestRoles)
											.then(
													argument("players", players())
															.executes(HPCommands::executeRoleLeavePlayers)
											)
											.executes(HPCommands::executeRoleLeavePlayer)
							);
			
			roleNode.then(roleJoinNode);
			roleNode.then(roleLeaveNode);
			
			dispatcher.register(roleNode);
		});
	}
}
