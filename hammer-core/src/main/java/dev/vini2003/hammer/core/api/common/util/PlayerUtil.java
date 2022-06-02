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
