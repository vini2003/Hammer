package dev.vini2003.hammer.interaction.common.manager

import com.google.common.collect.Maps
import dev.vini2003.hammer.common.util.BufUtils
import dev.vini2003.hammer.common.util.extension.frozen
import dev.vini2003.hammer.interaction.common.interaction.InteractionMode
import dev.vini2003.hammer.interaction.common.interaction.InteractionRule
import dev.vini2003.hammer.interaction.common.interaction.InteractionType
import dev.vini2003.hammer.interaction.common.packet.sync.SyncInteractionRulesPacket
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
	
	@JvmStatic
	fun syncWith(player: ServerPlayerEntity) {
		val rules = RULES.getOrDefault(player.uuid!!, mutableListOf())
		
		val packet = SyncInteractionRulesPacket(rules)
		val buf = BufUtils.toPacketByteBuf(packet)
		
		ServerPlayNetworking.send(player, HINetworking.SYNC_INTERACTION_RULES, buf)
	}
	
	@JvmStatic
	fun clear(player: PlayerEntity) {
		RULES.remove(player.uuid)
		
		if (!player.world.isClient) {
			syncWith(player as ServerPlayerEntity)
		}
	}
	
	@JvmStatic
	fun add(player: PlayerEntity, rule: InteractionRule<*>) {
		RULES.computeIfAbsent(player.uuid) { mutableListOf() }
		
		RULES[player.uuid]?.add(rule)
		
		if (!player.world.isClient) {
			syncWith(player as ServerPlayerEntity)
		}
	}
	
	@JvmStatic
	fun remove(player: PlayerEntity, rule: InteractionRule<*>) {
		RULES[player.uuid]?.remove(rule)
		
		if (!player.world.isClient) {
			syncWith(player as ServerPlayerEntity)
		}
	}
	
	@JvmStatic
	fun get(player: PlayerEntity): Collection<InteractionRule<*>> {
		return RULES.getOrDefault(player.uuid, mutableListOf())
	}
	
	@JvmStatic
	fun rules(): Iterator<InteractionRule<*>> {
		return RULES.iterator() as Iterator<InteractionRule<*>>
	}
	
	@JvmStatic
	fun allows(player: PlayerEntity, type: InteractionType, value: Any): Boolean {
		return allows(player.uuid, type, value)
	}
	
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