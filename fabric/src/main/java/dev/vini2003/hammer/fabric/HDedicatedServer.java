package dev.vini2003.hammer.fabric;

import dev.vini2003.hammer.core.HCDedicatedServer;
import net.fabricmc.api.DedicatedServerModInitializer;

public class HDedicatedServer implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		HCDedicatedServer.onInitializeDedicatedServer();
	}
}
