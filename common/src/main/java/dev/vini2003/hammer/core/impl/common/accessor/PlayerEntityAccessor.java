package dev.vini2003.hammer.core.impl.common.accessor;

import dev.vini2003.hammer.core.api.common.data.TrackedDataHandler;
import dev.vini2003.hammer.core.api.common.exception.NoMixinException;

public interface PlayerEntityAccessor {
	default float hammer$getAttackDamageMultiplier() {
		throw new NoMixinException();
	}
	
	default void hammer$setAttackDamageMultiplier(float multiplier) {
		throw new NoMixinException();
	}
	
	default float hammer$getDamageMultiplier() {
		throw new NoMixinException();
	}
	
	default void hammer$setDamageMultiplier(float multiplier) {
		throw new NoMixinException();
	}
	
	default float hammer$getFallDamageMultiplier() {
		throw new NoMixinException();
	}
	
	default void hammer$setFallDamageMultiplier(float multiplier) {
		throw new NoMixinException();
	}
	
	default float hammer$getMovementSpeedMultiplier() {
		throw new NoMixinException();
	}
	
	default void hammer$setMovementSpeedMultiplier(float multiplier) {
		throw new NoMixinException();
	}
	
	default float hammer$getJumpMultiplier() {
		throw new NoMixinException();
	}
	
	default void hammer$setJumpMultiplier(float multiplier) {
		throw new NoMixinException();
	}
	
	default float hammer$getHealMultiplier() {
		throw new NoMixinException();
	}
	
	default void hammer$setHealMultiplier(float multiplier) {
		throw new NoMixinException();
	}
	
	default float hammer$getExhaustionMultiplier() {
		throw new NoMixinException();
	}
	
	default void hammer$setExhaustionMultiplier(float multiplier) {
		throw new NoMixinException();
	}
	
	default boolean hammer$isFrozen() {
		throw new NoMixinException();
	}
	
	default void hammer$setFrozen(boolean frozen) {
		throw new NoMixinException();
	}
	
	default boolean hammer$hasHead() {
		throw new NoMixinException();
	}
	
	default void hammer$setHead(boolean head) {
		throw new NoMixinException();
	}
	
	default boolean hammer$hasTorso() {
		throw new NoMixinException();
	}
	
	default void hammer$setTorso(boolean torso) {
		throw new NoMixinException();
	}
	
	default boolean hammer$hasLeftArm() {
		throw new NoMixinException();
	}
	
	default void hammer$setLeftArm(boolean leftArm) {
		throw new NoMixinException();
	}
	
	default boolean hammer$hasRightArm() {
		throw new NoMixinException();
	}
	
	default void hammer$setRightArm(boolean rightArm) {
		throw new NoMixinException();
	}
	
	default boolean hammer$hasLeftLeg() {
		throw new NoMixinException();
	}
	
	default void hammer$setLeftLeg(boolean leftLeg) {
		throw new NoMixinException();
	}
	
	default boolean hammer$hasRightLeg() {
		throw new NoMixinException();
	}
	
	default void hammer$setRightLeg(boolean rightLeg) {
		throw new NoMixinException();
	}
	
	default void hammer$setAllowMovement(boolean allowsMovement) {
		throw new NoMixinException();
	}
	
	default boolean hammer$allowMovement() {
		throw new NoMixinException();
	}
	
	default void hammer$setAllowInteraction(boolean allowsInteraction) {
		throw new NoMixinException();
	}
	
	default boolean hammer$allowInteraction() {
		throw new NoMixinException();
	}
	
	default boolean hammer$hasAnyArm() {
		return hammer$hasLeftArm() || hammer$hasRightArm();
	}
	
	default boolean hammer$hasAnyLeg() {
		return hammer$hasLeftLeg() || hammer$hasRightLeg();
	}
}