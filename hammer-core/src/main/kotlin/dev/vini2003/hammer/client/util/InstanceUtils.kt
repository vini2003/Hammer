package dev.vini2003.hammer.client.util

import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.server.MinecraftServer

object InstanceUtils {
	@JvmStatic
	@get:JvmName("getFabric")
	val FABRIC
		get() = FabricLoader.getInstance()
	
	@JvmStatic
	@get:JvmName("getGame")
	val GAME
		get() = FABRIC.gameInstance
	
	@JvmStatic
	@get:JvmName("getClient")
	val CLIENT
		get() = GAME as MinecraftClient
	
	@JvmStatic
	@get:JvmName("getServer")
	val SERVER
		get() = GAME as MinecraftServer
	
	@JvmStatic
	@get:JvmName("isClient")
	val IS_CLIENT
		get() = GAME !is MinecraftServer
	
	@JvmStatic
	@get:JvmName("isServer")
	val IS_SERVER
		get() = GAME is MinecraftServer
}