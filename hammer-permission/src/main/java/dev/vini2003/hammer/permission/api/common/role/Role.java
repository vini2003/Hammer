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

package dev.vini2003.hammer.permission.api.common.role;

import dev.vini2003.hammer.core.api.common.cache.Cached;
import dev.vini2003.hammer.permission.api.common.event.RoleEvents;
import dev.vini2003.hammer.permission.api.common.util.PermUtil;
import dev.vini2003.hammer.permission.registry.common.HPNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.node.types.PrefixNode;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class Role {
	private String name;
	private String prefix = null;
	
	private int prefixWeight = -1;
	private int prefixColor = -1;
	
	private boolean init = false;
	
	private final Cached<Group> group = new Cached<>(() -> {
		if (!init) {
			init();
		}
		
		return PermUtil.getOrCreateGroup(name);
	});
	
	private final Cached<InheritanceNode> inheritanceNode = new Cached<>(() -> {
		if (!init) {
			init();
		}
		
		return InheritanceNode.builder(group.get()).build();
	});
	
	private final Cached<PermissionNode> permissionNode = new Cached<>(() -> {
		if (!init) {
			init();
		}
		
		return PermissionNode.builder("hammer." + name).value(true).build();
	});
	
	private final Cached<PrefixNode> prefixNode = new Cached<>(() -> {
		if (!init) {
			init();
		}
		
		return PrefixNode.builder(prefix, prefixWeight).build();
	});
	
	private final Collection<UUID> holders = new ArrayList<>();
	
	public Role(String name) {
		this.name = name;
	}
	
	public Role(String name, String prefix, int prefixWeight, int prefixColor) {
		this.name = name;
		this.prefix = prefix;
		
		this.prefixWeight = prefixWeight;
		this.prefixColor = prefixColor;
	}
	
	public void init() {
		init = true;
		
		var data = group.get().data();
		
		if (prefixNode.get() != null) {
			data.add(prefixNode.get());
		}
		
		data.add(permissionNode.get());
		data.add(inheritanceNode.get());
		
		PermUtil.saveGroup(group.get());
	}
	
	public boolean isIn(PlayerEntity player) {
		if (player.world.isClient) {
			return holders.contains(player.getUuid());
		} else {
			return PermUtil.hasPermission(player, inheritanceNode.get().getKey());
		}
	}
	
	public void addToClient(UUID uuid) {
		holders.add(uuid);
	}
	
	public void addTo(PlayerEntity player) {
		if (!player.world.isClient) {
			PermUtil.addNode(player.getUuid(), inheritanceNode.get());
			
			var buf = PacketByteBufs.create();
			buf.writeString(name);
			buf.writeUuid(player.getUuid());
			
			for (var otherPlayer : player.getServer().getPlayerManager().getPlayerList()) {
				ServerPlayNetworking.send(otherPlayer, HPNetworking.ADD_ROLE, PacketByteBufs.duplicate(buf));
			}
			
			holders.add(player.getUuid());
		} else {
			addToClient(player.getUuid());
		}
		
		RoleEvents.ADD.invoker().onAdd(player, this);
	}
	
	public void removeFromClient(UUID uuid) {
		holders.remove(uuid);
	}
	
	public void removeFrom(PlayerEntity player) {
		if (!player.world.isClient) {
			PermUtil.removeNode(player.getUuid(), inheritanceNode.get());
			
			var buf = PacketByteBufs.create();
			buf.writeString(name);
			buf.writeUuid(player.getUuid());
			
			for (var otherPlayer : player.getServer().getPlayerManager().getPlayerList()) {
				ServerPlayNetworking.send(otherPlayer, HPNetworking.REMOVE_ROLE, PacketByteBufs.duplicate(buf));
			}
			
			holders.remove(player.getUuid());
		} else {
			removeFromClient(player.getUuid());
		}
		
		RoleEvents.REMOVE.invoker().onRemove(player, this);
	}
	
	public void clearOnClient() {
		holders.clear();
	}
	
	public String getName() {
		return name;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public int getPrefixWeight() {
		return prefixWeight;
	}
	
	public int getPrefixColor() {
		return prefixColor;
	}
	
	public Cached<Group> getGroup() {
		return group;
	}
	
	public Cached<InheritanceNode> getInheritanceNode() {
		return inheritanceNode;
	}
	
	public Cached<PermissionNode> getPermissionNode() {
		return permissionNode;
	}
	
	public Cached<PrefixNode> getPrefixNode() {
		return prefixNode;
	}
	
	public Collection<UUID> getHolders() {
		return holders;
	}
}
