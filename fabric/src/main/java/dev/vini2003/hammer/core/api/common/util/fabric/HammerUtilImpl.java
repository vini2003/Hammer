package dev.vini2003.hammer.core.api.common.util.fabric;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class HammerUtilImpl {
	@Nullable
	private static Set<String> enabledModules = null;
	
	public static boolean isModuleEnabled(String moduleId) {
		if (enabledModules == null) {
			enabledModules = new HashSet<>();
			
			FabricLoader.getInstance()
						.getAllMods()
						.stream()
						.map(ModContainer::getMetadata)
						.map(metadata -> metadata.getCustomValue("hammer"))
						.filter(Objects::nonNull)
						.map(customValue -> {
							try {
								return customValue.getAsObject();
							} catch (Exception e) {
								return null;
							}
						})
						.filter(Objects::nonNull)
						.map(module -> module.get("modules"))
						.filter(Objects::nonNull)
						.map(customValue -> {
							try {
								return customValue.getAsArray();
							} catch (Exception e) {
								return null;
							}
						})
						.filter(Objects::nonNull)
						.forEach(modules ->
								modules.forEach(module -> enabledModules.add(module.getAsString()))
						);
		}
		
		return enabledModules.contains(moduleId);
	}
}
