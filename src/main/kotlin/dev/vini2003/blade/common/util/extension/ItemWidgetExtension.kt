package dev.vini2003.blade.common.util.extension

import dev.vini2003.blade.common.widget.item.ItemWidget
import net.minecraft.item.Item
import net.minecraft.item.ItemStack

fun ItemWidget.item(item: Item) {
	this.stack = ItemStack(item)
}

fun ItemWidget.stack(stack: ItemStack) {
	this.stack = stack
}