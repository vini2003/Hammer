package dev.vini2003.hammer.gravity.registry.common;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.vini2003.hammer.gravity.api.common.manager.GravityManager;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.RegistryKeyArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;


public class HGCommands {
	public static void init() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			dispatcher.register(
					CommandManager.literal("gravity").then(
							CommandManager.argument("world", RegistryKeyArgumentType.registryKey(Registry.WORLD_KEY)).then(
									CommandManager.argument("gravity", FloatArgumentType.floatArg()).executes(context -> {
										var world = RegistryKeyArgumentType.<World>getKey(context, "world", Registry.WORLD_KEY, new DynamicCommandExceptionType(id -> new TranslatableText("command.hammer.unknown_registry_key", id)));
										var gravity = FloatArgumentType.getFloat(context, "gravity");
										
										GravityManager.set(world, gravity);
										
										return Command.SINGLE_SUCCESS;
									})
							)
					)
			);
		});
	}
}
