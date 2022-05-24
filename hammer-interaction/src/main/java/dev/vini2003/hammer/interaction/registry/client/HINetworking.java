package dev.vini2003.hammer.interaction.registry.client;

import dev.vini2003.hammer.interaction.api.common.manager.InteractionRuleManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import static dev.vini2003.hammer.interaction.registry.common.HINetworking.SYNC_INTERACTION_RULES;

public class HINetworking {
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(SYNC_INTERACTION_RULES, new InteractionRuleManager.InteractionRuleSyncHandler());
	}
}
