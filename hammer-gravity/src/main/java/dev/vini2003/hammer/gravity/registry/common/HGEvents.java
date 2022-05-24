package dev.vini2003.hammer.gravity.registry.common;

import dev.vini2003.hammer.gravity.api.common.manager.GravityManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class HGEvents {
	public static void init() {
		ServerPlayConnectionEvents.JOIN.register(new GravityManager.JoinListener());
	}
}
