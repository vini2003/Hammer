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
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.vini2003.hammer.core.api.common.command.argument.PositionArgumentType;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.stage.api.common.manager.StageManager;
import dev.vini2003.hammer.stage.api.common.stage.Stage;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

import static dev.vini2003.hammer.core.api.common.command.argument.PositionArgumentType.position;
import static dev.vini2003.hammer.core.api.common.command.argument.SizeArgumentType.size;
import static net.minecraft.command.argument.IdentifierArgumentType.identifier;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class HSCommands {
	private static boolean requiresOp(ServerCommandSource source) {
		return source.hasPermissionLevel(4);
	}
	
	private static CompletableFuture<Suggestions> suggestStageIds(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
		for (var entry : StageManager.getFactories().entrySet()) {
			builder.suggest(entry.getKey().toString());
		}
		
		return builder.buildFuture();
	}
	
	// Loads a schematic stage.
	private static int executeSchematicStageLoad(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var server = source.getServer();
		
		var world = server.getWorld(HSDimensions.VOID_WORLD_KEY);
		if (world == null) {
			source.sendError(Text.translatable("command.hammer.stage.load.failure", Text.translatable("command.hammer.stage.world_not_found")));
			return Command.SINGLE_SUCCESS;
		}
		
		var stageId = context.getArgument("id", Identifier.class);
		
		var active = StageManager.getActive(world.getRegistryKey());
		
		if (active != null) {
			source.sendError(Text.translatable("command.hammer.stage.load.failure", Text.translatable("command.hammer.stage.none_active")));
			return Command.SINGLE_SUCCESS;
		}
		
		try {
			var stage = StageManager.create(stageId, world);
			
			StageManager.setActive(world.getRegistryKey(), stage);
			
			try {
				source.sendFeedback(Text.translatable("command.hammer.stage.load.progress"), false);
				
				stage.load(world);
			} catch (Exception e) {
				source.sendError(Text.translatable("command.hammer.stage.load.failure", stageId, e.getMessage()));
				return Command.SINGLE_SUCCESS;
			}
			
			source.sendFeedback(Text.translatable("command.hammer.stage.load.success", stageId), false);
		} catch (Exception e) {
			source.sendError(Text.translatable("command.hammer.stage.load.failure", stageId, e.getMessage()));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Loads a stage.
	private static int executeStageLoad(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var server = source.getServer();
		
		var world = server.getWorld(HSDimensions.VOID_WORLD_KEY);
		if (world == null) {
			source.sendError(Text.translatable("command.hammer.stage.load.failure", Text.translatable("command.hammer.stage.world_not_found")));
			return Command.SINGLE_SUCCESS;
		}
		
		var stageId = context.getArgument("id", Identifier.class);
		
		var stagePosition = PositionArgumentType.getPosition(context, "position");
		
		var stageSize = context.getArgument("size", Size.class);
		
		var active = StageManager.getActive(world.getRegistryKey());
		
		if (active != null) {
			source.sendError(Text.translatable("command.hammer.stage.load.failure", Text.translatable("command.hammer.stage.none_active")));
			return Command.SINGLE_SUCCESS;
		}
		
		try {
			var stage = StageManager.create(stageId, world);
			stage.setPosition(stagePosition);
			stage.setSize(stageSize);
			
			StageManager.setActive(world.getRegistryKey(), stage);
			
			try {
				source.sendFeedback(Text.translatable("command.hammer.stage.load.progress"), false);
				
				stage.load(world);
			} catch (Exception e) {
				source.sendError(Text.translatable("command.hammer.stage.load.failure", stageId, e.getMessage()));
				return Command.SINGLE_SUCCESS;
			}
			
			source.sendFeedback(Text.translatable("command.hammer.stage.load.success", stageId), false);
		} catch (Exception e) {
			source.sendError(Text.translatable("command.hammer.stage.load.failure", stageId, e.getMessage()));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Unloads the currently active stage.
	private static int executeStageUnload(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var server = source.getServer();
		
		var world = server.getWorld(HSDimensions.VOID_WORLD_KEY);
		if (world == null) {
			source.sendError(Text.translatable("command.hammer.stage.load.failure", Text.translatable("command.hammer.stage.world_not_found")));
			return Command.SINGLE_SUCCESS;
		}
		
		var stage = StageManager.getActive(world.getRegistryKey());
		
		if (stage != null) {
			try {
				stage.unload(world);
			} catch (Exception e) {
				source.sendError(Text.translatable("command.hammer.stage.unload.failure", stage.getId(), e.getMessage()));
				return Command.SINGLE_SUCCESS;
			}
			
			StageManager.setActive(world.getRegistryKey(), null);
			
			source.sendFeedback(Text.translatable("command.hammer.stage.unload.success"), true);
		} else {
			source.sendError(Text.translatable("command.hammer.stage.unload.failure", Text.translatable("command.hammer.stage.none_active")));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Prepares the currently active stage.
	private static int executeStagePrepare(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var server = source.getServer();
		
		var world = server.getWorld(HSDimensions.VOID_WORLD_KEY);
		if (world == null) {
			source.sendError(Text.translatable("command.hammer.stage.load.failure", Text.translatable("command.hammer.stage.world_not_found")));
			return Command.SINGLE_SUCCESS;
		}
		
		var stage = StageManager.getActive(world.getRegistryKey());
		
		if (stage != null) {
			try {
				stage.prepare(world);
			} catch (Exception e) {
				source.sendError(Text.translatable("command.hammer.stage.prepare.failure", stage.getId(), e.getMessage()));
				return Command.SINGLE_SUCCESS;
			}
			
			source.sendFeedback(Text.translatable("command.hammer.stage.prepare.success"), true);
		} else {
			source.sendError(Text.translatable("command.hammer.stage.prepare.failure", Text.translatable("command.hammer.stage.none_active")));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Starts the currently active stage.
	private static int executeStageStart(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var server = source.getServer();
		
		var world = server.getWorld(HSDimensions.VOID_WORLD_KEY);
		if (world == null) {
			source.sendError(Text.translatable("command.hammer.stage.load.failure", Text.translatable("command.hammer.stage.world_not_found")));
			return Command.SINGLE_SUCCESS;
		}
		
		var stage = StageManager.getActive(world.getRegistryKey());
		
		if (stage != null) {
			try {
				stage.start(world);
			} catch (Exception e) {
				source.sendError(Text.translatable("command.hammer.stage.start.failure", stage.getId(), e.getMessage()));
				return Command.SINGLE_SUCCESS;
			}
			
			source.sendFeedback(Text.translatable("command.hammer.stage.start.success"), true);
		} else {
			source.sendError(Text.translatable("command.hammer.stage.start.failure", Text.translatable("command.hammer.stage.none_active")));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Stops the currently active stage.
	private static int executeStageStop(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var server = source.getServer();
		
		var world = server.getWorld(HSDimensions.VOID_WORLD_KEY);
		if (world == null) {
			source.sendError(Text.translatable("command.hammer.stage.load.failure", Text.translatable("command.hammer.stage.world_not_found")));
			return Command.SINGLE_SUCCESS;
		}
		
		var stage = StageManager.getActive(world.getRegistryKey());
		
		if (stage != null) {
			try {
				stage.stop(world);
			} catch (Exception e) {
				source.sendError(Text.translatable("command.hammer.stage.stop.failure", stage.getId(), e.getMessage()));
				return Command.SINGLE_SUCCESS;
			}
			
			source.sendFeedback(Text.translatable("command.hammer.stage.stop.success"), true);
		} else {
			source.sendError(Text.translatable("command.hammer.stage.stop.failure", Text.translatable("command.hammer.stage.none_active")));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Pauses the currently active stage.
	private static int executeStagePause(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var server = source.getServer();
		
		var world = server.getWorld(HSDimensions.VOID_WORLD_KEY);
		if (world == null) {
			source.sendError(Text.translatable("command.hammer.stage.load.failure", Text.translatable("command.hammer.stage.world_not_found")));
			return Command.SINGLE_SUCCESS;
		}
		
		var stage = StageManager.getActive(world.getRegistryKey());
		
		if (stage != null) {
			try {
				stage.pause(world);
			} catch (Exception e) {
				source.sendError(Text.translatable("command.hammer.stage.pause.failure", stage.getId(), e.getMessage()));
				return Command.SINGLE_SUCCESS;
			}
			
			if (stage.getState() == Stage.State.PAUSED) {
				source.sendFeedback(Text.translatable("command.hammer.stage.pause.success"), true);
			} else {
				source.sendFeedback(Text.translatable("command.hammer.stage.unpause.success"), true);
			}
		} else {
			source.sendError(Text.translatable("command.hammer.stage.pause.failure", Text.translatable("command.hammer.stage.none_active")));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	// Restarts the currently active stage.
	private static int executeStageRestart(CommandContext<ServerCommandSource> context) {
		var source = context.getSource();
		var server = source.getServer();
		
		var world = server.getWorld(HSDimensions.VOID_WORLD_KEY);
		if (world == null) {
			source.sendError(Text.translatable("command.hammer.stage.load.failure", Text.translatable("command.hammer.stage.world_not_found")));
			return Command.SINGLE_SUCCESS;
		}
		
		var stage = StageManager.getActive(world.getRegistryKey());
		
		if (stage != null) {
			try {
				stage.restart(world);
			} catch (Exception e) {
				source.sendError(Text.translatable("command.hammer.stage.restart.failure", stage.getId(), e.getMessage()));
				return Command.SINGLE_SUCCESS;
			}
			
			source.sendFeedback(Text.translatable("command.hammer.stage.restart.success"), true);
		} else {
			source.sendError(Text.translatable("command.hammer.stage.restart.failure", Text.translatable("command.hammer.stage.none_active")));
		}
		
		return Command.SINGLE_SUCCESS;
	}
	
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, dedicated) -> {
			var stageNode =
					literal("stage")
							.requires(HSCommands::requiresOp);
			
			var stageLoadNode =
					literal("load")
							.then(
									argument("id", identifier())
											.suggests(HSCommands::suggestStageIds)
										  	.then(
													   argument("position", position())
															   .then(
																	   argument("size", size())
																			   .executes(HSCommands::executeStageLoad)
															   )
										  	)
											.executes(HSCommands::executeSchematicStageLoad)
							);
			
			var stageUnloadNode =
					literal("unload")
							.executes(HSCommands::executeStageUnload);
			
			var stagePrepareNode =
					literal("prepare")
							.executes(HSCommands::executeStagePrepare);
			
			var stageStartNode =
					literal("start")
							.executes(HSCommands::executeStageStart);
			
			var stageStopNode =
					literal("stop")
							.executes(HSCommands::executeStageStop);
			
			var stagePauseNode =
					literal("pause")
							.executes(HSCommands::executeStagePause);
			
			var stageRestartNode =
					literal("restart")
							.executes(HSCommands::executeStageRestart);
			
			stageNode.then(stageLoadNode);
			stageNode.then(stageUnloadNode);
			stageNode.then(stagePrepareNode);
			stageNode.then(stageStartNode);
			stageNode.then(stageStopNode);
			stageNode.then(stagePauseNode);
			stageNode.then(stageRestartNode);
			
			dispatcher.register(stageNode);
		});
	}
}
