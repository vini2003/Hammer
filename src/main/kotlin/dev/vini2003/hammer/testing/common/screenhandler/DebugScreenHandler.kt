package dev.vini2003.hammer.testing.common.screenhandler

import dev.vini2003.hammer.common.geometry.position.Position
import dev.vini2003.hammer.common.geometry.size.Size
import dev.vini2003.hammer.common.screen.handler.BaseScreenHandler
import dev.vini2003.hammer.common.util.extension.center
import dev.vini2003.hammer.common.util.extension.toLiteralText
import dev.vini2003.hammer.common.widget.bar.FluidBarWidget
import dev.vini2003.hammer.registry.common.HScreenHandlers
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.fluid.Fluids
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import kotlin.math.max

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
					widgets.find { it is FluidBarWidget }?.let { it as FluidBarWidget }?.apply {
						val next = max(0.0F, current() + 0.125F)
						current = { next }
					}
				}
			}
			
			button {
				position = Position.of(9.0F, 9.0F + 11.0F + 5F) + parent!!.position
				size = Size.of(11.0F, 11.0F)
				
				label = "-".toLiteralText()
				
				clickAction = {
					widgets.find { it is FluidBarWidget }?.let { it as FluidBarWidget }?.apply {
						val next = max(0.0F, current() - 0.125F)
						current = { next }
					}
				}
			}
			
			fluidBar {
				position = Position.of(9.0F + 11.0F + 5.0F, 9.0F) + parent!!.position
				size = Size.of(18.0F, 48.0F)
				
				vertical = true
				
				fluid = Fluids.WATER
				
				current = { 0.5F }
			}
			
			playerInventory(this, player.inventory)
		}
	}

	override fun canUse(player: PlayerEntity?): Boolean = true
	
	object Factory : ExtendedScreenHandlerFactory {
		override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): ScreenHandler {
			return DebugScreenHandler(syncId, player)
		}
		
		override fun getDisplayName(): Text {
			return "".toLiteralText()
		}
		
		override fun writeScreenOpeningData(player: ServerPlayerEntity?, buf: PacketByteBuf?) {
		
		}
	}
}