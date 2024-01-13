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

package dev.vini2003.hammer.gravity.api.common.manager;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.gravity.registry.common.HGNetworking;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GravityManager {
	private static final Map<RegistryKey<World>, Float> GRAVITIES = new ConcurrentHashMap<>();
	
	public static float get(World world) {
		return get(world.getRegistryKey());
	}
	
	public static float get(RegistryKey<World> world) {
		return GRAVITIES.getOrDefault(world, 0.08F);
	}
	
	public static void set(World world, float gravity) {
		set(world.getRegistryKey(), gravity);
	}
	
	public static void set(RegistryKey<World> world, float gravity) {
		GRAVITIES.put(world, gravity);
	}
	
	public static void reset() {
		GRAVITIES.clear();
	}
	
	public static void reset(World world) {
		reset(world.getRegistryKey());
	}
	
	public static void reset(RegistryKey<World> world) {
		GRAVITIES.remove(world);
	}
	
	private static void sync() {
		try {
			if (InstanceUtil.isServer()) {
				syncOnServer();
			} else {
				syncOnClient();
			}
		} catch (Exception e) {
			HC.LOGGER.error("Failed to synchronize gravities!");
		}
	}
	
	private static void syncOnClient() {
		var players = InstanceUtil.getClient().getServer().getPlayerManager().getPlayerList();
		
		syncWith(players);
	}
	
	private static void syncOnServer() {
		var players = InstanceUtil.getServer().getPlayerManager().getPlayerList();
		
		syncWith(players);
	}
	
	private static void syncWith(List<ServerPlayerEntity> players) {
		var buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(GRAVITIES.size());
		
		for (var entry : GRAVITIES.entrySet()) {
			buf.hammer$writeRegistryKey(entry.getKey());
			buf.writeFloat(entry.getValue());
		}
		
		for (var player : players) {
			NetworkManager.sendToPlayer(player, HGNetworking.SYNC_GRAVITIES, new PacketByteBuf(buf.duplicate()));
		}
	}
	
	private static void syncWith(ServerPlayerEntity player) {
		var buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(GRAVITIES.size());
		
		for (var entry : GRAVITIES.entrySet()) {
			buf.hammer$writeRegistryKey(entry.getKey());
			buf.writeFloat(entry.getValue());
		}
		
		NetworkManager.sendToPlayer(player, HGNetworking.SYNC_GRAVITIES, buf);
	}
	
	public static final class GravitySyncHandler implements NetworkManager.NetworkReceiver {
		@Override
		public void receive(PacketByteBuf buf, NetworkManager.PacketContext context) {
			var size = buf.readInt();
			
			reset();
			
			for (var i = 0; i < size; ++i) {
				var world = buf.<World>hammer$readRegistryKey();
				var gravity = buf.readFloat();
				
				set(world, gravity);
			}
		}
	}
	
	public static final class JoinListener implements PlayerEvent.PlayerJoin {
		@Override
		public void join(ServerPlayerEntity player) {
			syncWith(player);
		}
	}
}
