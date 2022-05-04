package dev.vini2003.hammer.gui


import dev.vini2003.hammer.gui.registry.common.HGUIEvents
import dev.vini2003.hammer.gui.registry.common.HGUINetworking
import net.fabricmc.api.ModInitializer

object HGUI : ModInitializer {
	override fun onInitialize() {
		HGUINetworking.init()
		HGUIEvents.init()
	}
}