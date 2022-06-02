package dev.vini2003.hammer.chat.api.common.channel;

import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class Channel {
	private String name;
	
	private Collection<UUID> holders = new ArrayList<>();
	
	public Channel(String name) {
		this.name = name;
	}
	
	public boolean isIn(PlayerEntity player) {
		return holders.contains(player.getUuid());
	}
	
	public void addTo(PlayerEntity player) {
		holders.add(player.getUuid());
	}
	
	public void removeFrom(PlayerEntity player) {
		holders.remove(player.getUuid());
	}
	
	public String getName() {
		return name;
	}
	
	public Collection<UUID> getHolders() {
		return holders;
	}
}
