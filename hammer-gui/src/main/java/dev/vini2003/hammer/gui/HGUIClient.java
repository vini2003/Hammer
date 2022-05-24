package dev.vini2003.hammer.gui;

import dev.vini2003.hammer.gui.registry.client.HGUIEvents;
import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HGUIClient implements ModInitializer {
	@Override
	public void onInitialize() {
		HGUIEvents.init();
	}
}
