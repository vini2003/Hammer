package dev.vini2003.hammer.core.api.client.util.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class InstanceUtilImpl {
	public static Path getConfigPath() {
		return FabricLoader.getInstance().getConfigDir();
	}
	
	public static Path getPersistentObjectPath() {
		return FabricLoader.getInstance().getGameDir().resolve("persistent");
	}
	
	public static boolean isClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}
	
	public static boolean isServer() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
	}
	
	public static boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}
}
