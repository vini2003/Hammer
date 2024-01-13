package dev.vini2003.hammer.border;

import dev.vini2003.hammer.core.HC;
import net.fabricmc.api.ModInitializer;

public class HB implements ModInitializer {
	@Override
	public void onInitialize() {
		HC.LOGGER.info("Initialized '" + HC.BORDER_MODULE_ID + "' module.");
	}
}
