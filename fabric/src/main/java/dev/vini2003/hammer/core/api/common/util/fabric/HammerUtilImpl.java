package dev.vini2003.hammer.core.api.common.util.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.Objects;

public class HammerUtilImpl {
	public static boolean isModuleEnabled(String moduleId) {
		return FabricLoader.getInstance()
						   .getAllMods()
						   .stream()
						   .map(ModContainer::getMetadata)
						   .map(metadata -> metadata.getCustomValue("hammer"))
						   .filter(Objects::nonNull)
						   .map((customValue) -> {
							   try {
								   return customValue.getAsObject();
							   } catch (Exception e) {
								   return null;
							   }
						   })
						   .filter(Objects::nonNull)
						   .map(module -> module.get("modules"))
						   .filter(Objects::nonNull)
						   .map((customValue) -> {
							   try {
								   return customValue.getAsArray();
							   } catch (Exception e) {
								   return null;
							   }
						   })
						   .filter(Objects::nonNull)
						   .anyMatch(modules -> {
							   boolean[] enabled = { false };
							   
							   modules.forEach(module -> {
								   if (module.getAsString().equals(moduleId)) {
									   enabled[0] = true;
								   }
							   });
							   
							   return enabled[0];
						   });
	}
}
