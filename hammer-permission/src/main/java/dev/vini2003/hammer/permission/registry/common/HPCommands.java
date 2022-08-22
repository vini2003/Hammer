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

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.vini2003.hammer.permission.api.common.manager.RoleManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;

public class HPCommands {
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, dedicated) -> {
			var roleNode = CommandManager.literal("role");
			
			var roleJoinNode = CommandManager.literal("join").requires(source -> {
				return source.hasPermissionLevel(4);
			}).then(
					CommandManager.argument("role_name", StringArgumentType.word()).suggests((context, builder) -> {
						for (var role : RoleManager.roles()) {
							builder.suggest(role.getName());
						}
						
						return builder.buildFuture();
					}).then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var roleName = StringArgumentType.getString(context, "role_name");
								var role = RoleManager.getRoleByName(roleName);
								
								var players = EntityArgumentType.getPlayers(context, "players");
								
								for (var player : players) {
									if (!role.isIn(player)) {
										role.addTo(player);
									}
								}
								
								return Command.SINGLE_SUCCESS;
							})
					).executes(context -> {
						var roleName = StringArgumentType.getString(context, "role_name");
						var role = RoleManager.getRoleByName(roleName);
						
						var source = context.getSource();
						var player = source.getPlayer();
						
						if (!role.isIn(player)) {
							role.addTo(player);
						}
						
						return Command.SINGLE_SUCCESS;
					})
			);
			
			var roleLeaveNode = CommandManager.literal("leave").requires(source -> {
				return source.hasPermissionLevel(4);
			}).then(
					CommandManager.argument("role_name", StringArgumentType.word()).suggests((context, builder) -> {
						var source = context.getSource();
						var player = source.getPlayer();
						
						for (var role : RoleManager.roles()) {
							if (role.isIn(player)) {
								builder.suggest(role.getName());
							}
						}
						
						return builder.buildFuture();
					}).then(
							CommandManager.argument("players", EntityArgumentType.players()).executes(context -> {
								var roleName = StringArgumentType.getString(context, "role_name");
								var role = RoleManager.getRoleByName(roleName);
								
								var players = EntityArgumentType.getPlayers(context, "players");
								
								for (var player : players) {
									if (role.isIn(player)) {
										role.removeFrom(player);
									}
								}
								
								return Command.SINGLE_SUCCESS;
							})
					).executes(context -> {
						var roleName = StringArgumentType.getString(context, "role_name");
						var role = RoleManager.getRoleByName(roleName);
						
						var source = context.getSource();
						var player = source.getPlayer();
						
						if (role.isIn(player)) {
							role.removeFrom(player);
						}
						
						return Command.SINGLE_SUCCESS;
					})
			);
			
			roleNode.then(roleJoinNode);
			roleNode.then(roleLeaveNode);
			
			dispatcher.register(roleNode);
		});
	}
}
