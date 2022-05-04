package dev.vini2003.hammer.gui.common.util

import dev.vini2003.hammer.gui.common.widget.WidgetCollection
import dev.vini2003.hammer.gui.common.widget.slot.SlotWidget
import dev.vini2003.hammer.common.geometry.position.Position
import dev.vini2003.hammer.common.geometry.position.PositionHolder
import dev.vini2003.hammer.common.geometry.size.Size
import net.minecraft.inventory.Inventory

object SlotUtils {
	@JvmStatic
	fun addPlayerInventory(position: PositionHolder, size: Size, parent: WidgetCollection, inventory: Inventory): Collection<SlotWidget>? {
		val set: MutableCollection<SlotWidget> = addArray(position, size, parent, 9, 9, 3, inventory)
		set.addAll(addArray(Position.of(position, 0, size.height * 3 + 4), size, parent, 0, 9, 1, inventory))
		
		return set
	}
	
	@JvmStatic
	fun addArray(position: PositionHolder, size: Size, parent: WidgetCollection, slotNumber: Int, arrayWidth: Int, arrayHeight: Int, inventory: Inventory): MutableCollection<SlotWidget> {
		val set: MutableCollection<SlotWidget> = HashSet()
		
		for (y in 0 until arrayHeight) {
			for (x in 0 until arrayWidth) {
				val slot = SlotWidget(slotNumber + y * arrayWidth + x, inventory)
				slot.position = Position.of(position, size.width * x, size.height * y)
				slot.size = Size.of(size)
				
				set += slot
				
				parent += slot
			}
		}
		
		return set
	}
}