package dev.vini2003.hammer.interaction;

import dev.vini2003.hammer.interaction.registry.client.HINetworking;
import net.fabricmc.api.ClientModInitializer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HIClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HINetworking.init();
	}
}
