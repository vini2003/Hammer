package dev.vini2003.hammer.adventure;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;

public class HA implements ModInitializer {
	private static FabricServerAudiences audiences;
	
	public static FabricServerAudiences getAudiences() {
		return audiences;
	}
	
	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> audiences = FabricServerAudiences.of(server));
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> audiences = null);
	}
}
