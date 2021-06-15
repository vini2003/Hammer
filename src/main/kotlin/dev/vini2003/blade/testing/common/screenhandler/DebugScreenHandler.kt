package dev.vini2003.blade.testing.common.screenhandler

import dev.vini2003.blade.common.handler.BaseScreenHandler
import dev.vini2003.blade.registry.common.BLScreenHandlers
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items

class DebugScreenHandler(syncId: Int, player: PlayerEntity) : BaseScreenHandler(BLScreenHandlers.Debug, syncId, player) {
	override fun initialize(width: Int, height: Int) {
		panel {
			position(48F, 48F)
			size(96F, 96F)
			
			button {
				position(54.0F, 54.0F)
				size(36.0F, 20.0F)
				
				label("UwU")
			}
			
			item {
				position(54.0F, 54.0F + 20.0F + 9.0F)
				size(18.0F, 18.0F)
				
				item(Items.EMERALD)
			}
		}
	}

	override fun canUse(player: PlayerEntity?): Boolean = true
}