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

package dev.vini2003.hammer.core.api.common.util.extension

import dev.vini2003.hammer.core.impl.common.accessor.PlayerEntityAccessor
import net.minecraft.entity.player.PlayerEntity

var PlayerEntity.attackDamageMultiplier
	get() = (this as PlayerEntityAccessor).attackDamageMultiplier
	set(value) {
		(this as PlayerEntityAccessor).attackDamageMultiplier = value
	}

var PlayerEntity.damageMultiplier
	get() = (this as PlayerEntityAccessor).damageMultiplier
	set(value) {
		(this as PlayerEntityAccessor).damageMultiplier = value
	}

var PlayerEntity.fallDamageMultiplier
	get() = (this as PlayerEntityAccessor).fallDamageMultiplier
	set(value) {
		(this as PlayerEntityAccessor).fallDamageMultiplier = value
	}

var PlayerEntity.movementSpeedMultiplier
	get() = (this as PlayerEntityAccessor).movementSpeedMultiplier
	set(value) {
		(this as PlayerEntityAccessor).movementSpeedMultiplier = value
	}

var PlayerEntity.jumpMultiplier
	get() = (this as PlayerEntityAccessor).jumpMultiplier
	set(value) {
		(this as PlayerEntityAccessor).jumpMultiplier = value
	}

var PlayerEntity.healMultiplier
	get() = (this as PlayerEntityAccessor).healMultiplier
	set(value) {
		(this as PlayerEntityAccessor).healMultiplier = value
	}

var PlayerEntity.exhaustionMultiplier
	get() = (this as PlayerEntityAccessor).exhaustionMultiplier
	set(value) {
		(this as PlayerEntityAccessor).exhaustionMultiplier = value
	}

var PlayerEntity.frozen
	get() = (this as PlayerEntityAccessor).frozen
	set(value) {
		(this as PlayerEntityAccessor).frozen = value
	}