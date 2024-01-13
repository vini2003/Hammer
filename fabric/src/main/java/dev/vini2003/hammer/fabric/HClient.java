package dev.vini2003.hammer.fabric;

import dev.vini2003.hammer.core.HCClient;
import net.fabricmc.api.ClientModInitializer;

public class HClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HCClient.onInitializeClient();
	}
}
