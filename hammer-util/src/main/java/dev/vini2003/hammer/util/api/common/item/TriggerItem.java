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

package dev.vini2003.hammer.util.api.common.item;

import com.google.common.base.Predicates;
import dev.vini2003.hammer.core.api.common.util.RaycastUtil;
import dev.vini2003.hammer.util.api.common.trigger.Trigger;
import dev.vini2003.hammer.util.api.common.trigger.RaycastTrigger;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class TriggerItem extends Item {
	protected final RaycastTrigger raycastTrigger;
	protected final Trigger trigger;
	
	protected final boolean raycast;
	protected final boolean outlineRaycast;
	
	protected final boolean client;
	protected final boolean server;
	
	protected final Predicate<Block> targetBlock;
	protected final Predicate<Entity> targetEntity;
	
	public TriggerItem(Settings settings, RaycastTrigger raycastTrigger, Trigger trigger, boolean raycast, boolean outlineRaycast, boolean client, boolean server, Predicate<Block> targetBlock, Predicate<Entity> targetEntity) {
		super(settings);
		
		this.raycastTrigger = raycastTrigger;
		this.trigger = trigger;
		
		this.raycast = raycast;
		this.outlineRaycast = outlineRaycast;
		
		this.client = client;
		this.server = server;
		
		this.targetBlock = targetBlock;
		this.targetEntity = targetEntity;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		var stack = user.getStackInHand(hand);
		
		if (world.isClient() && client || !world.isClient() && server) {
			if (trigger != null) {
				trigger.onTrigger(world, user, hand);
			}
			
			var raycastEntityHitResult = RaycastUtil.raycastEntity(user, 0.0F, 256.0F);
			
			if (raycastEntityHitResult != null) {
				if (raycastTrigger != null && targetEntity != null) {
					var entity = ((EntityHitResult) raycastEntityHitResult).getEntity();
					
					if (targetEntity.test(entity)) {
						raycastTrigger.onTrigger(world, user, hand, raycastEntityHitResult);
					}
				}
			} else {
				var raycastBlockHitResult = user.raycast(256.0F, 0.0F, false);
				
				if (raycastBlockHitResult != null) {
					if (raycastTrigger != null && targetBlock != null) {
						var blockPos = ((BlockHitResult) raycastBlockHitResult).getBlockPos();
						var block = world.getBlockState(blockPos).getBlock();
						
						if (targetBlock.test(block)) {
							raycastTrigger.onTrigger(world, user, hand, raycastBlockHitResult);
						}
					}
				}
			}
			
			return TypedActionResult.pass(stack);
		} else {
			return super.use(world, user, hand);
		}
	}
	
	public boolean shouldOutlineEntityRaycast(Entity entity) {
		return outlineRaycast && raycastTrigger != null && targetEntity != null && targetEntity.test(entity);
	}
	
	public boolean shouldOutlineBlockRaycast(Block block) {
		return outlineRaycast && raycastTrigger != null && targetBlock != null && targetBlock.test(block);
	}
	
	public boolean shouldEnableOutline() {
		return targetEntity != null || targetBlock != null;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		protected RaycastTrigger raycastTrigger = null;
		protected Trigger trigger = null;
		
		protected boolean raycast = false;
		protected boolean outlineRaycast = false;
		
		protected boolean client = true;
		protected boolean server = true;
		
		protected Predicate<Block> targetBlock = null;
		protected Predicate<Entity> targetEntity = null;
		
		protected Item.Settings settings;
		
		public Builder trigger(RaycastTrigger raycastTrigger) {
			this.raycastTrigger = raycastTrigger;
			
			return this;
		}
		
		public Builder trigger(Trigger trigger) {
			this.trigger = trigger;
			
			return this;
		}
		
		public Builder raycast(boolean raycast, boolean outlineRaycast) {
			this.raycast = raycast;
			this.outlineRaycast = outlineRaycast;
			
			return this;
		}
		
		public Builder side(boolean client, boolean server) {
			this.client = client;
			this.server = server;
			
			return this;
		}
		
		public Builder targetBlock() {
			this.targetBlock = Predicates.alwaysTrue();
			
			return this;
		}
		
		public Builder targetBlock(Predicate<Block> targetBlock) {
			this.targetBlock = targetBlock;

			return this;
		}
		
		public Builder targetEntity() {
			this.targetEntity = Predicates.alwaysTrue();
			
			return this;
		}
		
		public Builder targetEntity(Predicate<Entity> targetEntity) {
			this.targetEntity = targetEntity;
			
			return this;
		}
		
		public Builder settings(Item.Settings settings) {
			this.settings = settings.maxCount(1).fireproof();
			
			return this;
		}
		
		public TriggerItem build() {
			return new TriggerItem(settings, raycastTrigger, trigger, raycast, outlineRaycast, client, server, targetBlock, targetEntity);
		}
	}
}
