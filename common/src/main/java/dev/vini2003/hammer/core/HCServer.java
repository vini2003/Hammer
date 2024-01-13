package dev.vini2003.hammer.core;

import dev.vini2003.hammer.core.registry.server.HCEvents;
import net.fabricmc.api.DedicatedServerModInitializer;

public class HCServer implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		HCEvents.init();
		
		HC.LOGGER.info("Initialized '" + HC.CORE_MODULE_ID + "' server module.");
	}
}
