package dev.vini2003.hammer.util.registry.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import static dev.vini2003.hammer.util.registry.common.HUNetworking.FLY_SPEED_UPDATE;
import static dev.vini2003.hammer.util.registry.common.HUNetworking.WALK_SPEED_UPDATE;

public class HUNetworking {
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(FLY_SPEED_UPDATE, (client, handler, buf, responseSender) -> {
			var speed = buf.readInt();
			
			client.execute(() -> {
				client.player.getAbilities().setFlySpeed(speed / 20.0F);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(WALK_SPEED_UPDATE, (client, handler, buf, responseSender) -> {
			var speed = buf.readInt();
			
			client.execute(() -> {
				client.player.getAbilities().setWalkSpeed(speed / 20.0F);
			});
		});
	}
}
