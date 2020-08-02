package com.github.vini2003.blade.testing.kotlin

import com.github.vini2003.blade.common.data.Position
import com.github.vini2003.blade.common.data.Size
import com.github.vini2003.blade.common.handler.BaseScreenHandler
import com.github.vini2003.blade.common.widget.base.ButtonWidget
import com.github.vini2003.blade.common.widget.base.ItemWidget
import com.github.vini2003.blade.common.widget.base.SlotListWidget
import com.github.vini2003.blade.common.widget.base.SlotWidget
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.LiteralText
import net.minecraft.util.registry.Registry

class DebugScreenHandler(syncId: Int, player: PlayerEntity) : BaseScreenHandler(DebugContainers.DEBUG_HANDLER, syncId, player) {
    init {
        addInventory(0, player.inventory)

        val slot = SlotWidget(0, player.inventory)

        slot.setPosition(Position({16F}, {16F}))
        slot.setSize(Size({36F}, {36F}))

        val topButton = ButtonWidget {
        }

        topButton.setPosition(Position({slot.getPosition().x + slot.getSize().width + 18F}, {slot.getPosition().y}))
        topButton.setSize(Size({120F}, {16F}))
        topButton.label = LiteralText("I am NinePatch,")

        val bottomButton = ButtonWidget {
        }

        bottomButton.setPosition(Position({slot.getPosition().x + slot.getSize().width + 18F}, {topButton.getPosition().y + topButton.getSize().height + 1}))
        bottomButton.setSize(Size({160F}, {16F}))
        bottomButton.label = LiteralText("or something.")

        val item = ItemWidget()

        item.setPosition(Position({256F}, {16F}))
        item.setSize(Size({16F}, {16F}))
        item.stack = ItemStack(Items.RED_WOOL)

        val inv = SimpleInventory(1024)
        for (i in 0 until inv.size()) inv.setStack(i, ItemStack(Registry.ITEM.getRandom(player.world.random), player.world.random.nextInt(Integer.MAX_VALUE)))

        val slots = SlotListWidget(16, 12, 1024, inv)

        slots.setPosition(Position({slot.getPosition().x}, {slot.getPosition().y + slot.getSize().width + 2}))
        slots.setSize(Size({17 * 18F}, {12 * 18F}))

        addWidget(slot)

        addWidget(topButton)
        addWidget(bottomButton)

        addWidget(item)

        addWidget(slots)
    }
}