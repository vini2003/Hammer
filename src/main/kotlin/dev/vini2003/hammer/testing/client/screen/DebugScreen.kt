package dev.vini2003.hammer.testing.client.screen

import dev.vini2003.hammer.client.screen.BaseScreen
import dev.vini2003.hammer.common.geometry.position.Position
import dev.vini2003.hammer.common.geometry.size.Size
import dev.vini2003.hammer.common.util.extension.*
import net.minecraft.item.Items

class DebugScreen : BaseScreen(null) {
	override fun initialize(width: Int, height: Int) {
		panel {
			position = Position.of(48F, 48F)
			size = Size.of(96F, 96F)
			
			button {
				position = Position.of(54.0F, 54.0F)
				size = Size.of(36.0F, 20.0F)
				
				label = "UwU".toLiteralText()
			}
			
			item {
				position = Position.of(54.0F, 54.0F + 20.0F + 9.0F)
				size = Size.of(18.0F, 18.0F)
				
				item = Items.EMERALD
			}
		}
	}
}