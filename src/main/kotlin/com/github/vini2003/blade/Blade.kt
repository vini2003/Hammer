package com.github.vini2003.blade

import com.github.vini2003.blade.common.utilities.Networks
import com.github.vini2003.blade.common.utilities.Resources
import com.github.vini2003.blade.common.utilities.Styles
import com.github.vini2003.blade.testing.kotlin.DebugCommands
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
import org.apache.logging.log4j.Logger

class Blade : ModInitializer {
    companion object {
        @SuppressWarnings
        const val MOD_ID = "blade"

        val LOGGER: Logger = LogManager.getLogger(MOD_ID)

        fun identifier(string: String): Identifier {
            return Identifier(MOD_ID, string)
        }
    }

    override fun onInitialize() {
        Resources.initialize()
        Networks.initialize()

        DebugContainers.initialize()
        DebugCommands.initialize()
    }
}