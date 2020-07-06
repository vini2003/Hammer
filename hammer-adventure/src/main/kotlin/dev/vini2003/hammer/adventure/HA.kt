package dev.vini2003.hammer.adventure

import dev.vini2003.hammer.adventure.common.util.AdventureUtils
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.kyori.adventure.platform.fabric.FabricServerAudiences

object HA : ModInitializer {
	private var ADVENTURE_INSTANCE: FabricServerAudiences? = null
	
	@JvmStatic
	@get:JvmName("getAudience")
	val ADVENTURE
		get() = ADVENTURE_INSTANCE!!
	
	override fun onInitialize() {
		ServerLifecycleEvents.SERVER_STARTING.register { server ->
			ADVENTURE_INSTANCE = AdventureUtils.createAudience(server)
		}
		
		ServerLifecycleEvents.SERVER_STOPPED.register {
			ADVENTURE_INSTANCE = null
		}
	}
	
}