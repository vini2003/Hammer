package com.github.vini2003.blade.common.handler

import com.github.vini2003.blade.common.widget.OriginalWidgetCollection
import com.github.vini2003.blade.common.widget.base.AbstractWidget
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType

open class BaseScreenHandler(type: ScreenHandlerType<out ScreenHandler>, syncId: Int, private val player: PlayerEntity) : ScreenHandler(type, syncId), OriginalWidgetCollection {
    private val widgets: MutableList<AbstractWidget> = mutableListOf()

    private val inventories = mutableMapOf<Int, Inventory>()

    override fun getWidgets(): Collection<AbstractWidget> {
        return widgets
    }

    override fun addWidget(widget: AbstractWidget) {
        widgets.add(widget)
        widget.onAdded(this, this)

        widgets.forEach{
            widget.onLayoutChanged()
        }
    }

    override fun removeWidget(widget: AbstractWidget) {
        widgets.remove(widget)
        widget.onRemoved(this, this)

        widgets.forEach{
            widget.onLayoutChanged()
        }
    }

    override fun getInventory(inventoryNumber: Int): Inventory? {
        return inventories[inventoryNumber]
    }

    override fun addInventory(inventoryNumber: Int, inventory: Inventory) {
        inventories.put(inventoryNumber, inventory)
    }

    override fun getPlayer(): PlayerEntity {
        return player
    }

    override fun canUse(player: PlayerEntity?): Boolean {
        return true
    }
}