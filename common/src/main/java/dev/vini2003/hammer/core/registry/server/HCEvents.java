package dev.vini2003.hammer.core.registry.server;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;

public class HCEvents {
	public static void init() {
		LifecycleEvent.SERVER_STARTED.register((server) -> {
			InstanceUtil.setServerSupplier(() -> server);
		});
	}
}
