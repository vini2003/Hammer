package dev.vini2003.hammer.zone.registry.common;

import dev.vini2003.hammer.component.api.common.component.key.ComponentKey;
import dev.vini2003.hammer.component.api.common.manager.ComponentManager;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.zone.impl.common.component.ZoneComponent;

public class HZComponents {
	public static final ComponentKey<ZoneComponent> ZONES = ComponentManager.registerKey(HC.id("zones"));
	
	public static void init() {
		ComponentManager.registerForWorld(ZONES, ZoneComponent::new);
	}
}
