package dev.vini2003.hammer.common.util

import net.minecraft.server.world.ServerWorld
import net.minecraft.world.GameRules

object GameRuleUtils {
	fun enable(world: ServerWorld, rule: GameRules.Key<GameRules.BooleanRule>) {
		world.gameRules.get(rule).set(true, world.server)
	}
	
	fun disable(world: ServerWorld, rule: GameRules.Key<GameRules.BooleanRule>) {
		world.gameRules.get(rule).set(false, world.server)
	}
	
	fun set(world: ServerWorld, rule: GameRules.Key<GameRules.IntRule>, value: Int) {
		world.gameRules.get(rule).set(value, world.server)
	}
}