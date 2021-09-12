package dev.vini2003.blade.testing.client.screen

import dev.vini2003.blade.client.screen.BaseHandledScreen
import dev.vini2003.blade.common.handler.BaseScreenHandler
import dev.vini2003.blade.testing.common.screenhandler.DebugScreenHandler
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class DebugHandledScreen(handler: BaseScreenHandler, inventory: PlayerInventory, title: Text) : BaseHandledScreen<DebugScreenHandler>(handler, inventory, title) {

}