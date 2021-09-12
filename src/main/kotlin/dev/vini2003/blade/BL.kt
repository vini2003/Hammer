package dev.vini2003.blade

import dev.vini2003.blade.common.util.Networks
import dev.vini2003.blade.registry.common.BLEvents
import dev.vini2003.blade.registry.common.BLCommands
import dev.vini2003.blade.registry.common.BLNetworks
import dev.vini2003.blade.registry.common.BLScreenHandlers
import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier

object BL : ModInitializer {
	@SuppressWarnings
	const val Id = "blade"

	@JvmStatic
	fun id(string: String): Identifier {
		return Identifier(Id, string)
	}

	override fun onInitialize() {
		BLNetworks.init()
		BLEvents.init()

		if (FabricLoader.getInstance().isDevelopmentEnvironment) {
			BLScreenHandlers.init()
			BLCommands.init()
		}
	}
}