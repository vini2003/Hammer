package dev.vini2003.hammer.gui.registry.client;

import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class HGUIEvents {
	public static void init() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player.currentScreenHandler instanceof BaseScreenHandler screenHandler) {
				screenHandler.tick();
			}
		});
	}
}
