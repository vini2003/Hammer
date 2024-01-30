package dev.vini2003.hammer.core.registry.client;

import dev.architectury.networking.NetworkManager;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.manager.ComponentManager;
import dev.vini2003.hammer.core.impl.common.component.holder.ComponentHolder;

import static dev.vini2003.hammer.core.registry.common.HCNetworking.SYNC_COMPONENT;
import static dev.vini2003.hammer.core.registry.common.HCNetworking.SYNC_COMPONENT_CONTAINER;

public class HCNetworking {
	public static void init() {
		// Note that this is not called often. Using a PacketByteBuf is not necessary
		// to achieve reasonable performance here.
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, SYNC_COMPONENT_CONTAINER, (buf, context) -> {
			var client = InstanceUtil.getClient();
			
			var compHolder = buf.readInt();
			var containerNbt = buf.readNbt();
			
			switch (compHolder) {
				case ComponentHolder.ENTITY -> {
					var entityId = buf.readInt();
					
					client.execute(() -> {
						if (client.world.getEntityById(entityId) instanceof ComponentHolder holder) {
							holder.getComponentContainer().readFromNbt(containerNbt);
						}
					});
				}
				
				case ComponentHolder.WORLD -> {
					client.execute(() -> {
						if (client.world instanceof ComponentHolder holder) {
							holder.getComponentContainer().readFromNbt(containerNbt);
						}
					});
				}
			}
		});
		
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, SYNC_COMPONENT, (buf, context) -> {
			var client = InstanceUtil.getClient();
			
			var compId = buf.readIdentifier();
			var compKey = ComponentManager.getKey(compId);
			var compHolder = buf.readInt();
			
			buf.retain();
			
			switch (compHolder) {
				case ComponentHolder.ENTITY -> {
					var entityId = buf.readInt();
					
					client.execute(() -> {
						if (client.world.getEntityById(entityId) instanceof ComponentHolder holder) {
							var component = holder.getComponentContainer().get(compKey);
							component.readFromBuf(buf);
						}
					});
				}
				
				case ComponentHolder.WORLD -> {
					client.execute(() -> {
						if (client.world instanceof ComponentHolder holder) {
							var component = holder.getComponentContainer().get(compKey);
							component.readFromBuf(buf);
						}
					});
				}
			}
		});
	}
}
