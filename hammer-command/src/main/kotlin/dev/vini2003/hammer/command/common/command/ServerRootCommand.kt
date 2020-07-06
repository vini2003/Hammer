package dev.vini2003.hammer.command.common.command

import com.mojang.brigadier.CommandDispatcher
import net.minecraft.server.command.ServerCommandSource

open class ServerRootCommand(val block: CommandDispatcher<ServerCommandSource>.() -> Unit) {
	fun register(builder: CommandDispatcher<ServerCommandSource>) = builder.apply(block)
}