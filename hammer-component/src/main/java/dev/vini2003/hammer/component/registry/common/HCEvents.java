package dev.vini2003.hammer.component.registry.common;

import dev.vini2003.hammer.component.api.common.component.PlayerComponent;
import dev.vini2003.hammer.component.impl.common.component.holder.ComponentHolder;
import dev.vini2003.hammer.component.impl.common.state.ComponentPersistentState;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

public class HCEvents {
	private static final String HAMMER$COMPONENTS_KEY = "Hammer$Components";
	
	public static void init() {
		ServerWorldEvents.LOAD.register((server, world) -> {
			world.getPersistentStateManager().getOrCreate(nbt -> new ComponentPersistentState(world, nbt), () -> new ComponentPersistentState(world), HAMMER$COMPONENTS_KEY);
		});
		
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			if (oldPlayer instanceof ComponentHolder oldHolder) {
				if (newPlayer instanceof ComponentHolder newHolder) {
					for (var entry : oldHolder.getComponentContainer().entrySet()) {
						var key = entry.getKey();
						var component = entry.getValue();
						
						if (component instanceof PlayerComponent playerComp) {
							if (alive || (!alive && playerComp.shouldCopyOnDeath())) {
								newHolder.getComponentContainer().put(key, component);
							}
						} else {
							if (alive) {
								newHolder.getComponentContainer().put(key, component);
							}
						}
					}
				}
			}
		});
	}
}
