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

package dev.vini2003.hammer.registry.common;

import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.common.util.PlayerUtil;
import dev.vini2003.hammer.core.registry.common.HCConfig;
import dev.vini2003.hammer.core.registry.common.HCItemGroups;
import dev.vini2003.hammer.util.api.common.item.TriggerItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.registry.Registry;

public class HUItems {
	public static final Item FREEZE = Registry.register(Registry.ITEM, HC.id("freeze"),
			TriggerItem.builder()
					 .trigger((world, user, hand, hit) -> {
						if (hit instanceof EntityHitResult entityHit) {
							var entity = entityHit.getEntity();
							   
							if (entity instanceof PlayerEntity player) {
							  var frozen = !PlayerUtil.isFrozen(player);
								  
							  PlayerUtil.setFrozen(player, frozen);
								  
							  user.getCommandSource().sendFeedback(Text.translatable("command.hammer." + (frozen ? "freeze" : "unfreeze"), player.getDisplayName()), true);
							}
						}
					 })
					.targetEntity(e -> e instanceof PlayerEntity)
					.side(false, true)
					.raycast(true, true)
					.settings(new Item.Settings())
					.build()
	);
	
	public static final Item MUTE = Registry.register(Registry.ITEM, HC.id("mute"),
			TriggerItem.builder()
					.trigger((world, user, hand, hit) -> {
						if (hit instanceof EntityHitResult entityHit) {
							var entity = entityHit.getEntity();
					
							if (entity instanceof PlayerEntity player) {
								var muted = !ChatUtil.isMuted(player);
						
								ChatUtil.setMuted(player, muted);
						
								user.getCommandSource().sendFeedback(Text.translatable("command.hammer." + (muted ? "mute" : "unmute"), player.getDisplayName()), true);
							}
						}
					})
					.targetEntity(e -> e instanceof PlayerEntity)
					.side(false, true)
					.raycast(true, true)
					.settings(new Item.Settings())
					.build()
	);
	
	public static final Item HEAL = Registry.register(Registry.ITEM, HC.id("heal"),
			TriggerItem.builder()
					.trigger((world, user, hand) -> {
						user.setHealth(user.getMaxHealth());
				
						user.getCommandSource().sendFeedback(Text.translatable("command.hammer.heal.self"), true);
					})
					.trigger(((world, user, hand, hit) -> {
						if (hit instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof PlayerEntity player) {
							player.setHealth(player.getMaxHealth());
							
							user.getCommandSource().sendFeedback(Text.translatable("command.hammer.heal.other", player.getDisplayName()), true);
						}
					}))
					.targetEntity(e -> e instanceof PlayerEntity)
					.side(false, true)
					.raycast(true, true)
					.settings(new Item.Settings())
					.build()
	);
	
	public static final Item SATIATE = Registry.register(Registry.ITEM, HC.id("satiate"),
			TriggerItem.builder()
					   .trigger((world, user, hand) -> {
						   user.getHungerManager().setFoodLevel(20);
						   user.getHungerManager().setSaturationLevel(20.0F);
				
						   user.getCommandSource().sendFeedback(Text.translatable("command.hammer.satiate.self"), true);
					   })
					   .trigger(((world, user, hand, hit) -> {
						   if (hit instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof PlayerEntity player) {
							   player.getHungerManager().setFoodLevel(20);
							   player.getHungerManager().setSaturationLevel(20.0F);
					
							   user.getCommandSource().sendFeedback(Text.translatable("command.hammer.satiate.other", player.getDisplayName()), true);
						   }
					   }))
					   .targetEntity(e -> e instanceof PlayerEntity)
					   .side(false, true)
					   .raycast(true, true)
					   .settings(new Item.Settings())
					   .build()
	);
	
	public static final Item TOGGLE_END = Registry.register(Registry.ITEM, HC.id("toggle_end"),
			TriggerItem.builder()
					   .trigger((world, user, hand) -> {
						   HCConfig.ENABLE_END = !HCConfig.ENABLE_END;
						
						   user.getCommandSource().sendFeedback(Text.translatable("command.hammer." + (HCConfig.ENABLE_END ? "enable" : "disable") + "_end"), true);
					   })
					   .side(false, true)
					   .raycast(false, false)
					   .settings(new Item.Settings())
					   .build()
	);
	
	public static final Item TOGGLE_NETHER = Registry.register(Registry.ITEM, HC.id("toggle_nether"),
			TriggerItem.builder()
					   .trigger((world, user, hand) -> {
						   HCConfig.ENABLE_NETHER = !HCConfig.ENABLE_NETHER;
				
						   user.getCommandSource().sendFeedback(Text.translatable("command.hammer." + (HCConfig.ENABLE_NETHER ? "enable" : "disable") + "_nether"), true);
					   })
					   .side(false, true)
					   .raycast(false, false)
					   .settings(new Item.Settings())
					   .build()
	);
	
	public static void init() {
	
	}
}
