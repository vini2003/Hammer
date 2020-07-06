package dev.vini2003.hammer.command.client.command

import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource

open class ClientRootCommand(val block: CommandDispatcher<FabricClientCommandSource>.() -> Unit) {
	fun register(builder: CommandDispatcher<FabricClientCommandSource>) = builder.apply(block)
}