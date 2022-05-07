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

package dev.vini2003.hammer.gui.api.common.util

import dev.vini2003.hammer.gui.api.common.widget.BaseWidgetCollection
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget
import dev.vini2003.hammer.core.api.common.math.position.Position
import dev.vini2003.hammer.core.api.common.math.position.PositionHolder
import dev.vini2003.hammer.core.api.common.math.size.Size
import net.minecraft.inventory.Inventory

object SlotUtils {
	@JvmStatic
	fun addPlayerInventory(position: PositionHolder, size: Size, collection: BaseWidgetCollection, inventory: Inventory): Collection<SlotWidget>? {
		val slots = addArray(position, size, collection, inventory, 9, 3, 9)
		
		slots.addAll(addArray(Position(position, 0.0F, size.height * 3.0F + 4.0F), size, collection, inventory, 9, 1, 0))
		
		return slots
	}
	
	@JvmStatic
	fun addArray(position: PositionHolder, size: Size, collection: BaseWidgetCollection, inventory: Inventory, arrayWidth: Int, arrayHeight: Int, slotNumber: Int): MutableCollection<SlotWidget> {
		val slots = mutableListOf<SlotWidget>()
		
		for (y in 0 until arrayHeight) {
			for (x in 0 until arrayWidth) {
				val slot = SlotWidget(slotNumber + y * arrayWidth + x, inventory)
				
				slot.position = Position(position, size.width * x, size.height * y)
				slot.size = Size(size)
				
				slots += slot
				
				collection += slot
			}
		}
		
		return slots
	}
}