package dev.vini2003.hammer.gui.registry.common

import dev.vini2003.hammer.gui.common.listener.ScreenHandlerServerTickListener
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents

object HGUIEvents {
	fun init() {
		ServerTickEvents.END_SERVER_TICK.register(ScreenHandlerServerTickListener)
	}
}