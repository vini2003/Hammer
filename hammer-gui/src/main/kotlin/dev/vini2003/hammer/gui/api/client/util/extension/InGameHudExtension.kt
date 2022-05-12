package dev.vini2003.hammer.gui.api.client.util.extension

import dev.vini2003.hammer.core.api.common.math.position.Position
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.MathHelper

fun InGameHud.getHeartBarPos(playerEntity: PlayerEntity): Position {
	return Position(scaledWidth / 2.0F - 91.0F, scaledHeight - 39.0F)
}

fun InGameHud.getArmorBarPos(playerEntity: PlayerEntity): Position {
	val renderHealthValue = renderHealthValue
	
	val heartsY: Int = scaledHeight - 39
	
	val health = Math.max(playerEntity.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH).toFloat(), Math.max(renderHealthValue, MathHelper.ceil(playerEntity.health)).toFloat())
	val absorption = MathHelper.ceil(playerEntity.getAbsorptionAmount())
	
	val hearts = MathHelper.ceil((health + absorption.toFloat()) / 2.0F / 10.0F)
	
	val armorY: Int = heartsY - (hearts - 1) * Math.max(10 - (hearts - 2), 3) - 10
	
	if (playerEntity.armor == 0) {
		return getHeartBarPos(playerEntity)
	} else {
		return Position(scaledWidth / 2.0F - 91.0F, armorY.toFloat())
	}
	
}

fun InGameHud.getAirBarPos(playerEntity: PlayerEntity): Position {
	val heartsY: Int = scaledHeight - 39

	var bubblesY: Int = heartsY - 10
	
	val riddenEntity = playerEntity.vehicle
	
	if (riddenEntity is LivingEntity) {
		if (getHeartCount(riddenEntity) == 0) {
			bubblesY += 10
		}
	} else if (riddenEntity == null) {
		bubblesY += 10
	}
	
	return Position(scaledWidth / 2.0F + 91.0F, bubblesY.toFloat())
}

fun InGameHud.getHungerBarPos(playerEntity: PlayerEntity): Position {
	val renderHealthValue = renderHealthValue
	
	val heartsY: Int = scaledHeight - 39
	
	val health = Math.max(playerEntity.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH).toFloat(), Math.max(renderHealthValue, MathHelper.ceil(playerEntity.health)).toFloat())
	val absorption = MathHelper.ceil(playerEntity.getAbsorptionAmount())
	
	val hearts = MathHelper.ceil((health + absorption.toFloat()) / 2.0F / 10.0F)
	
	val minArmorDrawn = Math.max(10 - (hearts - 2), 3)
	
	var hungerY: Int = heartsY - (hearts - 1) * minArmorDrawn - 10
	
	val riddenEntity = playerEntity.vehicle
	
	if (riddenEntity is LivingEntity) {
		if (getHeartCount(riddenEntity) == 0) {
			hungerY += 10
		}
	} else if (riddenEntity == null) {
		hungerY += 10
	}
	
	return Position(scaledWidth / 2.0F + 91.0F - 9 * 8 - 9, hungerY.toFloat())
}