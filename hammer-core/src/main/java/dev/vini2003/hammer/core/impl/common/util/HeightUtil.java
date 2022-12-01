package dev.vini2003.hammer.core.impl.common.util;

import dev.vini2003.hammer.core.api.common.util.PlayerUtil;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.player.PlayerEntity;

public class HeightUtil {
	public static float getHeight(PlayerEntity player, EntityDimensions dimensions) {
		var newHeight = dimensions.height;
		
		var headHeight = 0.25F * dimensions.height;
		var armHeight = 0.375F * dimensions.height;
		var legHeight = 0.368F * dimensions.height;
		
		if (!PlayerUtil.hasHead(player)) {
			newHeight -= headHeight;
		}
		
		if (!PlayerUtil.hasAnyArm(player) && !PlayerUtil.hasTorso(player) && !PlayerUtil.hasAnyLeg(player)) {
			newHeight -= armHeight;
		} else if (!PlayerUtil.hasHead(player) && !PlayerUtil.hasAnyArm(player) && !PlayerUtil.hasTorso(player) && PlayerUtil.hasAnyLeg(player)) {
			newHeight -= armHeight;
		}
		
		if (!PlayerUtil.hasAnyLeg(player)) {
			newHeight -= legHeight;
		}
		
		return Math.max(0.125F, newHeight);
	}
}
