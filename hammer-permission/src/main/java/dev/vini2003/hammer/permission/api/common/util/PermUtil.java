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

package dev.vini2003.hammer.permission.api.common.util;

import dev.vini2003.hammer.permission.HP;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryOptions;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

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
	
	@Nullable
	public static Group createGroup(String name) {
		try {
			return getGroupManager().createAndLoadGroup(name).get();
		} catch (Exception ignored) {
			return null;
		}
	}
	
	@Nullable
	public static Group getOrCreateGroup(String name) {
		var group = getGroupManager().getGroup(name);
		
		return group != null ? group : createGroup(name);
	}
	
	@Nullable
	public static User getUser(PlayerEntity player) {
		return getUserManager().getUser(player.getUuid());
	}
	
	@Nullable
	public static User getUser(UUID uuid) {
		return getUserManager().getUser(uuid);
	}
	
	public static void saveUser(PlayerEntity player) {
		var user = getUser(player);
		
		if (user != null) {
			getUserManager().saveUser(user);
		}
	}
	
	public static void saveUser(UUID uuid) {
		var user = getUser(uuid);
		
		if (user != null) {
			getUserManager().saveUser(user);
		}
	}
	
	public static void saveGroup(Group group) {
		getGroupManager().saveGroup(group);
	}
	
	public static void addNode(PlayerEntity player, Node node) {
		addNode(player.getUuid(), node);
	}
	
	public static void addNode(UUID uuid, Node node) {
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
	
	public static void removeNode(UUID uuid, Node node) {
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
