package dev.vini2003.hammer.preset.registry.common;

import dev.vini2003.hammer.chat.api.common.channel.Channel;
import dev.vini2003.hammer.chat.api.common.manager.ChannelManager;

public class HPChannels {
	public static final Channel SPECTATOR = new Channel("spectator");
	public static final Channel STAFF = new Channel("staff");
	
	public static final Channel GENERAL = new Channel("general");
	
	public static void init() {
		ChannelManager.add(SPECTATOR);
		ChannelManager.add(STAFF);
		
		ChannelManager.add(GENERAL);
	}
}
