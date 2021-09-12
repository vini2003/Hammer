package dev.vini2003.blade.testing.common.screenhandler

import dev.vini2003.blade.common.geometry.position.Position
import dev.vini2003.blade.common.geometry.size.Size
import dev.vini2003.blade.common.handler.BaseScreenHandler
import dev.vini2003.blade.common.util.extension.*
import dev.vini2003.blade.common.widget.bar.VerticalFluidBarWidget
import dev.vini2003.blade.registry.common.BLScreenHandlers
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.item.Items

class DebugScreenHandler(syncId: Int, player: PlayerEntity) : BaseScreenHandler(BLScreenHandlers.Debug, syncId, player) {
	override fun initialize(width: Int, height: Int) {
		panel {
			size(180.0F, 168.0F)
			
			center()
			
			button {
				position(9.0F, 9.0F)
				size(36.0F, 16.0F)
				
				label("UwU")
			}
			
			button {
				position(9.0F, 9.0F + 16.0F + 4.5F)
				size(36.0F, 16.0F)
				
				label("OwO")
			}
			
			button {
				position(9.0F, 9.0F +16.0F + 4.5F + 16.0F + 4.5F)
				size(36.0F, 16.0F)
				
				label("OvO")
			}
			
			verticalFluidBar {
				position(9.0F + 36.0F + 9.0F, 9.0F)
				size(18.0F, 48.0F)
				
				fluid(Fluids.WATER)
				
				current { 50.0F }
			}
			
			playerInventory(this, player.inventory)
		}
	}

	override fun canUse(player: PlayerEntity?): Boolean = true
}