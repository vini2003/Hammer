package dev.vini2003.hammer.fabric;

import dev.vini2003.hammer.core.HC;
import net.fabricmc.api.ModInitializer;

public class H implements ModInitializer {
	@Override
	public void onInitialize() {
		HC.onInitialize();
	}
}
