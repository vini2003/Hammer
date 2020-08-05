package com.github.vini2003.blade.common.widget

import com.github.vini2003.blade.common.handler.BaseScreenHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory

interface OriginalWidgetCollection : WidgetCollection {
    fun getPlayer(): PlayerEntity

    fun getHandler(): BaseScreenHandler
}