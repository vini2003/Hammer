 package dev.vini2003.hammer.chat.api.common.event;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.vini2003.hammer.chat.api.common.channel.Channel;
import net.minecraft.entity.player.PlayerEntity;

public interface ChannelEvents {
	Event<Add> ADD = EventFactory.createLoop(Add.class);
	
	Event<Remove> REMOVE = EventFactory.createLoop(Remove.class);
	
	@FunctionalInterface
	interface Add {
		void onAdd(PlayerEntity player, Channel channel);
	}
	
	@FunctionalInterface
	interface Remove {
		void onRemove(PlayerEntity player, Channel channel);
	}
}
