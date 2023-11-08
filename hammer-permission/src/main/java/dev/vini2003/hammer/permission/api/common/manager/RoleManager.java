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

package dev.vini2003.hammer.permission.api.common.manager;

import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.permission.api.common.role.Role;
import dev.vini2003.hammer.permission.registry.common.HPNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TextColor;

import java.util.*;

public class RoleManager {
	private static final List<Role> ROLES = new ArrayList<>();
	private static final Map<String, Role> ROLES_BY_NAME = new HashMap<>();
	
	public static void register(Role role) {
		ROLES.add(role);
		ROLES_BY_NAME.put(role.getName(), role);
	}
	
	public static Collection<Role> getRoles() {
		return ROLES;
	}
	
	public static Role getRoleByName(String name) {
		return ROLES_BY_NAME.get(name);
	}
	
	public static String getRolePrefix(PlayerEntity player) {
		Role maxRole = null;
		
		for (var role : ROLES) {
			if (player.hammer$hasRole(role)) {
				if (maxRole == null || role.getPrefixWeight() > maxRole.getPrefixWeight()) {
					maxRole = role;
				}
			}
		}
		
		return maxRole != null ? maxRole.getPrefix() : null;
	}
	
	public static TextColor getRolePrefixColor(PlayerEntity player) {
		Role maxRole = null;
		
		for (var role : ROLES) {
			if (player.hammer$hasRole(role)) {
				if (maxRole == null || role.getPrefixWeight() > maxRole.getPrefixWeight()) {
					maxRole = role;
				}
			}
		}
		
		return maxRole != null ? TextColor.fromRgb(maxRole.getPrefixColor()) : null;
	}
	
	public static Collection<Role> roles() {
		return ROLES;
	}
	
	private static void syncWith(List<ServerPlayerEntity> players) {
		for (var role : ROLES) {
			var buf = PacketByteBufs.create();
			buf.writeString(role.getName());
			
			buf.writeInt(role.getHolders().size());
			
			for (var holder : role.getHolders()) {
				buf.writeUuid(holder);
			}
			
			for (var player : players) {
				ServerPlayNetworking.send(player, HPNetworking.SYNC_ROLES, PacketByteBufs.duplicate(buf));
			}
		}
		
	}
	
	public static final class JoinListener implements ServerPlayConnectionEvents.Join {
		@Override
		public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
			var players = ImmutableList.<ServerPlayerEntity>builder().addAll(server.getPlayerManager().getPlayerList()).add(handler.getPlayer()).build();
			
			syncWith(players);
		}
	}
}
