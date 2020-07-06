package dev.vini2003.hammer.command.common.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.minecraft.server.command.ServerCommandSource

open class ServerNestedCommand(val block: LiteralArgumentBuilder<ServerCommandSource>.() -> Unit) {
	fun register(builder: LiteralArgumentBuilder<ServerCommandSource>) = builder.apply(block)
}