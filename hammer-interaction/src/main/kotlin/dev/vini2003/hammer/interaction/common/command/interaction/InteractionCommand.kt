package dev.vini2003.hammer.interaction.common.command.interaction

import com.mojang.brigadier.context.CommandContext
import dev.vini2003.hammer.command.common.command.ServerRootCommand
import dev.vini2003.hammer.command.common.util.extension.*
import dev.vini2003.hammer.interaction.common.interaction.InteractionMode
import dev.vini2003.hammer.interaction.common.interaction.InteractionRule
import dev.vini2003.hammer.interaction.common.interaction.InteractionType
import dev.vini2003.hammer.interaction.common.manager.InteractionRuleManager
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.tag.TagKey
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey

val INTERACTION_COMMAND = ServerRootCommand {
	command("interaction") {
		literal("rule") {
			val execute: CommandContext<ServerCommandSource>.(Boolean, Collection<ServerPlayerEntity>) -> Unit =
				{ action, players ->
					val typeName = getString("type")
					
					if (InteractionType.values().any { type -> type.name.lowercase() == typeName }) {
						val type = InteractionType.valueOf(typeName.uppercase())
						
						val modeName = getString("mode")
						
						if (InteractionMode.values().any { mode -> mode.name.lowercase() == modeName }) {
							val mode = InteractionMode.valueOf(modeName.uppercase())
							
							val registryId = IdentifierArgumentType.getIdentifier(this, "registry")
							
							val registryKey = RegistryKey.ofRegistry<Any>(registryId)
							
							val tagId = IdentifierArgumentType.getIdentifier(this, "tag")
							
							val tagKey = TagKey.of(registryKey, tagId)
							
							val rule = InteractionRule<Any>(type, mode, tagKey)
							
							players.forEach { player ->
								if (action) {
									InteractionRuleManager.add(player, rule)
								} else {
									InteractionRuleManager.remove(player, rule)
								}
							}
						}
					}
				}
			
			arrayOf("add" to true, "remove" to false).forEach { (name, action) ->
				literal(name) {
					word("mode") {
						suggests { _, builder ->
							InteractionMode.values().map { type -> type.name.lowercase() }.forEach { type -> builder.suggest(type) }
							
							builder.buildFuture()
						}
						
						word("type") {
							suggests { _, builder ->
								InteractionType.values().map { type -> type.name.lowercase() }.forEach { type -> builder.suggest(type) }
								
								builder.buildFuture()
							}
							
							argument("registry", IdentifierArgumentType.identifier()) {
								suggests { _, builder ->
									Registry.REGISTRIES.map{ registry -> registry.key.value }.forEach { id -> builder.suggest(id.toString()) }
									
									builder.buildFuture()
								}
								
								argument("tag", IdentifierArgumentType.identifier()) {
									execute {
										execute(action, listOf(source.player))
									}
									
									argument("players", EntityArgumentType.players()) {
										execute {
											execute(action, EntityArgumentType.getPlayers(this, "players"))
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}