package dev.vini2003.hammer.stage.registry.common;

import dev.vini2003.hammer.stage.api.common.manager.StageManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class HSEvents {
	public static void init() {
		ServerTickEvents.START_WORLD_TICK.register(world -> {
			var active = StageManager.getActive(world.getRegistryKey());
			
			if (active != null) {
				active.tick(world);
			}
		});
	}
}
