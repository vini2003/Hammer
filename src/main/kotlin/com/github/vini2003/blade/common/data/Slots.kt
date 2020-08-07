package com.github.vini2003.blade.common.data

import com.github.vini2003.blade.common.widget.WidgetCollection
import com.github.vini2003.blade.common.widget.base.AbstractWidget
import com.github.vini2003.blade.common.widget.base.SlotWidget
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.inventory.Inventory
import java.util.*

class Slots {
	companion object {
		@JvmStatic
		fun addPlayerInventory(position: Position, size: Size, parent: AbstractWidget, inventory: Inventory): Collection<SlotWidget>? {
			val set: MutableCollection<SlotWidget> = addArray(position, size, parent, 9, 9, 3, inventory)
			set.addAll(addArray(Position({position.x}, {position.y + size.height * 3 + 4}), size, parent, 0, 9, 1, inventory))
			return set
		}

		@JvmStatic
		fun addArray(position: Position, size: Size, parent: AbstractWidget, slotNumber: Int, arrayWidth: Int, arrayHeight: Int, inventory: Inventory): MutableCollection<SlotWidget> {
			val set: MutableCollection<SlotWidget> = HashSet()
			for (y in 0 until arrayHeight) {
				for (x in 0 until arrayWidth) {
					val slot = SlotWidget(slotNumber + y * arrayWidth + x, inventory)
					slot.position = Position({position.x + size.width * x}, {position.y + size.height * y})
					slot.size = Size({size.width}, {size.height})

					(parent as WidgetCollection).also {
						it.addWidget(slot)
					}
				}
			}
			return set
		}
	}
}