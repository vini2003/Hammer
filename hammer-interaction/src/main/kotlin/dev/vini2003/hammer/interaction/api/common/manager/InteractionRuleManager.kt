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

package dev.vini2003.hammer.interaction.api.common.manager

import com.google.common.collect.Maps
import dev.vini2003.hammer.core.api.common.util.extension.frozen
import dev.vini2003.hammer.interaction.api.common.interaction.InteractionMode
import dev.vini2003.hammer.interaction.api.common.interaction.InteractionRule
import dev.vini2003.hammer.interaction.api.common.interaction.InteractionType
import dev.vini2003.hammer.interaction.impl.common.packet.sync.SyncInteractionRulesPacket
import dev.vini2003.hammer.interaction.registry.common.HINetworking
import net.fabricmc.fabric.api.event.player.*
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.tag.TagKey
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.*

object InteractionRuleManager {
	private val RULES: MutableMap<UUID, MutableList<InteractionRule<*>>> = Maps.newConcurrentMap()
	
	/**
	 * Clears all rules from the given player.
	 *
	 * @param player the player.
	 */
	@JvmStatic
	fun clear(player: PlayerEntity) {
		RULES.remove(player.uuid)
		
		if (!player.world.isClient) {
			syncWith(player as ServerPlayerEntity)
		}
	}
	
	/**
	 * Adds a rule to the given player.
	 *
	 * @param player the player.
	 * @param rule the rule.
	 */
	@JvmStatic
	fun add(player: PlayerEntity, rule: InteractionRule<*>) {
		RULES.computeIfAbsent(player.uuid) { mutableListOf() }
		
		RULES[player.uuid]?.add(rule)
		
		if (!player.world.isClient) {
			syncWith(player as ServerPlayerEntity)
		}
	}
	
	/**
	 * Removes a rule from the given player.
	 *
	 * @param player the player.
	 * @param rule the rule.
	 */
	@JvmStatic
	fun remove(player: PlayerEntity, rule: InteractionRule<*>) {
		RULES[player.uuid]?.remove(rule)
		
		if (!player.world.isClient) {
			syncWith(player as ServerPlayerEntity)
		}
	}
	
	/**
	 * Returns the rules added to a player.
	 *
	 * @param player the player.
	 * @return the rules.
	 */
	@JvmStatic
	fun get(player: PlayerEntity): Collection<InteractionRule<*>> {
		return RULES.getOrDefault(player.uuid, mutableListOf())
	}
	
	
	/**
	 * Returns whether a player is allowed to do a certain interaction.
	 *
	 * @param player the player.
	 * @param type the interaction type.
	 * @param value the interaction's check value.
	 * @return the result.
	 */
	@JvmStatic
	fun allows(player: PlayerEntity, type: InteractionType, value: Any): Boolean {
		return allows(player.uuid, type, value)
	}
	
	/**
	 * Returns whether a player is allowed to do a certain interaction.
	 *
	 * @param uuid the player's UUID.
	 * @param type the interaction type.
	 * @param value the interaction's check value.
	 * @return the result.
	 */
	@JvmStatic
	fun allows(uuid: UUID, type: InteractionType, value: Any): Boolean {
		return RULES[uuid]?.filter { rule ->
			rule.type == type
		}?.all { rule ->
			var result = when (rule.type) {
				InteractionType.ITEM_USE -> value is Item && value.registryEntry.isIn(rule.tagKey as TagKey<Item>)
				InteractionType.BLOCK_BREAK, InteractionType.BLOCK_PLACE, InteractionType.BLOCK_USE, InteractionType.BLOCK_ATTACK -> value is Block && value.registryEntry.isIn(rule.tagKey as TagKey<Block>)
				InteractionType.ENTITY_USE, InteractionType.ENTITY_ATTACK -> value is EntityType<*> && value.registryEntry.isIn(rule.tagKey as TagKey<EntityType<*>>)
			}
			
			if (rule.mode == InteractionMode.BLACKLIST) {
				result = !result
			}
			
			result
		} ?: true
	}
	
	@JvmStatic
	private fun syncWith(player: ServerPlayerEntity) {
		val rules = RULES.getOrDefault(player.uuid!!, mutableListOf())
		
		val packet = SyncInteractionRulesPacket(rules)
		val buf = BufUtils.toPacketByteBuf(packet)
		
		ServerPlayNetworking.send(player, HINetworking.SYNC_INTERACTION_RULES, buf)
	}
	
	/**
	 * [ServerPlayConnectionEvents.Join]
	 */
	object ServerPlayConnectionEventsJoinListener : ServerPlayConnectionEvents.Join {
		override fun onPlayReady(handler: ServerPlayNetworkHandler, sender: PacketSender, server: MinecraftServer) {
			syncWith(handler.player)
		}
	}
	
	/**
	 * [AttackBlockCallback]
	 */
	object AttackBlockCallbackListener : AttackBlockCallback {
		override fun interact(player: PlayerEntity, world: World, hand: Hand, pos: BlockPos, direction: Direction): ActionResult {
			if (player.frozen || !allows(player, InteractionType.BLOCK_ATTACK, world.getBlockState(pos).block)) {
				return ActionResult.FAIL
			}
			
			return ActionResult.PASS
		}
	}
	
	/**
	 * [AttackEntityCallback]
	 */
	object AttackEntityCallbackListener : AttackEntityCallback {
		override fun interact(player: PlayerEntity, world: World, hand: Hand, entity: Entity, hitResult: EntityHitResult?): ActionResult {
			if (player.frozen || !allows(player, InteractionType.ENTITY_ATTACK, entity.type)) {
				return ActionResult.FAIL
			}
			
			return ActionResult.PASS
		}
	}
	
	/**
	 * [UseBlockCallback]
	 */
	object UseBlockCallbackListener : UseBlockCallback {
		override fun interact(player: PlayerEntity, world: World, hand: Hand, hitResult: BlockHitResult): ActionResult {
			if (player.frozen || !allows(player, InteractionType.BLOCK_USE, world.getBlockState(hitResult.blockPos).block)) {
				return ActionResult.FAIL
			}
			
			return ActionResult.PASS
		}
	}
	
	
	/**
	 * [UseEntityCallback]
	 */
	object UseEntityCallbackListener : UseEntityCallback {
		override fun interact(player: PlayerEntity, world: World, hand: Hand, entity: Entity, hitResult: EntityHitResult?): ActionResult {
			if (player.frozen || !allows(player, InteractionType.ENTITY_USE, entity.type)) {
				return ActionResult.FAIL
			}
			
			return ActionResult.PASS
		}
	}
	
	/**
	 * [UseItemCallback]
	 */
	object UseItemCallbackListener : UseItemCallback {
		override fun interact(player: PlayerEntity, world: World, hand: Hand): TypedActionResult<ItemStack> {
			val stack = player.getStackInHand(hand)
			
			if (stack.item is BlockItem) {
				if (player.frozen || !allows(player, InteractionType.BLOCK_PLACE, (stack.item as BlockItem).block)) {
					return TypedActionResult.fail(stack)
				}
			}
			
			if (player.frozen || !allows(player, InteractionType.ITEM_USE, stack.item)) {
				return TypedActionResult.fail(stack)
			}
			
			return TypedActionResult.pass(stack)
		}
	}
	
	/**
	 * [PlayerBlockBreakEvents.Before]
	 */
	object PlayerBlockBreakEventsBeforeListener : PlayerBlockBreakEvents.Before {
		override fun beforeBlockBreak(world: World, player: PlayerEntity, pos: BlockPos, state: BlockState, blockEntity: BlockEntity?): Boolean {
			if (player.frozen || !allows(player, InteractionType.BLOCK_BREAK, world.getBlockState(pos).block)) {
				return false
			}
			
			return true
		}
	}
}