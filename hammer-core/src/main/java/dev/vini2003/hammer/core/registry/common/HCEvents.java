package dev.vini2003.hammer.core.registry.common;

import dev.vini2003.hammer.core.api.common.util.PlayerUtil;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public class HCEvents {
	public static void init() {
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			PlayerUtil.setFrozen(newPlayer, PlayerUtil.isFrozen(oldPlayer));
		});
	}
}
