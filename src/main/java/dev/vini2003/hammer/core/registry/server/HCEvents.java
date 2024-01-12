package dev.vini2003.hammer.core.registry.server;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

public class HCEvents {
	public static void init() {
		ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
			InstanceUtil.setServerSupplier(() -> (MinecraftServer) FabricLoader.getInstance().getGameInstance());
		});
	}
}
