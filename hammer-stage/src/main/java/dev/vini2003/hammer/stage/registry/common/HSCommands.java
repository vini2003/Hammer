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

package dev.vini2003.hammer.stage.registry.common;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.vini2003.hammer.core.api.common.command.argument.PositionArgumentType;
import dev.vini2003.hammer.core.api.common.command.argument.SizeArgumentType;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.stage.api.common.manager.StageManager;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class HSCommands {
	// Loads a stage.
	private static int stageLoad(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var source = context.getSource();
		var player = source.getPlayer();
		
		var stageId = context.getArgument("id", Identifier.class);
		
		var stagePosition = context.getArgument("position", Position.class);
		
		var stageSize = context.getArgument("size", Size.class);
		
		var active = StageManager.getActive();
		
		if (active != null) {
			source.sendFeedback(new TranslatableText("command.hammer.stage.load.failure"), false);
		}
		
		var stage = StageManager.create(stageId);
		stage.setPosition(stagePosition);
		stage.setSize(stageSize);
		
		StageManager.setActive(stage);
		
		stage.load(player.world);
		
		source.sendFeedback(new TranslatableText("command.hammer.stage.load.success", stageId), false);
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Unloads the currently active stage.
	private static int stageUnload(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var source = context.getSource();
		var player = source.getPlayer();
		
		var stage = StageManager.getActive();
		
		if (stage != null) {
			stage.unload(player.world);
			
			StageManager.setActive(null);
			
			source.sendFeedback(new TranslatableText("command.hammer.stage.unload.success"), true);
		} else {
			source.sendFeedback(new TranslatableText("command.hammer.stage.unload.none"), true);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Prepares the currently active stage.
	private static int stagePrepare(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var source = context.getSource();
		var player = source.getPlayer();
		
		var stage = StageManager.getActive();
		
		if (stage != null) {
			stage.prepare(player.world);
			
			source.sendFeedback(new TranslatableText("command.hammer.stage.prepare.success"), true);
		} else {
			source.sendFeedback(new TranslatableText("command.hammer.stage.prepare.failure"), true);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Starts the currently active stage.
	private static int stageStart(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var source = context.getSource();
		var player = source.getPlayer();
		
		var stage = StageManager.getActive();
		
		if (stage != null) {
			stage.start(player.world);
			
			source.sendFeedback(new TranslatableText("command.hammer.stage.start.success"), true);
		} else {
			source.sendFeedback(new TranslatableText("command.hammer.stage.start.failure"), true);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Stops the currently active stage.
	private static int stageStop(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var source = context.getSource();
		var player = source.getPlayer();
		
		var stage = StageManager.getActive();
		
		if (stage != null) {
			stage.stop(player.world);
			
			source.sendFeedback(new TranslatableText("command.hammer.stage.stop.success"), true);
		} else {
			source.sendFeedback(new TranslatableText("command.hammer.stage.stop.failure"), true);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Pauses the currently active stage.
	private static int stagePause(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var source = context.getSource();
		var player = source.getPlayer();
		
		var stage = StageManager.getActive();
		
		if (stage != null) {
			stage.pause(player.world);
			
			source.sendFeedback(new TranslatableText("command.hammer.stage.pause.success"), true);
		} else {
			source.sendFeedback(new TranslatableText("command.hammer.stage.pause.failure"), true);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Restarts the currently active stage.
	private static int stageRestart(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var source = context.getSource();
		var player = source.getPlayer();
		
		var stage = StageManager.getActive();
		
		if (stage != null) {
			stage.restart(player.world);
			
			source.sendFeedback(new TranslatableText("command.hammer.stage.restart.success"), true);
		} else {
			source.sendFeedback(new TranslatableText("command.hammer.stage.restart.failure"), true);
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(
					literal("stage").requires(source -> {
						return source.hasPermissionLevel(4);
					}).then(
							literal("load").then(
									argument("id", IdentifierArgumentType.identifier()).then(
											argument("position", PositionArgumentType.position()).then(
													argument("size", SizeArgumentType.size()).executes(HSCommands::stageLoad)
											)
									)
							)
					).then(
							literal("unload").executes(HSCommands::stageUnload)
					).then(
							literal("prepare").executes(HSCommands::stagePrepare)
					).then(
							literal("start").executes(HSCommands::stageStart)
					).then(
							literal("stop").executes(HSCommands::stageStop)
					).then(
							literal("pause").executes(HSCommands::stagePause)
					).then(
							literal("restart").executes(HSCommands::stageRestart)
					)
			);
		});
	}
}
