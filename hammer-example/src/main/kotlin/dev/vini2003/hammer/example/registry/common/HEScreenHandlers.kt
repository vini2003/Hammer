package dev.vini2003.hammer.example.registry.common

import dev.vini2003.hammer.core.HC
import dev.vini2003.hammer.example.impl.common.screen.handler.DebugScreenHandler
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry

object HEScreenHandlers {
	fun init() = Unit
	
	val DEBUG = ScreenHandlerRegistry.registerExtended(HC.id("debug")) { syncId, inventory, buf ->
		DebugScreenHandler(syncId, inventory.player)
	}
}