package dev.vini2003.hammer.chat

import dev.vini2003.hammer.chat.registry.common.HCCommands
import dev.vini2003.hammer.chat.registry.common.HCEvents
import dev.vini2003.hammer.chat.registry.common.HCNetworking
import net.fabricmc.api.ModInitializer

object HC : ModInitializer {
	override fun onInitialize() {
		HCNetworking.init()
		HCCommands.init()
		HCEvents.init()
	}
	
}