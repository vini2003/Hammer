package dev.vini2003.hammer.permission;

import dev.vini2003.hammer.permission.registry.client.HPNetworking;
import net.fabricmc.api.ClientModInitializer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HPClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HPNetworking.init();
	}
}
