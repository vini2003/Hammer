package dev.vini2003.hammer.gui

import dev.vini2003.hammer.gui.registry.client.HGUIEvents
import dev.vini2003.hammer.gui.registry.client.HGUIKeyBinds
import net.fabricmc.api.ClientModInitializer

object HGUIClient : ClientModInitializer {
	override fun onInitializeClient() {
		HGUIEvents.init()
		HGUIKeyBinds.init()
	}
}