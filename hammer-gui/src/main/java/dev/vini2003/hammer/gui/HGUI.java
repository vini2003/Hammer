package dev.vini2003.hammer.gui;

import dev.vini2003.hammer.gui.registry.common.HGUIEvents;
import dev.vini2003.hammer.gui.registry.common.HGUINetworking;
import net.fabricmc.api.ModInitializer;

public class HGUI implements ModInitializer {
	@Override
	public void onInitialize() {
		HGUIEvents.init();
		HGUINetworking.init();
	}
}
