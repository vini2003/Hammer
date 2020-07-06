package dev.vini2003.hammer.permission

import dev.vini2003.hammer.permission.registry.common.HPCommands
import dev.vini2003.hammer.permission.registry.common.HPEvents
import dev.vini2003.hammer.permission.registry.common.HPNetworking
import net.fabricmc.api.ModInitializer
import net.luckperms.api.LuckPermsProvider

object HP : ModInitializer {
	@JvmStatic
	@get:JvmName("getLuckPerms")
	val LUCK_PERMS by lazy { LuckPermsProvider.get() }
	
	override fun onInitialize() {
		HPNetworking.init()
		HPEvents.init()
		HPCommands.init()
	}
}