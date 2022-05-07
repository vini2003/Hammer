/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.interaction.impl.common.command.interaction

import com.mojang.brigadier.context.CommandContext
import dev.vini2003.hammer.command.api.common.command.ServerRootCommand
import dev.vini2003.hammer.command.api.common.util.extension.*
import dev.vini2003.hammer.interaction.api.common.interaction.InteractionMode
import dev.vini2003.hammer.interaction.api.common.interaction.InteractionRule
import dev.vini2003.hammer.interaction.api.common.interaction.InteractionType
import dev.vini2003.hammer.interaction.api.common.manager.InteractionRuleManager
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