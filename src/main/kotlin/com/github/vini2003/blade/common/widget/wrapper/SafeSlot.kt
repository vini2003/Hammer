package com.github.vini2003.blade.common.widget.wrapper

import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot

class SafeSlot(inventory: Inventory, index: Int, x: Int, y: Int) : Slot(inventory, index, x, y) {
    var canInsert = true

    override fun getStack(): ItemStack {
        return if (inventory.size() >= index) return super.getStack() else return ItemStack.EMPTY
    }

    override fun setStack(stack: ItemStack?) {
        if (inventory.size() >= index) super.setStack(stack)
    }

    override fun takeStack(amount: Int): ItemStack {
        return if (inventory.size() >= index) return super.takeStack(amount) else ItemStack.EMPTY
    }

    override fun canInsert(stack: ItemStack?): Boolean {
        return (inventory.size() >= index) && canInsert && super.canInsert(stack)
    }
}