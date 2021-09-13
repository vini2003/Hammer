package dev.vini2003.hammer.client.util

import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.server.MinecraftServer

object Instances {
	val fabric
		get() = FabricLoader.getInstance()
	
	val game
		get() = fabric.gameInstance
	
	val client
		get() = game as MinecraftClient
	
	val server
		get() = game as MinecraftServer
}