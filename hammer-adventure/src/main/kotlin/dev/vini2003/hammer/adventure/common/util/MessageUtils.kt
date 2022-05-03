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