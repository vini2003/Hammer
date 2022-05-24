package dev.vini2003.hammer.gravity;

import dev.vini2003.hammer.gravity.registry.client.HGNetworking;
import net.fabricmc.api.ClientModInitializer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HGClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HGNetworking.init();
	}
}
