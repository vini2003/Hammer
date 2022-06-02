package dev.vini2003.hammer.gui;

import dev.vini2003.hammer.gui.registry.client.HGUIEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HGUIClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HGUIEvents.init();
	}
}
