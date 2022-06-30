package dev.vini2003.hammer.zone.registry.client;

import dev.vini2003.hammer.zone.api.common.zone.Zone;

import javax.annotation.Nullable;

public class HZValues {
	public static boolean ZONE_EDITOR = false;
	
	@Nullable
	private static Zone MOUSE_SELECTED_ZONE = null;
	
	@Nullable
	private static Zone COMMAND_SELECTED_ZONE = null;
	
	public static Zone getSelectedZone() {
		if (COMMAND_SELECTED_ZONE != null) {
			return COMMAND_SELECTED_ZONE;
		} else {
			return MOUSE_SELECTED_ZONE;
		}
	}
	
	@Nullable
	public static Zone getMouseSelectedZone() {
		return MOUSE_SELECTED_ZONE;
	}
	
	@Nullable
	public static Zone getCommandSelectedZone() {
		return COMMAND_SELECTED_ZONE;
	}
	
	public static void setMouseSelectedZone(Zone zone) {
		MOUSE_SELECTED_ZONE = zone;
	}
	
	public static void setCommandSelectedZone(Zone zone) {
		COMMAND_SELECTED_ZONE = zone;
	}
}
