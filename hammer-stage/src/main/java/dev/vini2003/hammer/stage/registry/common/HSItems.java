package dev.vini2003.hammer.stage.registry.common;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.registry.common.HCItemGroups;
import dev.vini2003.hammer.stage.api.common.manager.StageManager;
import dev.vini2003.hammer.stage.api.common.stage.Stage;
import dev.vini2003.hammer.util.api.common.item.TriggerItem;
import net.minecraft.item.Item;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import net.minecraft.util.registry.Registry;

public class HSItems {
	public static final Item PREPARE_STAGE = Registry.register(Registry.ITEM,
			HC.id("prepare_stage"),
			TriggerItem.builder()
					   .settings(new Item.Settings())
					   .trigger((world, user, hand) -> {
							var active = StageManager.getActive(world.getRegistryKey());
						
						   if (active != null) {
							   active.prepare(world);
							
							   user.getCommandSource().sendFeedback(Text.translatable("command.hammer.stage.prepare.success"), true);
						   } else {
							   user.getCommandSource().sendFeedback(Text.translatable("command.hammer.stage.prepare.failure"), true);
						   }
						})
						.build()
	);
	
	public static final Item START_STAGE = Registry.register(Registry.ITEM,
			HC.id("start_stage"),
			TriggerItem.builder()
					   .settings(new Item.Settings())
					   .trigger((world, user, hand) -> {
							var active = StageManager.getActive(world.getRegistryKey());
							
							if (active != null) {
								active.start(world);
								
								user.getCommandSource().sendFeedback(Text.translatable("command.hammer.stage.start.success"), true);
							} else {
								user.getCommandSource().sendFeedback(Text.translatable("command.hammer.stage.start.failure"), true);
							}
						})
						.build()
	);
	
	public static final Item STOP_STAGE = Registry.register(Registry.ITEM,
			HC.id("stop_stage"),
			TriggerItem.builder()
						.settings(new Item.Settings())
						.trigger((world, user, hand) -> {
							var active = StageManager.getActive(world.getRegistryKey());
							
							if (active != null) {
								active.stop(world);
								
								user.getCommandSource().sendFeedback(Text.translatable("command.hammer.stage.stop.success"), true);
							} else {
								user.getCommandSource().sendFeedback(Text.translatable("command.hammer.stage.stop.failure"), true);
							}
						})
						.build()
	);
	
	public static final Item PAUSE_STAGE = Registry.register(Registry.ITEM,
			HC.id("pause_stage"),
			TriggerItem.builder()
					   .settings(new Item.Settings())
					   .trigger((world, user, hand) -> {
							var active = StageManager.getActive(world.getRegistryKey());
							
							if (active != null) {
								active.pause(world);
								
								if (active.getState() == Stage.State.PAUSED) {
									user.getCommandSource().sendFeedback(Text.translatable("command.hammer.stage.pause.success"), true);
								} else {
									user.getCommandSource().sendFeedback(Text.translatable("command.hammer.stage.unpause.success"), true);
								}
							} else {
								user.getCommandSource().sendFeedback(Text.translatable("command.hammer.stage.pause.failure"), true);
							}
						})
						.build()
	);
	
	public static final Item RESTART_STAGE = Registry.register(Registry.ITEM,
			HC.id("restart_stage"),
			TriggerItem.builder()
					   .settings(new Item.Settings())
					   .trigger((world, user, hand) -> {
							var active = StageManager.getActive(world.getRegistryKey());
						
						   if (active != null) {
							   active.restart(world);
							
							   user.getCommandSource().sendFeedback(Text.translatable("command.hammer.stage.restart.success"), true);
						   } else {
							   user.getCommandSource().sendFeedback(Text.translatable("command.hammer.stage.restart.failure"), true);
						   }
						})
						.build()
	);
	
	public static void init() {
	
	}
}
