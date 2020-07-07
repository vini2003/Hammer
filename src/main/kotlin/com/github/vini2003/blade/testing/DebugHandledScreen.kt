package com.github.vini2003.blade.testing

import com.github.vini2003.blade.client.handler.BaseHandledScreen
import com.github.vini2003.blade.common.handler.BaseScreenHandler
import com.github.vini2003.blade.common.widget.base.SlotWidget
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class DebugHandledScreen(handler: BaseScreenHandler, inventory: PlayerInventory, title: Text) : BaseHandledScreen<DebugScreenHandler>(handler, inventory, title) {
    init {
    }
}