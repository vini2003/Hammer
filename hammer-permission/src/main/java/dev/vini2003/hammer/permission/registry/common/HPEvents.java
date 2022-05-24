package dev.vini2003.hammer.permission.registry.common;

import dev.vini2003.hammer.permission.api.common.manager.RoleManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class HPEvents {
	public static void init() {
		ServerPlayConnectionEvents.JOIN.register(new RoleManager.JoinListener());
	}
}
