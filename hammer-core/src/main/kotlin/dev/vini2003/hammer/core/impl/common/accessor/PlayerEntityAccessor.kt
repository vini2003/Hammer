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

package dev.vini2003.hammer.core.impl.common.accessor

interface PlayerEntityAccessor {
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