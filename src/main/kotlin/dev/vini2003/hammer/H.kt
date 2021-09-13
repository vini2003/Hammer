package dev.vini2003.hammer

import dev.vini2003.hammer.client.util.Instances
import dev.vini2003.hammer.registry.common.HEvents
import dev.vini2003.hammer.registry.common.HCommands
import dev.vini2003.hammer.registry.common.HNetworks
import dev.vini2003.hammer.registry.common.HScreenHandlers
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier

object H : ModInitializer {
	@SuppressWarnings
	const val Id = "hammer"

	@JvmStatic
	fun id(string: String): Identifier {
		return Identifier(Id, string)
	}

	override fun onInitialize() {
		HNetworks.init()
		HEvents.init()

		if (Instances.fabric.isDevelopmentEnvironment) {
			HScreenHandlers.init()
			HCommands.init()
		}
	}
}