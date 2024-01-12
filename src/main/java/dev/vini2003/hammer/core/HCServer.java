package dev.vini2003.hammer.core;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.registry.server.HCEvents;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

public class HCServer implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		HCEvents.init();
		
		HC.LOGGER.info("Initialized '" + HC.CORE_MODULE_ID + "' server module.");
	}
}
