package com.github.vini2003.blade.common.widget.wrapper

import net.minecraft.inventory.Inventory
import net.minecraft.screen.slot.Slot

class SafeSlot(inventory: Inventory, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y) {
	var canInsert = true
}