package dev.vini2003.hammer.border.api.common.util;

import dev.vini2003.hammer.border.impl.common.border.CubicWorldBorder;
import net.minecraft.world.border.WorldBorder;

public class BorderUtil {
	public static void interpolateSize(WorldBorder border, float startSize, float endSize, long time) {
		border.interpolateSize(startSize, endSize, time);
	}
	
	public static void setSize(WorldBorder border, float size) {
		border.setSize(size);
	}
	
	public static void setCenter(WorldBorder border, float x, float y, float z) {
		var cubicBorder = (CubicWorldBorder) border;
		
		cubicBorder.setCenter(x, y, z);
	}
}
