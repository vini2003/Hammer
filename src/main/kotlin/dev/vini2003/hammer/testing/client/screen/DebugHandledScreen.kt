package dev.vini2003.hammer.testing.client.screen

import dev.vini2003.hammer.client.screen.BaseHandledScreen
import dev.vini2003.hammer.common.screen.handler.BaseScreenHandler
import dev.vini2003.hammer.testing.common.screenhandler.DebugScreenHandler
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class DebugHandledScreen(handler: BaseScreenHandler, inventory: PlayerInventory, title: Text) : BaseHandledScreen<DebugScreenHandler>(handler, inventory, title) {

}