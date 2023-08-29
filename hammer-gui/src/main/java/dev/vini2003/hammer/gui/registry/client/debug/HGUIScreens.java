package dev.vini2003.hammer.gui.registry.client.debug;

import dev.vini2003.hammer.gui.debug.client.screen.DebugScreen;
import dev.vini2003.hammer.gui.registry.common.debug.HGUIScreenHandlerTypes;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class HGUIScreens {
	public static void init() {
		HandledScreens.register(HGUIScreenHandlerTypes.DEBUG, DebugScreen::new);
	}
}
