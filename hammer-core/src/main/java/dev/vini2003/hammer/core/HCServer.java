package dev.vini2003.hammer.core;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

public class HCServer implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
			InstanceUtil.setServerSupplier(() -> (MinecraftServer) FabricLoader.getInstance().getGameInstance());
		});
	}
}
