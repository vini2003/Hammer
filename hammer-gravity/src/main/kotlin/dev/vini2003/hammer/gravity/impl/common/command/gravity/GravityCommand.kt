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

package dev.vini2003.hammer.gravity.impl.common.command.gravity

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import dev.vini2003.hammer.command.api.common.command.ServerRootCommand
import dev.vini2003.hammer.command.api.common.util.extension.*
import dev.vini2003.hammer.gravity.api.common.manager.GravityManager
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
					
					GravityManager.set(key, gravity)
					
					source.sendFeedback(TranslatableText("command.hammer.gravity", key.value.toString(), String.format("%.2f", gravity)), false)
				}
			})
		}
	}
}