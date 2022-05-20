package dev.vini2003.hammer.core.api.common.util;

import dev.vini2003.hammer.core.impl.common.accessor.PlayerEntityAccessor;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerUtil {
	public static float getAttackDamageMultiplier(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$getAttackDamageMultiplier();
	}
	
	public static void setAttackDamageMultiplier(PlayerEntity player, float multiplier) {
		((PlayerEntityAccessor) player).hammer$setAttackDamageMultiplier(multiplier);
	}
	
	public static float getDamageMultiplier(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$getDamageMultiplier();
	}
	
	public static void setDamageMultiplier(PlayerEntity player, float multiplier) {
		((PlayerEntityAccessor) player).hammer$setDamageMultiplier(multiplier);
	}
	
	public static float getFallDamageMultiplier(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$getFallDamageMultiplier();
	}
	
	public static void setFallDamageMultiplier(PlayerEntity player, float multiplier) {
		((PlayerEntityAccessor) player).hammer$setFallDamageMultiplier(multiplier);
	}
	
	public static float getMovementSpeedMultiplier(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$getMovementSpeedMultiplier();
	}
	
	public static void setMovementSpeedMultiplier(PlayerEntity player, float multiplier) {
		((PlayerEntityAccessor) player).hammer$setMovementSpeedMultiplier(multiplier);
	}
	
	public static float getJumpMultiplier(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$getJumpMultiplier();
	}
	
	public static void setJumpMultiplier(PlayerEntity player, float multiplier) {
		((PlayerEntityAccessor) player).hammer$setJumpMultiplier(multiplier);
	}
	
	public static float getHealMultiplier(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$getHealMultiplier();
	}
	
	public static void setHealMultiplier(PlayerEntity player, float multiplier) {
		((PlayerEntityAccessor) player).hammer$setHealMultiplier(multiplier);
	}
	
	public static float getExhaustionMultiplier(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$getExhaustionMultiplier();
	}
	
	public static void setExhaustionMultiplier(PlayerEntity player, float multiplier) {
		((PlayerEntityAccessor) player).hammer$setExhaustionMultiplier(multiplier);
	}
	
	public static void setFrozen(PlayerEntity player, boolean frozen) {
		((PlayerEntityAccessor) player).hammer$setFrozen(frozen);
	}
	
	public static boolean isFrozen(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$isFrozen();
	}
}
