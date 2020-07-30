package com.github.vini2003.blade.testing.kotlin

import com.github.vini2003.blade.common.data.Position
import com.github.vini2003.blade.common.data.Size
import com.github.vini2003.blade.common.handler.BaseScreenHandler
import com.github.vini2003.blade.common.widget.base.ItemWidget
import com.github.vini2003.blade.common.widget.base.SlotListWidget
import com.github.vini2003.blade.common.widget.base.SlotWidget
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

class DebugScreenHandler(syncId: Int, player: PlayerEntity) : BaseScreenHandler(DebugContainers.DEBUG_HANDLER, syncId, player) {
    init {
        addInventory(0, player.inventory)

        val slot = SlotWidget(0, player.inventory)

        slot.setPosition(Position({16F}, {16F}))
        slot.setSize(Size({18F}, {18F}))

        //val button = ButtonWidget {
        //    if (it.focused) print("I was clicked!")
        //}

        //button.setPosition(Position({32F}, {16F}))
        //button.setSize(Size({80F}, {16F}))

        val item = ItemWidget()

        item.setPosition(Position({256F}, {16F}))
        item.setSize(Size({16F}, {16F}))
        item.stack = ItemStack(Items.RED_WOOL)

        val slots = SlotListWidget(1, 1, 36, player.inventory)

        slots.setPosition(Position({32F}, {32F}))
        slots.setSize(Size({1 * 18F}, {1 * 18F}))

        addWidget(slot)

      //  addWidget(button)

        addWidget(item)

        addWidget(slots)
    }
}