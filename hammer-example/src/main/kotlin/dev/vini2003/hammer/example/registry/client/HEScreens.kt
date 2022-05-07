package dev.vini2003.hammer.example.registry.client

import dev.vini2003.hammer.example.impl.client.screen.handled.DebugHandledScreen
import dev.vini2003.hammer.example.registry.common.HEScreenHandlers
import net.minecraft.client.gui.screen.ingame.HandledScreens

object HEScreens {
	fun init() {
		HandledScreens.register(HEScreenHandlers.DEBUG, ::DebugHandledScreen)
	}
}