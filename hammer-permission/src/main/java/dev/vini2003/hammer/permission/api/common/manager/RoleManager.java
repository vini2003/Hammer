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
	
	public static Role getRoleByName(String name) {
		return ROLES_BY_NAME.get(name);
	}
	
	public static String getRolePrefix(PlayerEntity player) {
		Role maxRole = null;
		
		for (var role : ROLES) {
			if (role.isIn(player)) {
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
			if (role.isIn(player)) {
				if (maxRole == null || role.getPrefixWeight() > maxRole.getPrefixWeight()) {
					maxRole = role;
				}
			}
		}
		
		return maxRole != null ? TextColor.fromRgb(maxRole.getPrefixColor()) : null;
	}
	
	public static Iterator<Role> roles() {
		return ROLES.iterator();
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
