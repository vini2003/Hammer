package dev.vini2003.hammer.core.api.common.supplier;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

@FunctionalInterface
public interface SlotSupplier {
	Slot get(Inventory inventory, int index, int x, int y);
}
