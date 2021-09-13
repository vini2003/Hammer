package dev.vini2003.hammer.common.util.extension

import dev.vini2003.hammer.common.widget.item.ItemWidget
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

var ItemWidget.item: Item
	get() = stack.item
	set(value) {
		stack = ItemStack(item)
	}