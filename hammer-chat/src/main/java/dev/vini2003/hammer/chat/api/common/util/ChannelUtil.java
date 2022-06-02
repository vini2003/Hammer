package dev.vini2003.hammer.chat.api.common.util;

import dev.vini2003.hammer.chat.api.common.channel.Channel;
import dev.vini2003.hammer.chat.impl.common.accessor.PlayerEntityAccessor;
import net.minecraft.entity.player.PlayerEntity;

public class ChannelUtil {
	public static Channel getSelected(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$getSelectedChannel();
	}
	
	public static void setSelected(PlayerEntity player, Channel channel) {
		((PlayerEntityAccessor) player).hammer$setSelectedChannel(channel);
	}
}
