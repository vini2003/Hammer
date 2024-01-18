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

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.networking.NetworkManager;
import dev.vini2003.hammer.permission.api.common.role.Role;
import dev.vini2003.hammer.permission.registry.common.HPNetworking;
import dev.vini2003.hammer.persistence.api.common.PersistentObject;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TextColor;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RoleManager {
	private static class PersistentData extends PersistentObject {
		public List<Role> roles = new ArrayList<>();
		public Map<String, Role> rolesByName = new HashMap<>();
	}
	
	private static final PersistentData PERSISTENT_DATA = PersistentObject.getOrCreate("roles", PersistentData.class);
	
	public static void createRole(Role role) {
		getRoles().add(role);
		getRolesByName().put(role.getName(), role);
		
		PERSISTENT_DATA.save();
	}
	
	public static void createRole(String name, @Nullable String prefix, @Nullable Integer prefixWeight, @Nullable Integer prefixColor) {
		createRole(new Role(name, prefix, prefixWeight, prefixColor));
	}
	
	public static void deleteRole(Role role) {
		getRoles().remove(role);
		getRolesByName().remove(role.getName());
		
		PERSISTENT_DATA.save();
	}
	
	public static Collection<Role> getRoles() {
		return PERSISTENT_DATA.roles;
	}
	
	public static Map<String, Role> getRolesByName() {
		return PERSISTENT_DATA.rolesByName;
	}
	
	public static Role getRoleByName(String name) {
		return getRolesByName().get(name);
	}
	
	public static String getRolePrefix(PlayerEntity player) {
		Role maxRole = null;
		
		for (var role : getRoles()) {
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
		
		for (var role : getRoles()) {
			if (player.hammer$hasRole(role)) {
				if (maxRole == null || role.getPrefixWeight() > maxRole.getPrefixWeight()) {
					maxRole = role;
				}
			}
		}
		
		return maxRole != null ? TextColor.fromRgb(maxRole.getPrefixColor()) : null;
	}
	
	private static void syncWith(Collection<ServerPlayerEntity> players) {
		for (var role : getRoles()) {
			var buf = new PacketByteBuf(Unpooled.buffer());
			buf.writeString(role.getName());
			
			buf.writeInt(role.getHolders().size());
			
			for (var holder : role.getHolders()) {
				buf.writeUuid(holder);
			}
			
			for (var player : players) {
				NetworkManager.sendToPlayer(player, HPNetworking.SYNC_ROLES, new PacketByteBuf(buf.duplicate()));
			}
		}
		
	}
	
	public static final class JoinListener implements PlayerEvent.PlayerJoin {
		@Override
		public void join(ServerPlayerEntity player) {
			// TODO: Verify.
			var server = player.getServer();
			if (server == null) return;
			
			syncWith(server.hammer$getPlayers());
		}
	}
}
