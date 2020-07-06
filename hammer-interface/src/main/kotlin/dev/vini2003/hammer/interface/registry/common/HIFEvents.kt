package dev.vini2003.hammer.`interface`.registry.common

import dev.vini2003.hammer.`interface`.common.listener.ScreenHandlerServerTickListener
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents

object HIFEvents {
	fun init() {
		ServerTickEvents.END_SERVER_TICK.register(ScreenHandlerServerTickListener)
	}
}