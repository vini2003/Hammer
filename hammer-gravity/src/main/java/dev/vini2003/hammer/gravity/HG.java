package dev.vini2003.hammer.gravity;

import dev.vini2003.hammer.gravity.registry.common.HGCommands;
import dev.vini2003.hammer.gravity.registry.common.HGEvents;
import dev.vini2003.hammer.gravity.registry.common.HGNetworking;
import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HG implements ModInitializer {
	@Override
	public void onInitialize() {
		HGCommands.init();
		HGEvents.init();
		HGNetworking.init();
	}
}
