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

package dev.vini2003.hammer.gui.api.common.util;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class InGameHudUtil {
	public static InGameHud getHud() {
		var client = InstanceUtil.getClient();
		
		return client.inGameHud;
	}
	
	public static WidgetCollection.Root getCollection() {
		return (WidgetCollection.Root) getHud();
	}
	
	public static Position getLeftBarPos(PlayerEntity player) {
		return getArmorBarPos(player);
	}
	
	public static Position getRightBarPos(PlayerEntity player) {
		if (player.getAir() < player.getMaxAir()) {
			return getAirBarPos(player);
		} else {
			return getHungerBarPos(player);
		}
	}
	
	public static Position getHeartBarPos(PlayerEntity player) {
		var hud = getHud();
		
		return new Position(hud.scaledWidth / 2.0F - 91.0F, hud.scaledHeight - 39.0F);
	}
	
	public static Position getArmorBarPos(PlayerEntity player) {
		var hud = getHud();
		
		var heartsY = hud.scaledHeight - 39;
		
		var health = Math.max(player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH), Math.max(hud.renderHealthValue, MathHelper.ceil(player.getHealth())));
		var absorption = MathHelper.ceil(player.getAbsorptionAmount());
		
		var hearts = MathHelper.ceil((health + absorption) / 2.0F / 10.0F);
		
		var armorY = heartsY - (hearts - 1) * Math.max(10 - (hearts - 2), 3) - 10;
		
		if (player.getArmor() == 0) {
			return getHeartBarPos(player);
		} else {
			return new Position(hud.scaledWidth / 2.0F - 91.0F, armorY);
		}
		
	}
	
	public static Position getAirBarPos(PlayerEntity player) {
		var hud = getHud();
		
		var heartsY = hud.scaledHeight - 39;
		
		var bubblesY = heartsY - 10;
		
		var riddenEntity = player.getVehicle();
		
		if (riddenEntity instanceof LivingEntity riddenLivingEntity) {
			if (hud.getHeartCount(riddenLivingEntity) == 0) {
				bubblesY += 10;
			}
		} else if (riddenEntity == null) {
			bubblesY += 10;
		}
		
		return new Position(hud.scaledWidth / 2.0F + 91.0F - 9 * 8 - 9, bubblesY);
	}
	
	public static Position getHungerBarPos(PlayerEntity player) {
		var hud = getHud();
		
		var heartsY = hud.scaledHeight - 39;
		
		var health = Math.max(player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH), Math.max(hud.renderHealthValue, MathHelper.ceil(player.getHealth())));
		var absorption = MathHelper.ceil(player.getAbsorptionAmount());
		
		var hearts = MathHelper.ceil((health + absorption) / 2.0F / 10.0F);
		
		var minArmorDrawn = Math.max(10 - (hearts - 2), 3);
		
		var hungerY = heartsY - (hearts - 1) * minArmorDrawn - 10;
		
		var riddenEntity = player.getVehicle();
		
		if (riddenEntity instanceof LivingEntity riddenLivingEntity) {
			if (hud.getHeartCount(riddenLivingEntity) == 0) {
				hungerY += 10;
			}
		} else if (riddenEntity == null) {
			hungerY += 10;
		}
		
		return new Position(hud.scaledWidth / 2.0F + 91.0F - 9 * 8 - 9, hungerY);
	}
}
