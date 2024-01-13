package dev.vini2003.hammer.core;

import dev.vini2003.hammer.core.registry.server.HCEvents;

public class HCDedicatedServer {
	public static void onInitializeDedicatedServer() {
		HCEvents.init();
		
		HC.LOGGER.info("Initialized '" + HC.CORE_MODULE_ID + "' server module.");
	}
}
