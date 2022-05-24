package dev.vini2003.hammer.gravity;

import dev.vini2003.hammer.gravity.registry.common.HGCommands;
import dev.vini2003.hammer.gravity.registry.common.HGEvents;
import dev.vini2003.hammer.gravity.registry.common.HGNetworking;
import net.fabricmc.api.ModInitializer;

public class HG implements ModInitializer {
	@Override
	public void onInitialize() {
		HGCommands.init();
		HGEvents.init();
		HGNetworking.init();
	}
}
