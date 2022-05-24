package dev.vini2003.hammer.util.registry;

import dev.vini2003.hammer.util.registry.common.HUEvents;
import dev.vini2003.hammer.util.registry.common.HUNetworking;
import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HU implements ModInitializer {
	@Override
	public void onInitialize() {
		HUEvents.init();
		HUNetworking.init();
	}
}
