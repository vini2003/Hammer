package dev.vini2003.hammer.preset.registry.common;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static net.minecraft.command.argument.EntityArgumentType.getPlayers;
import static net.minecraft.command.argument.EntityArgumentType.players;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class HPCommands {
	private static final int HEAD_ID = 0;
	private static final int TORSO_ID = 1;
	private static final int ARMS_ID = 2;
	private static final int LEFT_ARM_ID = 3;
	private static final int RIGHT_ARM_ID = 4;
	private static final int LEGS_ID = 5;
	private static final int LEFT_LEG_ID = 6;
	private static final int RIGHT_LEG_ID = 7;
	
	private static boolean requiresOp(ServerCommandSource source) {
		return source.hasPermissionLevel(4);
	}
	
	private static int executeSet(CommandContext<ServerCommandSource> context, Collection<ServerPlayerEntity> players, int type) {
		var state = getBool(context, "state");
		
		for (var player : players) {
			switch (type) {
				case HEAD_ID -> player.hammer$setHead(state);
				case TORSO_ID -> player.hammer$setTorso(state);
				case ARMS_ID -> {
					player.hammer$setLeftArm(state);
					player.hammer$setRightArm(state);
				}
				case LEFT_ARM_ID -> player.hammer$setLeftArm(state);
				case RIGHT_ARM_ID -> player.hammer$setRightArm(state);
				case LEGS_ID -> {
					player.hammer$setLeftLeg(state);
					player.hammer$setRightLeg(state);
				}
				case LEFT_LEG_ID -> player.hammer$setLeftLeg(state);
				case RIGHT_LEG_ID -> player.hammer$setRightLeg(state);
			}
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	private static int executeSetHeadPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeSet(context, getPlayers(context, "players"), HEAD_ID);
	}
	
	private static int executeSetHead(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeSet(context, ImmutableList.of(player), HEAD_ID);
	}
	
	private static int executeSetTorsoPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeSet(context, getPlayers(context, "players"), TORSO_ID);
	}
	
	private static int executeSetTorso(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeSet(context, ImmutableList.of(player), TORSO_ID);
	}
	
	private static int executeSetArmsPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeSet(context, getPlayers(context, "players"), ARMS_ID);
	}
	
	private static int executeSetArms(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeSet(context, ImmutableList.of(player), ARMS_ID);
	}
	
	private static int executeSetLeftArmPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeSet(context, getPlayers(context, "players"), LEFT_ARM_ID);
	}
	
	private static int executeSetLeftArm(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeSet(context, ImmutableList.of(player), LEFT_ARM_ID);
	}
	
	private static int executeSetRightArmPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeSet(context, getPlayers(context, "players"), RIGHT_ARM_ID);
	}
	
	private static int executeSetRightArm(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeSet(context, ImmutableList.of(player), RIGHT_ARM_ID);
	}
	
	private static int executeSetLegsPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeSet(context, getPlayers(context, "players"), LEGS_ID);
	}
	
	private static int executeSetLegs(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeSet(context, ImmutableList.of(player), LEGS_ID);
	}
	
	private static int executeSetLeftLegPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeSet(context, getPlayers(context, "players"), LEFT_LEG_ID);
	}
	
	private static int executeSetLeftLeg(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeSet(context, ImmutableList.of(player), LEFT_LEG_ID);
	}
	
	private static int executeSetRightLegPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		return executeSet(context, getPlayers(context, "players"), RIGHT_LEG_ID);
	}
	
	private static int executeSetRightLeg(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var player = source.getPlayer();
		
		if (player == null) {
			source.sendError(Text.translatable("command.hammer.player_only"));
			return Command.SINGLE_SUCCESS;
		}
		
		return executeSet(context, ImmutableList.of(player), RIGHT_LEG_ID);
	}
	
	public static void init() {
		CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
			var bodyNode = literal("body")
					.requires(HPCommands::requiresOp);
			
			var setNode = literal("set");
			
			var executeSetHeadNode = literal("head")
					.then(
							argument("state", bool())
									.then(
											argument("players", players())
													.executes(HPCommands::executeSetHeadPlayers)
									)
									.executes(HPCommands::executeSetHead)
					);
			
			var executeSetTorsoNode = literal("torso")
					.then(
							argument("state", bool())
									.then(
											argument("players", players())
													.executes(HPCommands::executeSetTorsoPlayers)
									)
									.executes(HPCommands::executeSetTorso)
					);
			
			var executeSetArmsNode = literal("arms")
					.then(
							argument("state", bool())
									.then(
											argument("players", players())
													.executes(HPCommands::executeSetArmsPlayers)
									)
									.executes(HPCommands::executeSetArms)
					);
			
			var executeSetLeftArmNode = literal("left_arm")
					.then(
							argument("state", bool())
									.then(
											argument("players", players())
													.executes(HPCommands::executeSetLeftArmPlayers)
									)
									.executes(HPCommands::executeSetLeftArm)
					);
			
			var executeSetRightArmNode = literal("right_arm")
					.then(
							argument("state", bool())
									.then(
											argument("players", players())
													.executes(HPCommands::executeSetRightArmPlayers)
									)
									.executes(HPCommands::executeSetRightArm)
					);
			
			var executeSetLegsNode = literal("legs")
					.then(
							argument("state", bool())
									.then(
											argument("players", players())
													.executes(HPCommands::executeSetLegsPlayers)
									)
									.executes(HPCommands::executeSetLegs)
					);
			
			var executeSetLeftLegNode = literal("left_leg")
					.then(
							argument("state", bool())
									.then(
											argument("players", players())
													.executes(HPCommands::executeSetLeftLegPlayers)
									)
									.executes(HPCommands::executeSetLeftLeg)
					);
			
			var executeSetRightLegNode = literal("right_leg")
					.then(
							argument("state", bool())
									.then(
											argument("players", players())
													.executes(HPCommands::executeSetRightLegPlayers)
									)
									.executes(HPCommands::executeSetRightLeg)
					);
			
			setNode.then(executeSetHeadNode);
			setNode.then(executeSetTorsoNode);
			setNode.then(executeSetArmsNode);
			setNode.then(executeSetLeftArmNode);
			setNode.then(executeSetRightArmNode);
			setNode.then(executeSetLegsNode);
			setNode.then(executeSetLeftLegNode);
			setNode.then(executeSetRightLegNode);
			
			bodyNode.then(setNode);
			
			dispatcher.register(bodyNode);
		});
	}
}
