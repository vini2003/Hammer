package dev.vini2003.hammer.core.api.common.util.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class ModUtilImpl {
	public static boolean isModLoaded(String id) {
		return FabricLoader.getInstance().isModLoaded(id);
	}
}
