 package dev.vini2003.hammer.chat.api.common.event;

import dev.vini2003.hammer.chat.api.common.channel.Channel;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public interface ChannelEvents {
	Event<Add> ADD = EventFactory.createArrayBacked(Add.class, (events) -> (player, channel) -> {
		for (var event : events) {
			event.onAdd(player, channel);
		}
	});
	
	Event<Remove> REMOVE = EventFactory.createArrayBacked(Remove.class, (events) -> (player, channel) -> {
		for (var event : events) {
			event.onRemove(player, channel);
		}
	});
	
	@FunctionalInterface
	interface Add {
		void onAdd(PlayerEntity player, Channel channel);
	}
	
	@FunctionalInterface
	interface Remove {
		void onRemove(PlayerEntity player, Channel channel);
	}
}
