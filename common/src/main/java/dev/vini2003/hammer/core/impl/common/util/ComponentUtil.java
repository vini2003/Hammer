/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.core.impl.common.util;

import dev.architectury.networking.NetworkManager;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.component.base.Component;
import dev.vini2003.hammer.core.api.common.component.base.key.ComponentKey;
import dev.vini2003.hammer.core.impl.common.component.holder.ComponentHolder;
import dev.vini2003.hammer.core.impl.common.state.ComponentPersistentState;
import dev.vini2003.hammer.core.registry.common.HCNetworking;
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
	
	public static void syncKey(World world, PacketByteBuf buf) {
		if (InstanceUtil.isServer()) {
			syncKeyOnServer(world, buf);
		} else {
			syncKeyOnClient(world, buf);
		}
	}
	
	private static void syncKeyOnClient(World world, PacketByteBuf buf) {
		var server = InstanceUtil.getClient().getServer();
		
		if (server != null) {
			syncKeyWith(world, server.getPlayerManager().getPlayerList(), buf);
		}
	}
	
	private static void syncKeyOnServer(World world, PacketByteBuf buf) {
		var server = InstanceUtil.getServer();
		
		syncKeyWith(world, server.getPlayerManager().getPlayerList(), buf);
	}
	
	private static void syncKeyWith(World world, Collection<ServerPlayerEntity> players, PacketByteBuf buf) {
		for (var player : players) {
			if (player.getWorld().getRegistryKey().equals(world.getRegistryKey())) {
				NetworkManager.sendToPlayer(player, HCNetworking.SYNC_COMPONENT, new PacketByteBuf(buf.duplicate()));
			}
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
