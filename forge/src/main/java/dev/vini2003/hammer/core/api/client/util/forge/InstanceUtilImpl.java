package dev.vini2003.hammer.core.api.client.util.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class InstanceUtilImpl {
	public static Path getConfigPath() {
		return FMLPaths.CONFIGDIR.get();
	}
	
	public static Path getPersistentObjectPath() {
		return FMLPaths.GAMEDIR.get().resolve("persistent");
	}
	
	public static boolean isClient() {
		return FMLEnvironment.dist == Dist.CLIENT;
	}
	
	public static boolean isServer() {
		return FMLEnvironment.dist == Dist.DEDICATED_SERVER;
	}
	
	public static boolean isDevelopmentEnvironment() {
		return !FMLEnvironment.production;
	}
}
