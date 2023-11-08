package dev.vini2003.hammer.core.registry.common;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.common.component.TrackedDataComponent;
import dev.vini2003.hammer.core.api.common.component.base.key.ComponentKey;
import dev.vini2003.hammer.core.api.common.manager.ComponentManager;
import net.minecraft.entity.player.PlayerEntity;

public class HCComponents {
	public static final ComponentKey<TrackedDataComponent> TRACKED_DATA = ComponentManager.registerKey(HC.id("tracked_data"));
	
	public static void init() {
		ComponentManager.registerForEntity(TRACKED_DATA, (entity) -> {
			if (entity instanceof PlayerEntity player) {
				return new TrackedDataComponent(player);
			}
			
			return null;
		});
	}
}
