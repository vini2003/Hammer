package dev.vini2003.hammer.gravity.common.command.gravity

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import dev.vini2003.hammer.command.common.command.ServerRootCommand
import dev.vini2003.hammer.command.common.util.extension.*
import dev.vini2003.hammer.gravity.common.manager.GravityManager
import net.minecraft.command.CommandSource
import net.minecraft.command.argument.RegistryKeyArgumentType
import net.minecraft.text.TranslatableText
import net.minecraft.util.registry.Registry

val GRAVITY_COMMAND = ServerRootCommand {
	val UNKNOWN_REGISTRY_KEY_EXCEPTION = DynamicCommandExceptionType { id ->
		TranslatableText("command.hammer.unknown_registry_key", id)
	}
	
	command("gravity") {
		argument("world", RegistryKeyArgumentType.registryKey(Registry.WORLD_KEY)) {
			suggests { context, builder ->
				CommandSource.suggestIdentifiers(context.source.worldKeys.map { key -> key.value }, builder)
			}
			
			float("gravity", {
				execute {
					val key = RegistryKeyArgumentType.getKey(this, "world", Registry.WORLD_KEY, UNKNOWN_REGISTRY_KEY_EXCEPTION)
					val gravity = getFloat("gravity")
					
					GravityManager.set(key, gravity, source.world)
					
					source.sendFeedback(TranslatableText("command.hammer.gravity", key.value.toString(), String.format("%.2f", gravity)), false)
				}
			})
		}
	}
}