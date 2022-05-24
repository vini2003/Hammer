package dev.vini2003.hammer.interaction.registry.common;

import dev.vini2003.hammer.interaction.api.common.manager.InteractionRuleManager;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class HIEvents {
	public static void init() {
		AttackBlockCallback.EVENT.register(new InteractionRuleManager.AttackBlockListener());
		AttackEntityCallback.EVENT.register(new InteractionRuleManager.AttackEntityListener());
		
		UseBlockCallback.EVENT.register(new InteractionRuleManager.UseBlockListener());
		UseEntityCallback.EVENT.register(new InteractionRuleManager.UseEntityListener());
		UseItemCallback.EVENT.register(new InteractionRuleManager.UseItemListener());
		
		ServerPlayConnectionEvents.JOIN.register(new InteractionRuleManager.JoinListener());
		
		PlayerBlockBreakEvents.BEFORE.register(new InteractionRuleManager.BeforeBreakListener());
	}
}
