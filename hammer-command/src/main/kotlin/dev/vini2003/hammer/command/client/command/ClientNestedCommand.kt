package dev.vini2003.hammer.command.client.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource

open class ClientNestedCommand(val block: LiteralArgumentBuilder<FabricClientCommandSource>.() -> Unit) {
	fun register(builder: LiteralArgumentBuilder<FabricClientCommandSource>) = builder.apply(block)
}