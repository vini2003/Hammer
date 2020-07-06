package dev.vini2003.hammer.common.util

import dev.vini2003.hammer.common.util.extension.*
import net.minecraft.entity.player.PlayerEntity

object PlayerUtils {
	@JvmStatic
	fun getAttackDamageMultiplier(player: PlayerEntity) = player.attackDamageMultiplier
	
	@JvmStatic
	fun setAttackDamageMultiplier(player: PlayerEntity, value: Float) {
		player.attackDamageMultiplier = value
	}
	
	@JvmStatic
	fun getDamageMultiplier(player: PlayerEntity) = player.damageMultiplier
	
	@JvmStatic
	fun setDamageMultiplier(player: PlayerEntity, value: Float) {
		player.damageMultiplier = value
	}
	
	@JvmStatic
	fun getFallDamageMultiplier(player: PlayerEntity) = player.fallDamageMultiplier
	
	@JvmStatic
	fun setFallDamageMultiplier(player: PlayerEntity, value: Float) {
		player.fallDamageMultiplier = value
	}
	
	@JvmStatic
	fun getMovementSpeedMultiplier(player: PlayerEntity) = player.movementSpeedMultiplier
	
	@JvmStatic
	fun setMovementSpeedMultiplier(player: PlayerEntity, value: Float) {
		player.movementSpeedMultiplier = value
	}
	
	@JvmStatic
	fun getJumpMultiplier(player: PlayerEntity) = player.jumpMultiplier
	
	@JvmStatic
	fun setJumpMultiplier(player: PlayerEntity, value: Float) {
		player.jumpMultiplier = value
	}
	
	@JvmStatic
	fun getHealMultiplier(player: PlayerEntity) = player.healMultiplier
	
	@JvmStatic
	fun setHealMultiplier(player: PlayerEntity, value: Float) {
		player.healMultiplier = value
	}
	
	@JvmStatic
	fun getExhaustionMultiplier(player: PlayerEntity) = player.exhaustionMultiplier
	
	@JvmStatic
	fun setExhaustionMultiplier(player: PlayerEntity, value: Float) {
		player.exhaustionMultiplier = value
	}
	
	@JvmStatic
	fun isFrozen(player: PlayerEntity) = player.frozen
	
	@JvmStatic
	fun setFrozen(player: PlayerEntity, value: Boolean) {
		player.frozen = value
	}
}