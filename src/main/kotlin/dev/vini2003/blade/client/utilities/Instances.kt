package dev.vini2003.blade.client.utilities

import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient

class Instances {
	companion object {
		@JvmStatic
		fun instance(): Any {
			return FabricLoader.getInstance().gameInstance
		}

		@JvmStatic
		fun client(): MinecraftClient {
			return MinecraftClient.getInstance()
		}
	}
}