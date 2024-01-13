package dev.vini2003.hammer.core.api.common.util.forge;

import net.minecraftforge.fml.ModList;

public class ModUtilImpl {
	public static boolean isModLoaded(String id) {
		return ModList.get().isLoaded(id);
	}
}
