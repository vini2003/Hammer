package dev.vini2003.hammer.adventure.registry.common;

import dev.vini2003.hammer.adventure.HA;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;

public class HAEvents {
	public static void init() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> HA.setAudiences(FabricServerAudiences.of(server)));
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> HA.setAudiences(null));
	}
}
