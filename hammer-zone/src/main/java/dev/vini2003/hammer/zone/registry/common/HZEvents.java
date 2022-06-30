package dev.vini2003.hammer.zone.registry.common;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.ArrayList;

public class HZEvents {
	public static void init() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (var world : server.getWorlds()) {
				HZComponents.ZONES.sync(world);
			}
		});
	}
}
