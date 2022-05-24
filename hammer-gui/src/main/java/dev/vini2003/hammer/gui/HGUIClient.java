package dev.vini2003.hammer.gui;

import dev.vini2003.hammer.gui.registry.client.HGUIEvents;
import net.fabricmc.api.ModInitializer;

public class HGUIClient implements ModInitializer {
	@Override
	public void onInitialize() {
		HGUIEvents.init();
	}
}
