/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.gui.api.common.util;

import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.position.StaticPosition;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.Collection;

public class SlotUtil {
	public static Collection<SlotWidget> addPlayerInventory(StaticPosition position, Size size, WidgetCollection collection, Inventory inventory) {
		var slots = addArray(position, size, collection, inventory, 9, 3, 9);
		
		slots.addAll(addArray(Position.of(position, 0.0F, size.getHeight() * 3.0F + 4.0F), size, collection, inventory, 9, 1, 0));
		
		return slots;
	}
	
	public static Collection<SlotWidget> addArray(StaticPosition position, Size size, WidgetCollection collection, Inventory inventory, int arrayWidth, int arrayHeight, int slotNumber) {
		var slots = new ArrayList<SlotWidget>();
		
		for (var y = 0; y < arrayHeight; ++y) {
			for (var x = 0; x < arrayWidth; ++x) {
				var slot = new SlotWidget(inventory, slotNumber + y * arrayWidth + x, Slot::new);
				
				slot.setPosition(Position.of(position, size.getWidth() * x, size.getHeight() * y));
				slot.setSize(Size.of(size));
				
				slots.add(slot);
				
				collection.add(slot);
			}
		}
		
		return slots;
	}
}
