package com.github.vini2003.blade

import com.github.vini2003.blade.common.utilities.Networks
import com.github.vini2003.blade.common.utilities.Resources
import com.github.vini2003.blade.testing.kotlin.DebugCommands
import com.github.vini2003.blade.testing.kotlin.DebugContainers
import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class Blade : ModInitializer {
	companion object {
		@SuppressWarnings
		const val MOD_ID = "blade"

		@JvmStatic
		val LOGGER: Logger = LogManager.getLogger(MOD_ID)

		@JvmStatic
		fun identifier(string: String): Identifier {
			return Identifier(MOD_ID, string)
		}
	}

	override fun onInitialize() {
		Resources.initialize()
		Networks.initialize()

		if (FabricLoader.getInstance().isDevelopmentEnvironment) {
			DebugContainers.initialize()
			DebugCommands.initialize()
		}
	}
}