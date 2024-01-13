package dev.vini2003.hammer.config;

import net.fabricmc.api.ModInitializer;

public class HC {
	public static void onInitialize() {
		dev.vini2003.hammer.core.HC.LOGGER.info("Initialized '" + dev.vini2003.hammer.core.HC.CONFIG_MODULE_ID + "' module.");
	}
}
