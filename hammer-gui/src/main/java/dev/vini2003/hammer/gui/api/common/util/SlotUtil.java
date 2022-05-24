package dev.vini2003.hammer.gui.api.common.util;

import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.Collection;

public class SlotUtil {
	public static Collection<SlotWidget> addPlayerInventory(Position position, Size size, WidgetCollection collection, Inventory inventory) {
		var slots = addArray(position, size, collection, inventory, 9, 3, 9);
		
		slots.addAll(addArray(new Position(position, 0.0F, size.getHeight() * 3.0F + 4.0F), size, collection, inventory, 9, 1, 0));
		
		return slots;
	}
	
	public static Collection<SlotWidget> addArray(Position position, Size size, WidgetCollection collection, Inventory inventory, int arrayWidth, int arrayHeight, int slotNumber) {
		var slots = new ArrayList<SlotWidget>();
		
		for (var y = 0; y < arrayHeight; ++y) {
			for (var x = 0; x < arrayWidth; ++x) {
				var slot = new SlotWidget(inventory, slotNumber + y * arrayWidth + x, Slot::new);
				
				slot.setPosition(new Position(position, size.getWidth() * x, size.getHeight() * y));
				slot.setSize(new Size(size));
				
				slots.add(slot);
				
				collection.add(slot);
			}
		}
		
		return slots;
	}
}
