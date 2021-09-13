package dev.vini2003.hammer.testing.common.screenhandler

import dev.vini2003.hammer.common.geometry.position.Position
import dev.vini2003.hammer.common.geometry.size.Size
import dev.vini2003.hammer.common.orientation.Orientation
import dev.vini2003.hammer.common.screen.handler.BaseScreenHandler
import dev.vini2003.hammer.common.util.extension.*
import dev.vini2003.hammer.registry.common.HScreenHandlers
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import kotlin.system.exitProcess

class DebugScreenHandler(syncId: Int, player: PlayerEntity) : BaseScreenHandler(HScreenHandlers.Debug, syncId, player) {
	override fun initialize(width: Int, height: Int) {
		panel {
			size = Size.of(180.0F, 168.0F)
			
			center()
			
			button {
				position = Position.of(9.0F, 9.0F) + parent!!.position
				size = Size.of(11.0F, 11.0F)
				
				label = "+".toLiteralText()
				
				clickAction = {
					exitProcess(1)
				}
			}
			
			button {
				position = Position.of(9.0F, 9.0F + 11.0F + 5F) + parent!!.position
				size = Size.of(11.0F, 11.0F)
				
				label = "-".toLiteralText()
			}
			
			button {
				position = Position.of(9.0F, 9.0F + 16.0F + 4.5F + 16.0F + 4.5F) + parent!!.position
				size = Size.of(36.0F, 16.0F)
				
				label = "OvO".toLiteralText()
			}
			
			fluidBar {
				position = Position.of(9.0F + 36.0F + 9.0F, 9.0F) + parent!!.position
				size = Size.of(18.0F, 48.0F)
				
				vertical = true
				
				fluid = Fluids.WATER
				
				current = { 0.5F }
			}
			
			fluidBar {
				position = Position.of(9.0F + 36.0F + 9.0F + 18.0F + 9.0F, 9.0F) + parent!!.position
				size = Size.of(48.0F, 18.0F)
			}
			
			playerInventory(this, player.inventory)
		}
	}

	override fun canUse(player: PlayerEntity?): Boolean = true
}