package dev.vini2003.hammer.interaction;

import dev.vini2003.hammer.interaction.registry.client.HINetworking;
import net.fabricmc.api.ClientModInitializer;

public class HIClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HINetworking.init();
	}
}
