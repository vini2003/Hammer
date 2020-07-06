package dev.vini2003.hammer.common.entity

interface ConfigurablePlayerEntity {
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("hammer\$getAttackDamageMultiplier")
	@set:JvmName("hammer\$setAttackDamageMultiplier")
	var attackDamageMultiplier: Float
	
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("hammer\$getDamageMultiplier")
	@set:JvmName("hammer\$setDamageMultiplier")
	var damageMultiplier: Float
	
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("hammer\$getFallDamageMultiplier")
	@set:JvmName("hammer\$setFallDamageMultiplier")
	var fallDamageMultiplier: Float
	
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("hammer\$getMovementSpeedMultiplier")
	@set:JvmName("hammer\$setMovementSpeedMultiplier")
	var movementSpeedMultiplier: Float
	
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("hammer\$getJumpMultiplier")
	@set:JvmName("hammer\$setJumpMultiplier")
	var jumpMultiplier: Float
	
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("hammer\$getHealMultiplier")
	@set:JvmName("hammer\$setHealMultiplier")
	var healMultiplier: Float
	
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("hammer\$getExhaustionMultiplier")
	@set:JvmName("hammer\$setExhaustionMultiplier")
	var exhaustionMultiplier: Float
	
	@Suppress("INAPPLICABLE_JVM_NAME")
	@get:JvmName("hammer\$isFrozen")
	@set:JvmName("hammer\$setFrozen")
	var frozen: Boolean
}