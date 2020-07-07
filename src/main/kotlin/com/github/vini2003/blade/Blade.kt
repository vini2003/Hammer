package com.github.vini2003.blade

import com.github.vini2003.blade.testing.DebugContainers
import com.github.vini2003.blade.testing.DebugScreenHandler
import com.github.vini2003.blade.testing.DebugScreens
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry
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
import net.minecraft.util.Identifier

class Blade : ModInitializer {
    companion object {
        val MOD_ID = "blade"

        @JvmStatic
        fun identifier(string: String): Identifier {
            return Identifier(MOD_ID, string)
        }
    }

    override fun onInitialize() {
        DebugScreens.initialize()
        DebugContainers.initialize()
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, dedicated ->
            val debugNode = CommandManager.literal("debug")
                .executes { context: CommandContext<ServerCommandSource> ->
                    context.source.player.openHandledScreen(object : ExtendedScreenHandlerFactory {
                        override fun writeScreenOpeningData(player: ServerPlayerEntity, buffer: PacketByteBuf) {
                        }

                        override fun getDisplayName(): Text {
                            return TranslatableText("")
                        }

                        override fun createMenu(
                            syncId: Int,
                            playerInventory: PlayerInventory,
                            player: PlayerEntity
                        ): ScreenHandler? {
                            return DebugScreenHandler(syncId, player)
                        }
                    })
                    1
                }.build()

            dispatcher.root.addChild(debugNode)
        })
    }
}