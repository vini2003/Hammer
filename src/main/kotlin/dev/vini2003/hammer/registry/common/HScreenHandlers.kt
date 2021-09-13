package dev.vini2003.hammer.registry.common

import dev.vini2003.hammer.H.id
import dev.vini2003.hammer.testing.common.screenhandler.DebugScreenHandler
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandlerType

class HScreenHandlers {
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