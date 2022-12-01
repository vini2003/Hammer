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

package dev.vini2003.hammer.core.impl.common.accessor;

public interface PlayerEntityAccessor {
	float hammer$getAttackDamageMultiplier();
	
	void hammer$setAttackDamageMultiplier(float multiplier);
	
	float hammer$getDamageMultiplier();
	
	void hammer$setDamageMultiplier(float multiplier);
	
	float hammer$getFallDamageMultiplier();
	
	void hammer$setFallDamageMultiplier(float multiplier);
	
	float hammer$getMovementSpeedMultiplier();
	
	void hammer$setMovementSpeedMultiplier(float multiplier);
	
	float hammer$getJumpMultiplier();
	
	void hammer$setJumpMultiplier(float multiplier);
	
	float hammer$getHealMultiplier();
	
	void hammer$setHealMultiplier(float multiplier);
	
	float hammer$getExhaustionMultiplier();
	
	void hammer$setExhaustionMultiplier(float multiplier);
	
	boolean hammer$isFrozen();
	
	void hammer$setFrozen(boolean frozen);
	
	boolean hammer$hasHead();
	
	void hammer$setHead(boolean head);
	
	boolean hammer$hasTorso();
	
	void hammer$setTorso(boolean torso);
	
	boolean hammer$hasLeftArm();
	
	void hammer$setLeftArm(boolean leftArm);
	
	boolean hammer$hasRightArm();
	
	void hammer$setRightArm(boolean rightArm);
	
	boolean hammer$hasLeftLeg();
	
	void hammer$setLeftLeg(boolean leftLeg);
	
	boolean hammer$hasRightLeg();
	
	void hammer$setRightLeg(boolean rightLeg);
	
	default boolean hammer$hasAnyArm() {
		return hammer$hasLeftArm() || hammer$hasRightArm();
	}
	
	default boolean hammer$hasAnyLeg() {
		return hammer$hasLeftLeg() || hammer$hasRightLeg();
	}
	
	void hammer$setAllowMovement(boolean allowsMovement);
	
	boolean hammer$allowMovement();
	
	void hammer$setAllowInteraction(boolean allowsInteraction);
	
	boolean hammer$allowInteraction();
}
