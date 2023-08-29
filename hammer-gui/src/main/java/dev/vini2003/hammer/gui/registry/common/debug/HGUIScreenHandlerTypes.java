package dev.vini2003.hammer.gui.registry.common.debug;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.gui.debug.common.screen.handler.DebugScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;

public class HGUIScreenHandlerTypes {
	public static ScreenHandlerType<DebugScreenHandler> DEBUG = Registry.register(
			Registries.SCREEN_HANDLER,
			HC.id("debug"),
			new ScreenHandlerType<>((syncId, playerInventory) -> new DebugScreenHandler(syncId, playerInventory.player), FeatureSet.empty())
	);
	
	public static void init() {
	
	}
}
