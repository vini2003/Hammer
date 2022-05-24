package dev.vini2003.hammer.adventure;

import dev.vini2003.hammer.adventure.registry.common.HAEvents;
import net.fabricmc.api.ModInitializer;
import net.kyori.adventure.platform.fabric.FabricServerAudiences;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HA implements ModInitializer {
	private static FabricServerAudiences audiences;
	
	public static FabricServerAudiences getAudiences() {
		return audiences;
	}
	
	public static void setAudiences(FabricServerAudiences audiences) {
		HA.audiences = audiences;
	}
	
	@Override
	public void onInitialize() {
		HAEvents.init();
	}
}
