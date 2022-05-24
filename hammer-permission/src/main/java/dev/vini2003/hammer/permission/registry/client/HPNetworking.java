package dev.vini2003.hammer.permission.registry.client;

import dev.vini2003.hammer.permission.api.common.manager.RoleManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.ArrayList;
import java.util.UUID;

import static dev.vini2003.hammer.permission.registry.common.HPNetworking.*;

public class HPNetworking {
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(SYNC_ROLES, (client, handler, buf, responseSender) -> {
			var name = buf.readString();
			
			var holderSize = buf.readInt();
			var holders = new ArrayList<UUID>(holderSize);
			
			for (var i = 0; i < holderSize; ++i) {
				holders.add(buf.readUuid());
			}
			
			// TODO: Use List setter.
			client.execute(() -> {
				var role = RoleManager.getRoleByName(name);
				
				role.clearOnClient();
				
				for (var holder : holders) {
					role.addToClient(holder);
				}
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(ADD_ROLE, (client, handler, buf, responseSender) -> {
			var name = buf.readString();
			var uuid = buf.readUuid();
			
			client.execute(() -> {
				RoleManager.getRoleByName(name).addToClient(uuid);
			});
		});
		
		ClientPlayNetworking.registerGlobalReceiver(REMOVE_ROLE, (client, handler, buf, responseSender) -> {
			var name = buf.readString();
			var uuid = buf.readUuid();
			
			client.execute(() -> {
				RoleManager.getRoleByName(name).removeFromClient(uuid);
			});
		});
	}
}
