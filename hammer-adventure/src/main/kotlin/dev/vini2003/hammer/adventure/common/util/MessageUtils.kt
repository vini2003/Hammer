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

package dev.vini2003.hammer.adventure.common.util

import dev.vini2003.hammer.adventure.common.util.extension.sendFeedback
import dev.vini2003.hammer.adventure.common.util.extension.sendMessage
import dev.vini2003.hammer.adventure.common.util.extension.sendSubtitle
import dev.vini2003.hammer.adventure.common.util.extension.sendTitle
import net.kyori.adventure.text.Component
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld

object MessageUtils {
	@JvmStatic
	fun sendMessage(source: ServerCommandSource, component: Component, actionBar: Boolean) = source.sendMessage(component, actionBar)
	
	@JvmStatic
	fun sendTitle(source: ServerCommandSource, component: Component) = source.sendTitle(component)
	
	@JvmStatic
	fun sendSubtitle(source: ServerCommandSource, component: Component) = source.sendSubtitle(component)
	
	@JvmStatic
	fun sendFeedback(source: ServerCommandSource, component: Component, broadcastToOps: Boolean) = source.sendFeedback(component, broadcastToOps)
	
	@JvmStatic
	fun sendMessage(player: ServerPlayerEntity, component: Component, actionBar: Boolean) = player.sendMessage(component, actionBar)
	
	@JvmStatic
	fun sendTitle(player: ServerPlayerEntity, component: Component) = player.sendTitle(component)
	
	@JvmStatic
	fun sendSubtitle(player: ServerPlayerEntity, component: Component) = player.sendSubtitle(component)
	
	@JvmStatic
	fun sendMessage(world: ServerWorld, component: Component, actionBar: Boolean) = world.sendMessage(component, actionBar)

	@JvmStatic
	fun sendTitle(world: ServerWorld, component: Component) = world.sendTitle(component)
	
	@JvmStatic
	fun sendSubtitle(world: ServerWorld, component: Component) = world.sendSubtitle(component)
}