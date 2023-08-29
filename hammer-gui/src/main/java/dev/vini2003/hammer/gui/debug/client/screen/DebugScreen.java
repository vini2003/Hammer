package dev.vini2003.hammer.gui.debug.client.screen;

import dev.vini2003.hammer.gui.api.client.screen.base.BaseHandledScreen;
import dev.vini2003.hammer.gui.debug.common.screen.handler.DebugScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class DebugScreen extends BaseHandledScreen<DebugScreenHandler> {
	public DebugScreen(DebugScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}
}
