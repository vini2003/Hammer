package com.github.vini2003.blade.testing

import com.github.vini2003.blade.client.utilities.Instances
import com.github.vini2003.blade.common.data.Position
import com.github.vini2003.blade.common.data.Size
import com.github.vini2003.blade.common.handler.BaseScreenHandler
import com.github.vini2003.blade.common.widget.base.ButtonWidget
import com.github.vini2003.blade.common.widget.base.SlotWidget
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.ScreenHandlerType

class DebugScreenHandler(syncId: Int, player: PlayerEntity) : BaseScreenHandler(DebugContainers.DEBUG_HANDLER, syncId, player) {
    init {
        addInventory(0, player.inventory)

        val slot: SlotWidget = SlotWidget(0, 0)

        slot.setPosition(Position({16F}, {16F}))
        slot.setSize(Size({18F}, {18F}))

        val button: ButtonWidget = ButtonWidget {
            if (it.focused) print("I was clicked!")
        }

        button.setPosition(Position({32F}, {16F}))
        button.setSize(Size({80F}, {16F}))

        addWidget(slot)

        addWidget(button)
    }
}