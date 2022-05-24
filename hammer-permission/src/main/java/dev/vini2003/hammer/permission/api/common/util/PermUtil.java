package dev.vini2003.hammer.permission.api.common.util;

import dev.vini2003.hammer.permission.HP;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.query.QueryOptions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.PlayerManager;

import java.util.UUID;

public class PermUtil {
	public static LuckPerms getLuckPerms() {
		return HP.getLuckPerms();
	}
	
	public static UserManager getUserManager() {
		return getLuckPerms().getUserManager();
	}
	
	public static GroupManager getGroupManager() {
		return getLuckPerms().getGroupManager();
	}
	
	public static ContextManager getContextManager() {
		return getLuckPerms().getContextManager();
	}
	
	public static QueryOptions getStaticQueryOptions() {
		return getLuckPerms().getContextManager().getStaticQueryOptions();
	}
	
	public static Group createGroup(String name) {
		try {
			return getGroupManager().createAndLoadGroup(name).get();
		} catch (Exception ignored) {
			return null;
		}
	}
	
	public static Group getOrCreateGroup(String name) {
		var group = getGroupManager().getGroup(name);
		
		return group != null ? group : createGroup(name);
	}
	
	public static User getUser(PlayerEntity player) {
		return getUserManager().getUser(player.getUuid());
	}
	
	public static User getUser(UUID uuid) {
		return getUserManager().getUser(uuid);
	}
	
	// TODO: Fix nullability issues!
	public static void saveUser(PlayerEntity player) {
		getUserManager().saveUser(getUser(player.getUuid()));
	}
	
	public static void saveUser(UUID uuid) {
		getUserManager().saveUser(getUser(uuid));
	}
	
	public static void saveGroup(Group group) {
		getGroupManager().saveGroup(group);
	}
	
	public static void addNode(PlayerEntity player, Node node) {
		addNode(player.getUuid(), node);
	}
	
	public static  void addNode(UUID uuid, Node node) {
		var user = getUser(uuid);
		
		if (user != null) {
			var data = user.data();
			
			data.add(node);
			
			saveUser(uuid);
		}
	}
	
	public static void removeNode(PlayerEntity player, Node node) {
		removeNode(player.getUuid(), node);
	}
	
	public static  void removeNode(UUID uuid, Node node) {
		var user = getUser(uuid);
		
		if (user != null) {
			var data = user.data();
			
			data.remove(node);
			
			saveUser(uuid);
		}
	}
	
	public static boolean hasPermission(PlayerEntity player, String permission) {
		return hasPermission(player.getUuid(), permission);
	}
	
	public static boolean hasPermission(UUID uuid, String permission) {
		var user = getUser(uuid);
		
		if (user != null) {
			return user.getCachedData().getPermissionData(getStaticQueryOptions()).checkPermission(permission).asBoolean();
		} else {
			return false;
		}
	}
}
