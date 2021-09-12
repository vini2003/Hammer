package dev.vini2003.blade.client.util

import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.server.MinecraftServer

object Instances {
	val fabricInstance
		get() = FabricLoader.getInstance()
	
	val gameInstance
		get() = fabricInstance.gameInstance
	
	val client
		get() = gameInstance as MinecraftClient
	
	val server
		get() = gameInstance as MinecraftServer
}