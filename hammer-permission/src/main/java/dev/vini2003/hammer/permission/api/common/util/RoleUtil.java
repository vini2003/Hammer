package dev.vini2003.hammer.permission.api.common.util;

import dev.vini2003.hammer.permission.impl.common.accessor.PlayerEntityAccessor;
import net.minecraft.entity.player.PlayerEntity;

public class RoleUtil {
	public static void setRoleOutline(PlayerEntity player, boolean roleOutline) {
		((PlayerEntityAccessor) player).hammer$setRoleOutline(roleOutline);
	}
	
	public static boolean hasRoleOutline(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$hasRoleOutline();
	}
}
