package dev.vini2003.hammer.registry.client

import dev.vini2003.hammer.registry.common.HScreenHandlers
import dev.vini2003.hammer.testing.client.screen.DebugHandledScreen
import dev.vini2003.hammer.testing.common.screenhandler.DebugScreenHandler
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class HScreens {
	companion object {
		fun initialize() {
			ScreenRegistry.register(HScreenHandlers.Debug) { handler: DebugScreenHandler, inventory: PlayerInventory, title: Text -> DebugHandledScreen(handler, inventory, title) }
		}
	}
}