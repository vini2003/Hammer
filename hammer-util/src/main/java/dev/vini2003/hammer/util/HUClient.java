package dev.vini2003.hammer.util;

import dev.vini2003.hammer.util.registry.client.HUNetworking;
import net.fabricmc.api.ClientModInitializer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HUClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HUNetworking.init();
	}
}
