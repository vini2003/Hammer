package dev.vini2003.hammer.common.util.extension

import dev.vini2003.hammer.common.entity.ConfigurablePlayerEntity
import net.minecraft.entity.player.PlayerEntity

var PlayerEntity.attackDamageMultiplier
	get() = (this as ConfigurablePlayerEntity).attackDamageMultiplier
	set(value) {
		(this as ConfigurablePlayerEntity).attackDamageMultiplier = value
	}

var PlayerEntity.damageMultiplier
	get() = (this as ConfigurablePlayerEntity).damageMultiplier
	set(value) {
		(this as ConfigurablePlayerEntity).damageMultiplier = value
	}

var PlayerEntity.fallDamageMultiplier
	get() = (this as ConfigurablePlayerEntity).fallDamageMultiplier
	set(value) {
		(this as ConfigurablePlayerEntity).fallDamageMultiplier = value
	}

var PlayerEntity.movementSpeedMultiplier
	get() = (this as ConfigurablePlayerEntity).movementSpeedMultiplier
	set(value) {
		(this as ConfigurablePlayerEntity).movementSpeedMultiplier = value
	}

var PlayerEntity.jumpMultiplier
	get() = (this as ConfigurablePlayerEntity).jumpMultiplier
	set(value) {
		(this as ConfigurablePlayerEntity).jumpMultiplier = value
	}

var PlayerEntity.healMultiplier
	get() = (this as ConfigurablePlayerEntity).healMultiplier
	set(value) {
		(this as ConfigurablePlayerEntity).healMultiplier = value
	}

var PlayerEntity.exhaustionMultiplier
	get() = (this as ConfigurablePlayerEntity).exhaustionMultiplier
	set(value) {
		(this as ConfigurablePlayerEntity).exhaustionMultiplier = value
	}

var PlayerEntity.frozen
	get() = (this as ConfigurablePlayerEntity).frozen
	set(value) {
		(this as ConfigurablePlayerEntity).frozen = value
	}