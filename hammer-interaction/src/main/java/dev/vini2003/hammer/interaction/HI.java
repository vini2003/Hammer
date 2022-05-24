package dev.vini2003.hammer.interaction;

import dev.vini2003.hammer.interaction.registry.common.HIEvents;
import dev.vini2003.hammer.interaction.registry.common.HINetworking;
import net.fabricmc.api.ModInitializer;

public class HI implements ModInitializer {
	@Override
	public void onInitialize() {
		HIEvents.init();
		HINetworking.init();
	}
}
