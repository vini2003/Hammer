package dev.vini2003.hammer.gui.registry.common;

import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class HGUIEvents {
	public static void init() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (var player : server.getPlayerManager().getPlayerList()) {
				if (player.currentScreenHandler instanceof BaseScreenHandler screenHandler) {
					screenHandler.tick();
				}
			}
		});
	}
}
