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

package dev.vini2003.hammer.gravity.registry.common;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.vini2003.hammer.gravity.api.common.manager.GravityManager;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.FloatArgumentType.floatArg;
import static com.mojang.brigadier.arguments.FloatArgumentType.getFloat;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class HGCommands {
	private static boolean requiresOp(ServerCommandSource source) {
		return source.hasPermissionLevel(4);
	}
	
	// Changes gravity for the specified world.
	private static int executeGravity(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		var source = context.getSource();

		var world = DimensionArgumentType.getDimensionArgument(context, "world");
		var gravity = getFloat(context, "gravity");
		
		GravityManager.set(world, gravity);
		
		source.sendFeedback(Text.translatable("command.hammer.gravity", world.getRegistryKey().toString(), gravity), true);
		
		return Command.SINGLE_SUCCESS;
	}
	
	public static void init() {
		CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> {
			var gravityNode =
					literal("gravity")
							.requires(HGCommands::requiresOp)
							.then(
									argument("world", DimensionArgumentType.dimension())
											.then(
													argument("gravity", floatArg())
															.executes(HGCommands::executeGravity)
									)
					);
			
			dispatcher.register(gravityNode);
		});
	}
}
