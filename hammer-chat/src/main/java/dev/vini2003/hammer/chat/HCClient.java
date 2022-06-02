package dev.vini2003.hammer.chat;

import dev.vini2003.hammer.chat.registry.client.HCEvents;
import dev.vini2003.hammer.chat.registry.client.HCNetworking;
import net.fabricmc.api.ClientModInitializer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HCClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HCEvents.init();
		HCNetworking.init();
	}
}
