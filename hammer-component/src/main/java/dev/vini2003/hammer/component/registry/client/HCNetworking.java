package dev.vini2003.hammer.component.registry.client;

import dev.vini2003.hammer.component.api.common.manager.ComponentManager;
import dev.vini2003.hammer.component.impl.common.component.ComponentHolder;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import static dev.vini2003.hammer.component.registry.common.HCNetworking.SYNC_COMPONENT;
import static dev.vini2003.hammer.component.registry.common.HCNetworking.SYNC_COMPONENT_CONTAINER;

public class HCNetworking {
	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(SYNC_COMPONENT_CONTAINER, (client, handler, buf, responseSender) -> {
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
		
		ClientPlayNetworking.registerGlobalReceiver(SYNC_COMPONENT, (client, handler, buf, responseSender) -> {
			var compId = buf.readIdentifier();
			var compKey = ComponentManager.getKey(compId);
			var compHolder = buf.readInt();
			var compNbt = buf.readNbt();
			
			switch (compHolder) {
				case ComponentHolder.ENTITY -> {
					var entityId = buf.readInt();
					
					client.execute(() -> {
						if (client.world.getEntityById(entityId) instanceof ComponentHolder holder) {
							var component = holder.getComponentContainer().get(compKey);
							component.readFromNbt(compNbt);
						}
					});
				}
				
				case ComponentHolder.WORLD -> {
					client.execute(() -> {
						if (client.world instanceof ComponentHolder holder) {
							var component = holder.getComponentContainer().get(compKey);
							component.readFromNbt(compNbt);
						}
					});
				}
			}
		});
	}
}
