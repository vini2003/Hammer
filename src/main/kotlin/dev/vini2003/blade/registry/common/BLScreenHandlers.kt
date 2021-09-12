package dev.vini2003.blade.registry.common

import dev.vini2003.blade.BL.id
import dev.vini2003.blade.testing.common.screenhandler.DebugScreenHandler
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandlerType

class BLScreenHandlers {
	companion object {
		@JvmStatic
		val Debug: ScreenHandlerType<DebugScreenHandler> = ScreenHandlerRegistry.registerExtended(id("debug")) { synchronizationID: Int, inventory: PlayerInventory, buffer: PacketByteBuf? ->
			DebugScreenHandler(
					synchronizationID,
					inventory.player
			)
		}

		@JvmStatic
		fun init() {

		}
	}
}