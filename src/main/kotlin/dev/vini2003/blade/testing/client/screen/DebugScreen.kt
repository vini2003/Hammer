package dev.vini2003.blade.testing.client.screen

import dev.vini2003.blade.client.handler.BaseScreen
import net.minecraft.item.Items

class DebugScreen : BaseScreen(null) {
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
}