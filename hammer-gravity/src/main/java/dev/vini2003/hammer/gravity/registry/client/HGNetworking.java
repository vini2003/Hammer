package dev.vini2003.hammer.gravity.registry.client;

import dev.vini2003.hammer.core.api.common.util.BufUtil;
import dev.vini2003.hammer.gravity.api.common.manager.GravityManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.World;

import static dev.vini2003.hammer.gravity.registry.common.HGNetworking.SYNC_GRAVITIES;

public class HGNetworking {
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(SYNC_GRAVITIES, new GravityManager.GravitySyncHandler());
	}
}
