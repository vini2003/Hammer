package dev.vini2003.hammer.core.api.common.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.vini2003.hammer.core.api.common.exception.NoPlatformImplementationException;

public class HammerUtil {
	public static void executeIfModuleEnabled(String moduleId, Runnable runnable) {
		if (isModuleEnabled(moduleId)) {
			runnable.run();
		}
	}
	
	@ExpectPlatform
	public static boolean isModuleEnabled(String moduleId) {
		throw new NoPlatformImplementationException();
	}
	
	public static void initializeIfModuleEnabled(String moduleId, String initializerClassPath) {
		executeIfModuleEnabled(moduleId, () -> {
			try {
				Class.forName(initializerClassPath).getMethod("onInitialize").invoke(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public static void initializeClientIfModuleEnabled(String moduleId, String initializerClassPath) {
		executeIfModuleEnabled(moduleId, () -> {
			try {
				Class.forName(initializerClassPath).getMethod("onInitializeClient").invoke(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public static void initializeDedicatedServerIfModuleEnabled(String moduleId, String initializerClassPath) {
		executeIfModuleEnabled(moduleId, () -> {
			try {
				Class.forName(initializerClassPath).getMethod("onInitializeDedicatedServer").invoke(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
