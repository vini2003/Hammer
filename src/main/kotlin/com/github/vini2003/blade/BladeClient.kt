package com.github.vini2003.blade

import com.github.vini2003.blade.testing.kotlin.DebugScreens
import net.fabricmc.api.ClientModInitializer

class BladeClient : ClientModInitializer {
	override fun onInitializeClient() {
		DebugScreens.initialize()
	}
}