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
}
