package dev.vini2003.hammer.example.impl.client.screen.handled

import dev.vini2003.hammer.example.impl.common.screen.handler.DebugScreenHandler
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

class DebugHandledScreen(handler: BaseScreenHandler, inventory: PlayerInventory, title: Text) : BaseHandledScreen<DebugScreenHandler>(handler, inventory, title) {

}