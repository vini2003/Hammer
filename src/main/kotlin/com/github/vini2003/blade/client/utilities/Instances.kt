package com.github.vini2003.blade.client.utilities

import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient

class Instances {
	companion object {
		fun getInstance(): Any {
			return FabricLoader.getInstance().gameInstance
		}

		fun client(): MinecraftClient {
			return MinecraftClient.getInstance()
		}
	}
}