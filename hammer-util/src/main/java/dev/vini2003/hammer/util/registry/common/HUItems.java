package dev.vini2003.hammer.util.registry.common;

import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.common.util.PlayerUtil;
import dev.vini2003.hammer.core.registry.common.HCConfig;
import dev.vini2003.hammer.util.api.common.item.TriggerItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.registry.Registry;

public class HUItems {
	public static final Item FREEZE = Registry.register(Registry.ITEM, HC.id("freeze"),
			TriggerItem.builder()
					 .trigger((world, user, hand, hit) -> {
						if (hit instanceof EntityHitResult entityHit) {
							var entity = entityHit.getEntity();
							   
							if (entity instanceof PlayerEntity player) {
							  var frozen = !PlayerUtil.isFrozen(player);
								  
							  PlayerUtil.setFrozen(player, frozen);
								  
							  user.getCommandSource().sendFeedback(new TranslatableText("command.hammer." + (frozen ? "freeze" : "unfreeze"), player.getDisplayName()), true);
							}
						}
					 })
					.targetEntity(e -> e instanceof PlayerEntity)
					.side(false, true)
					.raycast(true, true)
					.settings(new Item.Settings().group(HUItemGroups.HAMMER))
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
						
								user.getCommandSource().sendFeedback(new TranslatableText("command.hammer." + (muted ? "mute" : "unmute"), player.getDisplayName()), true);
							}
						}
					})
					.targetEntity(e -> e instanceof PlayerEntity)
					.side(false, true)
					.raycast(true, true)
					.settings(new Item.Settings().group(HUItemGroups.HAMMER))
					.build()
	);
	
	public static final Item HEAL = Registry.register(Registry.ITEM, HC.id("heal"),
			TriggerItem.builder()
					.trigger((world, user, hand) -> {
						user.setHealth(user.getMaxHealth());
				
						user.getCommandSource().sendFeedback(new TranslatableText("command.hammer.heal.self"), true);
					})
					.trigger(((world, user, hand, hit) -> {
						if (hit instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof PlayerEntity player) {
							player.setHealth(player.getMaxHealth());
							
							user.getCommandSource().sendFeedback(new TranslatableText("command.hammer.heal.other", player.getDisplayName()), true);
						}
					}))
					.targetEntity(e -> e instanceof PlayerEntity)
					.side(false, true)
					.raycast(true, true)
					.settings(new Item.Settings().group(HUItemGroups.HAMMER))
					.build()
	);
	
	public static final Item SATIATE = Registry.register(Registry.ITEM, HC.id("satiate"),
			TriggerItem.builder()
					   .trigger((world, user, hand) -> {
						   user.getHungerManager().setFoodLevel(20);
						   user.getHungerManager().setSaturationLevel(20.0F);
				
						   user.getCommandSource().sendFeedback(new TranslatableText("command.hammer.satiate.self"), true);
					   })
					   .trigger(((world, user, hand, hit) -> {
						   if (hit instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof PlayerEntity player) {
							   player.getHungerManager().setFoodLevel(20);
							   player.getHungerManager().setSaturationLevel(20.0F);
					
							   user.getCommandSource().sendFeedback(new TranslatableText("command.hammer.satiate.other", player.getDisplayName()), true);
						   }
					   }))
					   .targetEntity(e -> e instanceof PlayerEntity)
					   .side(false, true)
					   .raycast(true, true)
					   .settings(new Item.Settings().group(HUItemGroups.HAMMER))
					   .build()
	);
	
	public static final Item TOGGLE_END = Registry.register(Registry.ITEM, HC.id("toggle_end"),
			TriggerItem.builder()
					   .trigger((world, user, hand) -> {
						   HCConfig.ENABLE_END = !HCConfig.ENABLE_END;
						
						   user.getCommandSource().sendFeedback(new TranslatableText("command.hammer." + (HCConfig.ENABLE_END ? "enable" : "disable") + "_end"), true);
					   })
					   .side(false, true)
					   .raycast(false, false)
					   .settings(new Item.Settings().group(HUItemGroups.HAMMER))
					   .build()
	);
	
	public static final Item TOGGLE_NETHER = Registry.register(Registry.ITEM, HC.id("toggle_nether"),
			TriggerItem.builder()
					   .trigger((world, user, hand) -> {
						   HCConfig.ENABLE_NETHER = !HCConfig.ENABLE_NETHER;
				
						   user.getCommandSource().sendFeedback(new TranslatableText("command.hammer." + (HCConfig.ENABLE_NETHER ? "enable" : "disable") + "_nether"), true);
					   })
					   .side(false, true)
					   .raycast(false, false)
					   .settings(new Item.Settings().group(HUItemGroups.HAMMER))
					   .build()
	);
	
	public static void init() {
	
	}
}
