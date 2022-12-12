package dev.vini2003.hammer.stage.registry.common;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.common.component.base.key.ComponentKey;
import dev.vini2003.hammer.core.api.common.manager.ComponentManager;
import dev.vini2003.hammer.stage.impl.common.component.GridComponent;

public class HSComponents {
	public static final ComponentKey<GridComponent> GRID = ComponentManager.registerKey(HC.id("grid"));
	
	public static void init() {
		ComponentManager.registerForWorld(GRID, GridComponent::new);
	}
}
