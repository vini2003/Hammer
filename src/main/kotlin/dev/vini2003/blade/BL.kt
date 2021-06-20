package dev.vini2003.blade

import dev.vini2003.blade.common.utilities.Networks
import dev.vini2003.blade.common.utilities.Resources
import dev.vini2003.blade.registry.common.BLCallbacks
import dev.vini2003.blade.registry.common.BLCommands
import dev.vini2003.blade.registry.common.BLScreenHandlers
import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier

class BL : ModInitializer {
	companion object {
		@SuppressWarnings
		const val Id = "blade"

		@JvmStatic
		fun id(string: String): Identifier {
			return Identifier(Id, string)
		}
	}

	override fun onInitialize() {
		Resources.init()
		Networks.init()
		
		BLCallbacks.init()

		if (FabricLoader.getInstance().isDevelopmentEnvironment) {
			BLScreenHandlers.init()
			BLCommands.init()
		}
	}
}