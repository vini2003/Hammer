package com.github.vini2003.blade

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

class Blade : ModInitializer {
    companion object {
        val MOD_ID = "blade"

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

        listOf(127, 48, 11, 36, 29, 15, 53, 17, 4, 14).forEach{
            println(it.toString() + ": " + Integer.toBinaryString(it))
        }

        listOf(0b101011, 0b10101, 0b11101111, 0b11101011, 0b11001111, 0b111000, 0b10101010, 0b110010, 0b1100101, 0b1110101).forEach {
            println(Integer.toBinaryString(it) + ": " + it)
        }

        val list = listOf(0b0, 0b0, 0b0, 0b0, 0b1, 0b1, 0b1, 0b1)
        val permutations = mutableSetOf<String>()

        for (i in 0 .. 128) {
            val shuffled = list.shuffled()

            val a = shuffled[0]
            val b = shuffled[1]
            val c = shuffled[2]
            val d = shuffled[3]

            if (permutations.contains("${a}${b}${c}${d}")) continue
            else permutations.add("${a}${b}${c}${d}")

            val x = if (a == 0b1 && b == 0b1) 0b1 else 0b0
            val y = if (x == 0b1 || c == 0b1) 0b1 else 0b0
            val z = if (d == 0b0) 0b1 else 0b0
            val s = if (y == 0b1 && z == 0b1) 0b1 else 0b0

            println("${a}, ${b}, ${c}, ${d} : X:${x}, Y:${y}, Z:${z}, S:${s}")
        }

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