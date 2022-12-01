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

package dev.vini2003.hammer.core.api.common.util;

import dev.vini2003.hammer.core.impl.common.accessor.PlayerEntityAccessor;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerUtil {
	public static float getAttackDamageMultiplier(PlayerEntity player) {
		if (player == null) return 1.0F;
		return ((PlayerEntityAccessor) player).hammer$getAttackDamageMultiplier();
	}
	
	public static void setAttackDamageMultiplier(PlayerEntity player, float multiplier) {
		if (player == null) return;
		((PlayerEntityAccessor) player).hammer$setAttackDamageMultiplier(multiplier);
	}
	
	public static float getDamageMultiplier(PlayerEntity player) {
		if (player == null) return 1.0F;
		return ((PlayerEntityAccessor) player).hammer$getDamageMultiplier();
	}
	
	public static void setDamageMultiplier(PlayerEntity player, float multiplier) {
		if (player == null) return;
		((PlayerEntityAccessor) player).hammer$setDamageMultiplier(multiplier);
	}
	
	public static float getFallDamageMultiplier(PlayerEntity player) {
		if (player == null) return 1.0F;
		return ((PlayerEntityAccessor) player).hammer$getFallDamageMultiplier();
	}
	
	public static void setFallDamageMultiplier(PlayerEntity player, float multiplier) {
		if (player == null) return;
		((PlayerEntityAccessor) player).hammer$setFallDamageMultiplier(multiplier);
	}
	
	public static float getMovementSpeedMultiplier(PlayerEntity player) {
		if (player == null) return 1.0F;
		return ((PlayerEntityAccessor) player).hammer$getMovementSpeedMultiplier();
	}
	
	public static void setMovementSpeedMultiplier(PlayerEntity player, float multiplier) {
		if (player == null) return;
		((PlayerEntityAccessor) player).hammer$setMovementSpeedMultiplier(multiplier);
	}
	
	public static float getJumpMultiplier(PlayerEntity player) {
		if (player == null) return 1.0F;
		return ((PlayerEntityAccessor) player).hammer$getJumpMultiplier();
	}
	
	public static void setJumpMultiplier(PlayerEntity player, float multiplier) {
		if (player == null) return;
		((PlayerEntityAccessor) player).hammer$setJumpMultiplier(multiplier);
	}
	
	public static float getHealMultiplier(PlayerEntity player) {
		if (player == null) return 1.0F;
		return ((PlayerEntityAccessor) player).hammer$getHealMultiplier();
	}
	
	public static void setHealMultiplier(PlayerEntity player, float multiplier) {
		if (player == null) return;
		((PlayerEntityAccessor) player).hammer$setHealMultiplier(multiplier);
	}
	
	public static float getExhaustionMultiplier(PlayerEntity player) {
		if (player == null) return 1.0F;
		return ((PlayerEntityAccessor) player).hammer$getExhaustionMultiplier();
	}
	
	public static void setExhaustionMultiplier(PlayerEntity player, float multiplier) {
		if (player == null) return;
		((PlayerEntityAccessor) player).hammer$setExhaustionMultiplier(multiplier);
	}
	
	public static boolean isFrozen(PlayerEntity player) {
		if (player == null) return false;
		return ((PlayerEntityAccessor) player).hammer$isFrozen();
	}
	
	public static void setFrozen(PlayerEntity player, boolean frozen) {
		if (player == null) return;
		((PlayerEntityAccessor) player).hammer$setFrozen(frozen);
	}
	
	public static boolean hasHead(PlayerEntity player) {
		if (player == null) return true;
		return ((PlayerEntityAccessor) player).hammer$hasHead();
	}
	
	public static void setHead(PlayerEntity player, boolean hasHead) {
		if (player == null) return;
		((PlayerEntityAccessor) player).hammer$setHead(hasHead);
	}
	
	public static boolean hasTorso(PlayerEntity player) {
		if (player == null) return true;
		return ((PlayerEntityAccessor) player).hammer$hasTorso();
	}
	
	public static void setTorso(PlayerEntity player, boolean hasTorso) {
		if (player == null) return;
		((PlayerEntityAccessor) player).hammer$setTorso(hasTorso);
	}
	
	public static boolean hasLeftArm(PlayerEntity player) {
		if (player == null) return true;
		return ((PlayerEntityAccessor) player).hammer$hasLeftArm();
	}
	
	public static void setLeftArm(PlayerEntity player, boolean hasLeftArm) {
		if (player == null) return;
		((PlayerEntityAccessor) player).hammer$setLeftArm(hasLeftArm);
	}
	
	public static boolean hasRightArm(PlayerEntity player) {
		if (player == null) return true;
		return ((PlayerEntityAccessor) player).hammer$hasRightArm();
	}
	
	public static void setRightArm(PlayerEntity player, boolean hasRightArm) {
		if (player == null) return;
		((PlayerEntityAccessor) player).hammer$setRightArm(hasRightArm);
	}
	
	public static boolean hasLeftLeg(PlayerEntity player) {
		if (player == null) return true;
		return ((PlayerEntityAccessor) player).hammer$hasLeftLeg();
	}
	
	public static void setLeftLeg(PlayerEntity player, boolean hasLeftLeg) {
		if (player == null) return;
		((PlayerEntityAccessor) player).hammer$setLeftLeg(hasLeftLeg);
	}
	
	public static boolean hasRightLeg(PlayerEntity player) {
		if (player == null) return true;
		return ((PlayerEntityAccessor) player).hammer$hasRightLeg();
	}
	
	public static void setRightLeg(PlayerEntity player, boolean hasRightLeg) {
		if (player == null) return;
		((PlayerEntityAccessor) player).hammer$setRightLeg(hasRightLeg);
	}
	
	public static boolean hasAnyArm(PlayerEntity player) {
		if (player == null) return true;
		return ((PlayerEntityAccessor) player).hammer$hasAnyArm();
	}
	
	public static boolean hasAnyLeg(PlayerEntity player) {
		if (player == null) return true;
		return ((PlayerEntityAccessor) player).hammer$hasAnyLeg();
	}
	
	public static boolean allowMovement(PlayerEntity player) {
		if (player == null) return true;
		return ((PlayerEntityAccessor) player).hammer$allowMovement();
	}
	
	public static void setAllowMovement(PlayerEntity player, boolean allowMovement) {
		if (player == null) return;
		((PlayerEntityAccessor) player).hammer$setAllowMovement(allowMovement);
	}
	
	public static boolean allowInteraction(PlayerEntity player) {
		if (player == null) return true;
		return ((PlayerEntityAccessor) player).hammer$allowInteraction();
	}
	
	public static void setAllowInteraction(PlayerEntity player, boolean allowInteraction) {
		if (player == null) return;
		((PlayerEntityAccessor) player).hammer$setAllowInteraction(allowInteraction);
	}
}
