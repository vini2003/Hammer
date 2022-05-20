package dev.vini2003.hammer.core.api.client.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;

public class InstanceUtil {
	public static FabricLoader getFabric() {
		return FabricLoader.getInstance();
	}
	
	public static Object getGame() {
		return getFabric().getGameInstance();
	}
	
	public static MinecraftClient getClient() {
		return (MinecraftClient) getGame();
	}
	
	public static MinecraftServer getServer() {
		return (MinecraftServer) getGame();
	}
	
	public static boolean isClient() {
		return getFabric().getEnvironmentType() == EnvType.CLIENT;
	}
	
	public static boolean isServer() {
		return getFabric().getEnvironmentType() == EnvType.SERVER;
	}
}
