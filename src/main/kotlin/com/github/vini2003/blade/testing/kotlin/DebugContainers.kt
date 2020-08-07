package com.github.vini2003.blade.testing.kotlin

import com.github.vini2003.blade.Blade.Companion.identifier
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandlerType

class DebugContainers {
	companion object {
		@JvmStatic
		val DEBUG_HANDLER: ScreenHandlerType<DebugScreenHandler> = ScreenHandlerRegistry.registerExtended(identifier("debug_handler")) { synchronizationID: Int, inventory: PlayerInventory, buffer: PacketByteBuf? ->
			DebugScreenHandler(
					synchronizationID,
					inventory.player
			)
		}

		@JvmStatic
		fun initialize() {

		}
	}
}