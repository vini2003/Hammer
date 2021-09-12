package dev.vini2003.blade.registry.client

import dev.vini2003.blade.testing.client.screen.DebugScreen
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager
import net.minecraft.client.MinecraftClient

class BLCommands {
	companion object {
		@JvmStatic
		fun init() {
			ClientCommandManager.DISPATCHER.register(
				ClientCommandManager.literal("ds").executes {
					MinecraftClient.getInstance().send {
						MinecraftClient.getInstance().setScreen(DebugScreen())
					}
					
					return@executes 1
				}
			)
		}
	}
}