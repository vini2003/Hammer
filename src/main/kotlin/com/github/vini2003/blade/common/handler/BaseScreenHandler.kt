package com.github.vini2003.blade.common.handler

import com.github.vini2003.blade.common.widget.OriginalWidgetCollection
import com.github.vini2003.blade.common.widget.base.AbstractWidget
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.screen.slot.SlotActionType

open class BaseScreenHandler(type: ScreenHandlerType<out ScreenHandler>, syncId: Int, private val player: PlayerEntity) : ScreenHandler(type, syncId), OriginalWidgetCollection {
    private val widgets: MutableList<AbstractWidget> = mutableListOf()

    private val inventories = mutableMapOf<Int, Inventory>()

    override fun getWidgets(): Collection<AbstractWidget> {
        return widgets
    }

    override fun addWidget(widget: AbstractWidget) {
        widgets.add(widget)
        widget.onAdded(this, this)

        widgets.forEach{ _ ->
            widget.onLayoutChanged()
        }
    }

    override fun removeWidget(widget: AbstractWidget) {
        widgets.remove(widget)
        widget.onRemoved(this, this)

        widgets.forEach{ _ ->
            widget.onLayoutChanged()
        }
    }

    override fun getInventory(inventoryNumber: Int): Inventory? {
        return inventories[inventoryNumber]
    }

    override fun addInventory(inventoryNumber: Int, inventory: Inventory) {
        inventories[inventoryNumber] = inventory
    }

    override fun getPlayer(): PlayerEntity {
        return player
    }

    override fun canUse(player: PlayerEntity?): Boolean {
        return true
    }

    public override fun addSlot(slot: Slot?): Slot {
        return super.addSlot(slot)
    }

    fun removeSlot(slot: Slot?) {
        val id = slot!!.id
        slots.remove(slot)
        slots.forEach {
            if (it.id >= id) {
                --it.id
            }
        }
    }
}