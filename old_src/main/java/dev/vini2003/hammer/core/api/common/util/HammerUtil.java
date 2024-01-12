package dev.vini2003.hammer.core.api.common.util;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.Objects;

public class HammerUtil {
	public static void executeIfModuleEnabled(String moduleId, Runnable runnable) {
		if (isModuleEnabled(moduleId)) {
			runnable.run();
		}
	}
	
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
	
	public static void initializeIfModuleEnabled(String moduleId, String initializerClassPath) {
		executeIfModuleEnabled(moduleId, () -> {
			try {
				((ModInitializer) Class.forName(initializerClassPath).newInstance()).onInitialize();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public static void initializeClientIfModuleEnabled(String moduleId, String initializerClassPath) {
		executeIfModuleEnabled(moduleId, () -> {
			try {
				((ClientModInitializer) Class.forName(initializerClassPath).newInstance()).onInitializeClient();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public static void initializeDedicatedServerIfModuleEnabled(String moduleId, String initializerClassPath) {
		executeIfModuleEnabled(moduleId, () -> {
			try {
				((DedicatedServerModInitializer) Class.forName(initializerClassPath).newInstance()).onInitializeServer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
