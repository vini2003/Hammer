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

package dev.vini2003.hammer.util.registry.common;

import dev.architectury.registry.registries.RegistrySupplier;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.registry.common.HCConfig;
import dev.vini2003.hammer.util.api.common.item.TriggerItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;

public class HUItems {
	public static final RegistrySupplier<Item> FREEZE = HC.getItemRegistry().register(HC.id("freeze"),
			() -> TriggerItem.builder()
					 .trigger((world, user, hand, hit) -> {
						if (hit instanceof EntityHitResult entityHit) {
							var entity = entityHit.getEntity();
							   
							if (entity instanceof PlayerEntity player) {
							  var frozen = !player.hammer$isFrozen();
								 
							  player.hammer$setFrozen(frozen);
								 
							  user.getCommandSource().sendFeedback(() -> Text.translatable("command.hammer." + (frozen ? "freeze" : "unfreeze"), player.getDisplayName()), true);
							}
						}
					 })
					.targetEntity(e -> e instanceof PlayerEntity)
					.side(false, true)
					.raycast(true, true)
					.settings(new Item.Settings())
					.build()
	);
	
	public static final RegistrySupplier<Item> MUTE = HC.getItemRegistry().register(HC.id("mute"),
			() -> TriggerItem.builder()
					.trigger((world, user, hand, hit) -> {
						if (hit instanceof EntityHitResult entityHit) {
							var entity = entityHit.getEntity();
					
							if (entity instanceof PlayerEntity player) {
								var muted = !player.hammer$isMuted();
						
								player.hammer$setMuted(muted);
						
								user.getCommandSource().sendFeedback(() -> Text.translatable("command.hammer." + (muted ? "mute" : "unmute"), player.getDisplayName()), true);
							}
						}
					})
					.targetEntity(e -> e instanceof PlayerEntity)
					.side(false, true)
					.raycast(true, true)
					.settings(new Item.Settings())
					.build()
	);
	
	public static final RegistrySupplier<Item> HEAL = HC.getItemRegistry().register(HC.id("heal"),
			() -> TriggerItem.builder()
					.trigger((world, user, hand) -> {
						user.setHealth(user.getMaxHealth());
				
						user.getCommandSource().sendFeedback(() -> Text.translatable("command.hammer.heal.self"), true);
					})
					.trigger(((world, user, hand, hit) -> {
						if (hit instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof PlayerEntity player) {
							player.setHealth(player.getMaxHealth());
							
							user.getCommandSource().sendFeedback(() -> Text.translatable("command.hammer.heal.other", player.getDisplayName()), true);
						}
					}))
					.targetEntity(e -> e instanceof PlayerEntity)
					.side(false, true)
					.raycast(true, true)
					.settings(new Item.Settings())
					.build()
	);
	
	public static final RegistrySupplier<Item> SATIATE = HC.getItemRegistry().register(HC.id("satiate"),
			() -> TriggerItem.builder()
					   .trigger((world, user, hand) -> {
						   user.getHungerManager().setFoodLevel(20);
						   user.getHungerManager().setSaturationLevel(20.0F);
				
						   user.getCommandSource().sendFeedback(() -> Text.translatable("command.hammer.satiate.self"), true);
					   })
					   .trigger(((world, user, hand, hit) -> {
						   if (hit instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof PlayerEntity player) {
							   player.getHungerManager().setFoodLevel(20);
							   player.getHungerManager().setSaturationLevel(20.0F);
					
							   user.getCommandSource().sendFeedback(() -> Text.translatable("command.hammer.satiate.other", player.getDisplayName()), true);
						   }
					   }))
					   .targetEntity(e -> e instanceof PlayerEntity)
					   .side(false, true)
					   .raycast(true, true)
					   .settings(new Item.Settings())
					   .build()
	);
	
	public static final RegistrySupplier<Item> TOGGLE_END = HC.getItemRegistry().register(HC.id("toggle_end"),
			() -> TriggerItem.builder()
					   .trigger((world, user, hand) -> {
						   HCConfig.ENABLE_END = !HCConfig.ENABLE_END;
						
						   user.getCommandSource().sendFeedback(() -> Text.translatable("command.hammer." + (HCConfig.ENABLE_END ? "enable" : "disable") + "_end"), true);
					   })
					   .side(false, true)
					   .raycast(false, false)
					   .settings(new Item.Settings())
					   .build()
	);
	
	public static final RegistrySupplier<Item> TOGGLE_NETHER = HC.getItemRegistry().register(HC.id("toggle_nether"),
			() -> TriggerItem.builder()
					   .trigger((world, user, hand) -> {
						   HCConfig.ENABLE_NETHER = !HCConfig.ENABLE_NETHER;
				
						   user.getCommandSource().sendFeedback(() -> Text.translatable("command.hammer." + (HCConfig.ENABLE_NETHER ? "enable" : "disable") + "_nether"), true);
					   })
					   .side(false, true)
					   .raycast(false, false)
					   .settings(new Item.Settings())
					   .build()
	);
	
	public static void init() {
	
	}
}
