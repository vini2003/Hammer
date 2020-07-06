package dev.vini2003.hammer.`interface`.registry.client

import dev.vini2003.hammer.`interface`.client.listener.ScreenHandlerClientTickListener
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents

object HIFEvents {
	fun init() {
		ClientTickEvents.END_CLIENT_TICK.register(ScreenHandlerClientTickListener)
	}
}