package dev.vini2003.hammer.core.api.common.util.forge;

import net.minecraftforge.fml.loading.LoadingModList;

public class ModUtilImpl {
	public static boolean isModLoaded(String id) {
		return LoadingModList.get()
							 .getMods()
							 .stream()
							 .anyMatch(modInfo -> modInfo.getModId().equals(id));
	}
}
