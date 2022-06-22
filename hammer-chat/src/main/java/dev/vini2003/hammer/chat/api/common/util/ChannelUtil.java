package dev.vini2003.hammer.chat.api.common.util;

import dev.vini2003.hammer.chat.api.common.channel.Channel;
import dev.vini2003.hammer.chat.impl.common.accessor.PlayerEntityAccessor;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;

public class ChannelUtil {
	@Nullable
	public static Channel getSelected(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$getSelectedChannel();
	}
	
	public static void setSelected(PlayerEntity player, @Nullable Channel channel) {
		var prev = getSelected(player);
		
		if (prev != null) {
			((PlayerEntityAccessor) player).hammer$getPreviousSelectedChannels().add(prev);
		}
		
		((PlayerEntityAccessor) player).hammer$setSelectedChannel(channel);
	}
	
	@Nullable
	public static Channel getPreviousSelected(PlayerEntity player) {
		var channels = ((PlayerEntityAccessor) player).hammer$getPreviousSelectedChannels();
		
		if (!channels.isEmpty()) {
			var first = channels.get(0);
			channels.remove(0);
			
			return first;
		} else {
			return null;
		}
	}
}
