package dev.vini2003.hammer.core.impl.common.util;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.player.PlayerEntity;

public class HeightUtil {
	public static float getHeight(PlayerEntity player, EntityDimensions dimensions) {
		var newHeight = dimensions.height;
		
		var headHeight = 0.25F * dimensions.height;
		var armHeight = 0.375F * dimensions.height;
		var legHeight = 0.368F * dimensions.height;
		
		if (!player.hammer$hasHead()) {
			newHeight -= headHeight;
		}
		
		if (!player.hammer$hasAnyArm() && !player.hammer$hasTorso() && !player.hammer$hasAnyLeg()) {
			newHeight -= armHeight;
		} else if (!player.hammer$hasHead() && !player.hammer$hasAnyArm() && !player.hammer$hasTorso() && player.hammer$hasAnyLeg()) {
			newHeight -= armHeight;
		}
		
		if (!player.hammer$hasAnyLeg()) {
			newHeight -= legHeight;
		}
		
		return Math.max(0.125F, newHeight);
	}
}
