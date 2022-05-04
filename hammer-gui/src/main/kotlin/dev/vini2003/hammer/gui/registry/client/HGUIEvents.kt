package dev.vini2003.hammer.gui.registry.client

import dev.vini2003.hammer.gui.client.listener.ScreenHandlerClientTickListener
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents

object HGUIEvents {
	fun init() {
		ClientTickEvents.END_CLIENT_TICK.register(ScreenHandlerClientTickListener)
	}
}