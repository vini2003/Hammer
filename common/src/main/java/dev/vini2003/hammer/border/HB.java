package dev.vini2003.hammer.border;

import dev.vini2003.hammer.core.HC;
import net.fabricmc.api.ModInitializer;

public class HB {
	public static void onInitialize() {
		HC.LOGGER.info("Initialized '" + HC.BORDER_MODULE_ID + "' module.");
	}
}
