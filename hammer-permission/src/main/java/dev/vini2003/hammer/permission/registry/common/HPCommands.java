package dev.vini2003.hammer.permission.registry.common;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import dev.vini2003.hammer.permission.api.common.manager.RoleManager;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;

public class HPCommands {
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			var roleNode = CommandManager.literal("role");
			
			var roleJoinNode = CommandManager.literal("join").requires(source -> {
				return source.hasPermissionLevel(4);
			}).then(
					CommandManager.argument("role_name", StringArgumentType.word()).suggests((context, builder) -> {
						for (var role : RoleManager.roles()) {
							builder.suggest(role.getName());
						}
						
						return builder.buildFuture();
					}).executes(context -> {
						var roleName = StringArgumentType.getString(context, "role_name");
						var role = RoleManager.getRoleByName(roleName);
						
						var source = context.getSource();
						var player = source.getPlayer();
						
						if (!role.isIn(player)) {
							role.addTo(player);
						}
						
						return Command.SINGLE_SUCCESS;
					})
			).then(
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
					}).executes(context -> {
						var roleName = StringArgumentType.getString(context, "role_name");
						var role = RoleManager.getRoleByName(roleName);
						
						var source = context.getSource();
						var player = source.getPlayer();
						
						if (role.isIn(player)) {
							role.removeFrom(player);
						}
						
						return Command.SINGLE_SUCCESS;
					})
			).then(
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
			);
			
			roleNode.then(roleJoinNode);
			roleNode.then(roleLeaveNode);
			
			dispatcher.register(roleNode);
		});
	}
}
