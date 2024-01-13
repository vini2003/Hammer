package dev.vini2003.hammer.core.registry.server;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

public class HCEvents {
	public static void init() {
		LifecycleEvent.SERVER_STARTED.register((server) -> {
			InstanceUtil.setServerSupplier(() -> (MinecraftServer) FabricLoader.getInstance().getGameInstance());
		});
	}
}
