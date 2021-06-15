package dev.vini2003.blade.registry.client

import dev.vini2003.blade.registry.common.BLScreenHandlers
import dev.vini2003.blade.testing.client.screen.DebugHandledScreen
import dev.vini2003.blade.testing.common.screenhandler.DebugScreenHandler
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class BLScreens {
	companion object {
		fun initialize() {
			ScreenRegistry.register(BLScreenHandlers.Debug) { handler: DebugScreenHandler, inventory: PlayerInventory, title: Text -> DebugHandledScreen(handler, inventory, title) }
		}
	}
}