package com.github.vini2003.blade

import com.github.vini2003.blade.common.utilities.Networks
import com.github.vini2003.blade.common.utilities.Styles
import com.github.vini2003.blade.testing.kotlin.DebugContainers
import com.github.vini2003.blade.testing.kotlin.DebugScreenHandler
import com.github.vini2003.blade.testing.kotlin.DebugScreens
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.network.PacketByteBuf
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

class Blade : ModInitializer {
    companion object {
        const val MOD_ID = "blade"

        val LOGGER = LogManager.getLogger(MOD_ID)

        @JvmStatic
        fun identifier(string: String): Identifier {
            return Identifier(MOD_ID, string)
        }
    }

    override fun onInitialize() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
            private val id: Identifier = identifier("reload_listener")

            override fun apply(manager: ResourceManager?) {
                Styles.clear()
                Styles.load(manager!!)
            }

            override fun getFabricId(): Identifier {
                return id
            }
        })

        DebugScreens.initialize()
        DebugContainers.initialize()

        Networks.initialize()

        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, dedicated ->
            val debugNode = CommandManager.literal("debug")
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