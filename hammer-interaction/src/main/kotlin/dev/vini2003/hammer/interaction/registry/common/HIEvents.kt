package dev.vini2003.hammer.interaction.registry.common

import dev.vini2003.hammer.interaction.common.manager.InteractionRuleManager
import net.fabricmc.fabric.api.event.player.*
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents

object HIEvents {
	fun init() {
		AttackBlockCallback.EVENT.register(InteractionRuleManager.AttackBlockCallbackListener)
		AttackEntityCallback.EVENT.register(InteractionRuleManager.AttackEntityCallbackListener)
		
		UseBlockCallback.EVENT.register(InteractionRuleManager.UseBlockCallbackListener)
		UseEntityCallback.EVENT.register(InteractionRuleManager.UseEntityCallbackListener)
		UseItemCallback.EVENT.register(InteractionRuleManager.UseItemCallbackListener)
		
		ServerPlayConnectionEvents.JOIN.register(InteractionRuleManager.ServerPlayConnectionEventsJoinListener)
		
		PlayerBlockBreakEvents.BEFORE.register(InteractionRuleManager.PlayerBlockBreakEventsBeforeListener)
	}
}