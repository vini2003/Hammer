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

package dev.vini2003.hammer.interaction.api.common.manager;

import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.api.common.util.BufUtil;
import dev.vini2003.hammer.core.api.common.util.PlayerUtil;
import dev.vini2003.hammer.interaction.api.common.interaction.InteractionMode;
import dev.vini2003.hammer.interaction.api.common.interaction.InteractionRule;
import dev.vini2003.hammer.interaction.api.common.interaction.InteractionType;
import dev.vini2003.hammer.interaction.registry.common.HINetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InteractionRuleManager {
	private static final Map<UUID, List<InteractionRule<?>>> RULES = new ConcurrentHashMap<>();
	
	public static void clear(PlayerEntity player) {
		RULES.remove(player.getUuid());
		
		if (player instanceof ServerPlayerEntity serverPlayer) {
			syncWith(ImmutableList.of(serverPlayer));
		}
	}
	
	public static void add(PlayerEntity player, InteractionRule<?> rule) {
		RULES.computeIfAbsent(player.getUuid(), ($) -> new ArrayList<>());
		
		if (contains(player, rule)) {
			return;
		}
		
		RULES.get(player.getUuid()).add(rule);
		
		if (player instanceof ServerPlayerEntity serverPlayer) {
			syncWith(ImmutableList.of(serverPlayer));
		}
	}
	
	public static void remove(PlayerEntity player, InteractionRule<?> rule) {
		RULES.computeIfAbsent(player.getUuid(), ($) -> new ArrayList<>());
		
		RULES.get(player.getUuid()).remove(rule);
		
		if (player instanceof ServerPlayerEntity serverPlayer) {
			syncWith(ImmutableList.of(serverPlayer));
		}
	}
	
	public static boolean contains(PlayerEntity player, InteractionRule<?> rule) {
		return get(player).contains(rule);
	}
	
	public static Collection<InteractionRule<?>> get(PlayerEntity player) {
		return RULES.getOrDefault(player, ImmutableList.of());
	}
	
	public static boolean allows(PlayerEntity player, InteractionType type, Object object) {
		return allows(player.getUuid(), type, object);
	}
	
	public static boolean allows(UUID uuid, InteractionType type, Object object) {
		RULES.computeIfAbsent(uuid, ($) -> new ArrayList<>());
		
		var rules = new ArrayList<InteractionRule<?>>();
		
		for (var rule : RULES.get(uuid)) {
			if (rule.getType() == type) {
				rules.add(rule);
			}
		}
		
		for (var rule : rules) {
			var result = switch (rule.getType()) {
				case ITEM_USE -> object instanceof Item item && item.getRegistryEntry().isIn((TagKey<Item>) rule.getTag());
				case BLOCK_BREAK, BLOCK_PLACE, BLOCK_USE, BLOCK_ATTACK -> object instanceof Block block && block.getRegistryEntry().isIn((TagKey<Block>) rule.getTag());
				case ENTITY_USE, ENTITY_ATTACK -> object instanceof EntityType<?> entityType && entityType.getRegistryEntry().isIn((TagKey<EntityType<?>>) rule.getTag());
			};
			
			if (rule.getMode() == InteractionMode.BLACKLIST) {
				result = !result;
			}
			
			if (!result) {
				return false;
			}
		}
		
		return true;
	}
	
	private static void syncWith(List<ServerPlayerEntity> players) {
		var buf = PacketByteBufs.create();
		
		buf.writeInt(RULES.size());
		
		for (var entry : RULES.entrySet()) {
			buf.writeUuid(entry.getKey());
			
			var rules = entry.getValue();
			
			buf.writeInt(rules.size());
			
			for (var rule : rules) {
				buf.writeEnumConstant(rule.getType());
				buf.writeEnumConstant(rule.getMode());
				BufUtil.writeTagKey(buf, rule.getTag());
			}
		}
		
		for (var player : players) {
			ServerPlayNetworking.send(player, HINetworking.SYNC_INTERACTION_RULES, PacketByteBufs.duplicate(buf));
		}
	}
	
	public static final class InteractionRuleSyncHandler implements ClientPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			var rulesSize = buf.readInt();
			
			for (var i = 0; i < rulesSize; ++i) {
				var uuid = buf.readUuid();
				
				var rules = new ArrayList<InteractionRule<?>>();
				
				var size = buf.readInt();
				
				for (var j = 0; j < size; ++j) {
					var rule = new InteractionRule(
							buf.readEnumConstant(InteractionType.class),
							buf.readEnumConstant(InteractionMode.class),
							BufUtil.readTagKey(buf)
					);
				}
			}
		}
	}
	
	public static final class JoinListener implements ServerPlayConnectionEvents.Join {
		@Override
		public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
			syncWith(ImmutableList.of(handler.getPlayer()));
		}
	}
	
	public static final class AttackBlockListener implements AttackBlockCallback {
		@Override
		public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction) {
			if (PlayerUtil.isFrozen(player) || !allows(player, InteractionType.BLOCK_ATTACK, world.getBlockState(pos).getBlock())) {
				return ActionResult.FAIL;
			}
			
			return ActionResult.PASS;
		}
	}
	
	public static final class AttackEntityListener implements AttackEntityCallback {
		@Override
		public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
			if (PlayerUtil.isFrozen(player) || !allows(player, InteractionType.ENTITY_ATTACK, entity.getType())) {
				return ActionResult.FAIL;
			}
			
			return ActionResult.PASS;
		}
	}
	
	public static final class UseBlockListener implements UseBlockCallback {
		@Override
		public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
			if (PlayerUtil.isFrozen(player) || !allows(player, InteractionType.BLOCK_USE, world.getBlockState(hitResult.getBlockPos()).getBlock())) {
				return ActionResult.FAIL;
			}
			
			return ActionResult.PASS;
		}
	}
	
	public static final class UseEntityListener implements UseEntityCallback {
		@Override
		public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
			if (PlayerUtil.isFrozen(player) || !allows(player, InteractionType.ENTITY_USE, entity.getType())) {
				return ActionResult.FAIL;
			}
			
			return ActionResult.PASS;
		}
	}
	
	public static final class UseItemListener implements UseItemCallback {
		@Override
		public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand) {
			var stack = player.getStackInHand(hand);
			
			if (stack.getItem() instanceof BlockItem blockItem) {
				if (PlayerUtil.isFrozen(player) || !allows(player, InteractionType.BLOCK_PLACE, blockItem.getBlock())) {
					return TypedActionResult.fail(stack);
				}
			}
			
			if (PlayerUtil.isFrozen(player) || !allows(player, InteractionType.ITEM_USE, stack.getItem())) {
				return TypedActionResult.fail(stack);
			}
			
			return TypedActionResult.pass(stack);
		}
	}
	
	public static final class BeforeBreakListener implements PlayerBlockBreakEvents.Before {
		@Override
		public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
			if (PlayerUtil.isFrozen(player) || !allows(player, InteractionType.BLOCK_BREAK, world.getBlockState(pos).getBlock())) {
				return false;
			}
			
			return true;
		}
	}
}
