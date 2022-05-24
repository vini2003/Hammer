package dev.vini2003.hammer.gui;

import dev.vini2003.hammer.gui.registry.common.HGUIEvents;
import dev.vini2003.hammer.gui.registry.common.HGUINetworking;
import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HGUI implements ModInitializer {
	@Override
	public void onInitialize() {
		HGUIEvents.init();
		HGUINetworking.init();
	}
}
