package dev.vini2003.hammer.example

import dev.vini2003.hammer.example.registry.client.HEScreens
import dev.vini2003.hammer.gui.api.client.event.InGameHudEvents
import dev.vini2003.hammer.gui.api.common.widget.BaseWidgetCollection
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.gui.hud.InGameHud

object HEClient : ClientModInitializer {
	override fun onInitializeClient() {
		HEScreens.init()
		
		val listener = object : InGameHudEvents.Init {
			override fun onInit(hud: InGameHud, collection: BaseWidgetCollection) {
				TODO("Not yet implemented")
			}
			
		}
		
		InGameHudEvents.INIT.register { hud, collection ->
		
		}
	}
}