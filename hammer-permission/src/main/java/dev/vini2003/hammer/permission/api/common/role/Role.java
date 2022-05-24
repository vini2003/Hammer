package dev.vini2003.hammer.permission.api.common.role;

import dev.vini2003.hammer.core.api.common.cache.Cached;
import dev.vini2003.hammer.permission.api.common.util.PermissionUtils;
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
	
	private Cached<Group> group = new Cached<>(() -> PermissionUtils.getOrCreateGroup(name));
	
	private Cached<InheritanceNode> inheritanceNode = new Cached<>(() -> InheritanceNode.builder(group.get()).build());
	private Cached<PermissionNode> permissionNode = new Cached<>(() -> PermissionNode.builder("hammer." + name).value(true).build());
	private Cached<PrefixNode> prefixNode = new Cached<>(() -> PrefixNode.builder(prefix, prefixWeight).build());
	
	private Collection<UUID> holders = new ArrayList<>();
	
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
		var data = group.get().data();
		
		if (prefixNode.get() != null) {
			data.add(prefixNode.get());
		}
		
		data.add(permissionNode.get());
		data.add(inheritanceNode.get());
		
		PermissionUtils.saveGroup(group.get());
	}
	
	public boolean isIn(PlayerEntity player) {
		return PermissionUtils.hasPermission(player, inheritanceNode.get().getKey());
	}
	
	public void addToClient(UUID uuid) {
		holders.add(uuid);
	}
	
	public void addTo(PlayerEntity player) {
		if (!player.world.isClient) {
			PermissionUtils.addNode(player.getUuid(), inheritanceNode.get());
			
			var buf = PacketByteBufs.create();
			buf.writeString(name);
			buf.writeUuid(player.getUuid());
			
			for (var otherPlayer : player.getServer().getPlayerManager().getPlayerList()) {
				ServerPlayNetworking.send(otherPlayer, HPNetworking.ADD_ROLE, PacketByteBufs.duplicate(buf));
			}
		} else {
			addToClient(player.getUuid());
		}
	}
	
	public void removeFromClient(UUID uuid) {
		holders.remove(uuid);
	}
	
	public void removeFrom(PlayerEntity player) {
		if (!player.world.isClient) {
			PermissionUtils.removeNode(player.getUuid(), inheritanceNode.get());
			
			var buf = PacketByteBufs.create();
			buf.writeString(name);
			buf.writeUuid(player.getUuid());
			
			for (var otherPlayer : player.getServer().getPlayerManager().getPlayerList()) {
				ServerPlayNetworking.send(otherPlayer, HPNetworking.REMOVE_ROLE, PacketByteBufs.duplicate(buf));
			}
		} else {
			removeFromClient(player.getUuid());
		}
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
