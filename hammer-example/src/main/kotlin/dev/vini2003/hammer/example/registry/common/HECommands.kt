package dev.vini2003.hammer.example.registry.common

import dev.vini2003.hammer.command.api.common.util.extension.command
import dev.vini2003.hammer.command.api.common.util.extension.execute
import dev.vini2003.hammer.core.api.common.util.TextUtils
import dev.vini2003.hammer.example.impl.common.screen.handler.DebugScreenHandler
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object HECommands {
	fun init() {
		CommandRegistrationCallback.EVENT.register { dispatcher, dedicated ->
			dispatcher.command("debug_screen_handler") {
				execute {
					source.player.openHandledScreen(object : ExtendedScreenHandlerFactory {
						override fun createMenu(syncId: Int, playerInventory: PlayerInventory, playerEntity: PlayerEntity): ScreenHandler {
							return DebugScreenHandler(syncId, playerEntity)
						}
						
						override fun getDisplayName(): Text {
							return TextUtils.EMPTY
						}
						
						override fun writeScreenOpeningData(player: ServerPlayerEntity, buf: PacketByteBuf) {
						
						}
					})
				}
			}
		}
	}
}