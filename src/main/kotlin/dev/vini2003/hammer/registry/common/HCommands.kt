package dev.vini2003.hammer.registry.common

import com.mojang.brigadier.context.CommandContext
import dev.vini2003.hammer.testing.common.screenhandler.DebugScreenHandler
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText

class HCommands {
	companion object {
		@JvmStatic
		fun init() {
			CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, dedicated ->
				val debugNode = CommandManager.literal("dsh")
						.executes { context: CommandContext<ServerCommandSource> ->
							context.source.player.openHandledScreen(object : ExtendedScreenHandlerFactory {
								override fun writeScreenOpeningData(player: ServerPlayerEntity, buffer: PacketByteBuf) {
								}

								override fun getDisplayName(): Text {
									return TranslatableText("")
								}

								override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler? {
									return DebugScreenHandler(syncId, player)
								}
							})
							1
						}.build()

				dispatcher.root.addChild(debugNode)
			})
		}
	}
}