package com.github.vini2003.blade.testing.kotlin

import com.github.vini2003.blade.common.data.Position
import com.github.vini2003.blade.common.data.Size
import com.github.vini2003.blade.common.handler.BaseScreenHandler
import com.github.vini2003.blade.common.widget.base.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.registry.Registry

class DebugScreenHandler(syncId: Int, player: PlayerEntity) : BaseScreenHandler(DebugContainers.DEBUG_HANDLER, syncId, player) {
    override fun initialize(width: Int, height: Int) {
        val player = getPlayer()

// addInventory(0, player.inventory)

 val slot = SlotWidget(0, player.inventory)

 slot.position = (Position({16F}, {16F}))
 slot.size = (Size({36F}, {36F}))

 val topButton = ButtonWidget {
 }

 topButton.position = (Position({24F}, {70F}))
 topButton.size = (Size({120F}, {16F}))
 topButton.label = LiteralText("I am NinePatch,")

 val bottomButton = ButtonWidget {
 }

 bottomButton.position = (Position({24F}, {70F}))
 bottomButton.size = (Size({160F}, {16F}))
 bottomButton.label = LiteralText("or something.")

// val item = ItemWidget()

// item.position = (Position({256F}, {16F}))
// item.size = (Size({16F}, {16F}))
// item.stack = ItemStack(Items.RED_WOOL)

// val inventory = SimpleInventory(4096)
// for (i in 0 until inventory.size()) inventory.setStack(i, ItemStack(Registry.ITEM.getRandom(player.world.random), player.world.random.nextInt(64)))

////val slots = SlotListWidget(inventory)

////slots.position = (Position({slot.position.x}, {slot.position.y + slot.size.width + 2}))
////slots.size = (Size({17 * 18F}, {11 * 18F}))

// val panel = PanelWidget()
// panel.size = Size({128F}, {128F})
// panel.position = Position({0F}, {0F})

// val bar = BarWidget(false, {100F}, {75F})
// bar.size = Size({128F}, {128F})
// bar.position = Position({ 64F }, { 64F })

        val tabs = TabWidget()
        tabs.size = Size({259F}, {128F})
        tabs.position = Position({18F}, {18F})

        val firstTab = tabs.addTab(Items.RED_CONCRETE)

        for (i in 1..8) {
            tabs.addTab(Registry.ITEM.getRandom(player.world.random)) {
                listOf<Text>(LiteralText(i.toString()))
            }
        }

        tabs.addTab(Items.BLUE_CONCRETE).also {
            it.addWidget(topButton)
        }

        addWidget(tabs)

        firstTab.also {
            it.addWidget(bottomButton)
            it.addWidget(slot)
        }

        //addWidget(panel)

      // addWidget(slot)

       // addWidget(topButton)
      //  addWidget(bottomButton)

       // addWidget(item)

     //   addWidget(slots)

      //  addWidget(bar)
    }
}