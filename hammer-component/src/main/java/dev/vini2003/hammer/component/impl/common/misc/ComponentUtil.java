package dev.vini2003.hammer.component.impl.common.misc;

import dev.vini2003.hammer.component.api.common.component.Component;
import dev.vini2003.hammer.component.api.common.component.ComponentKey;
import dev.vini2003.hammer.component.impl.common.component.ComponentHolder;
import dev.vini2003.hammer.component.impl.common.state.ComponentPersistentState;
import dev.vini2003.hammer.component.registry.common.HCNetworking;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ComponentUtil {
	public static final Map<ComponentKey<? extends Component>, Function<Entity, ? extends Component>> ENTITY_ATTACHERS = new HashMap<>();
	public static final Map<ComponentKey<? extends Component>, Function<World, ? extends Component>> WORLD_ATTACHERS = new HashMap<>();
	
	public static void syncKey(PacketByteBuf buf) {
		if (InstanceUtil.isServer()) {
			syncKeyOnServer(buf);
		} else {
			syncKeyOnClient(buf);
		}
	}
	
	private static void syncKeyOnClient(PacketByteBuf buf) {
		var server = InstanceUtil.getClient().getServer();
		
		syncKeyWith(server.getPlayerManager().getPlayerList(), buf);
	}
	
	private static void syncKeyOnServer(PacketByteBuf buf) {
		var server = InstanceUtil.getServer();
		
		syncKeyWith(server.getPlayerManager().getPlayerList(), buf);
	}
	
	private static void syncKeyWith(Collection<ServerPlayerEntity> players, PacketByteBuf buf) {
		for (var player : players) {
			ServerPlayNetworking.send(player, HCNetworking.SYNC_COMPONENT, PacketByteBufs.duplicate(buf));
		}
	}
	
	public static void attachToEntity(Entity entity) {
		if (entity instanceof ComponentHolder holder) {
			var container = holder.getComponentContainer();
			
			for (var entry : ENTITY_ATTACHERS.entrySet()) {
				var key = entry.getKey();
				var function = entry.getValue();
				
				var component = function.apply(entity);
				
				if (component != null) {
					container.put(key, component);
				}
			}
		}
	}
	
	public static void attachToWorld(World world) {
		if (world instanceof ComponentHolder holder) {
			var container = holder.getComponentContainer();
			
			for (var entry : WORLD_ATTACHERS.entrySet()) {
				var key = entry.getKey();
				var function = entry.getValue();
				
				var component = function.apply(world);
				
				if (component != null) {
					container.put(key, component);
				}
			}
		}
	}
	
	public static void attachToPersistentState(ComponentPersistentState state) {
		var container = state.getComponentContainer();
		
		for (var entry : WORLD_ATTACHERS.entrySet()) {
			var key = entry.getKey();
			var function = entry.getValue();
			
			var component = function.apply(state.getWorld());
			
			if (component != null) {
				container.put(key, component);
			}
		}
	}
}
