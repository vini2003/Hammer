package dev.vini2003.hammer.chat.api.common.manager;

import dev.vini2003.hammer.chat.api.common.channel.Channel;

import java.util.*;

public class ChannelManager {
	private static final List<Channel> CHANNELS = new ArrayList<>();
	private static final Map<String, Channel> CHANNELS_BY_NAME = new HashMap<>();
	
	public static void add(Channel channel) {
		CHANNELS.add(channel);
		CHANNELS_BY_NAME.put(channel.getName(), channel);
	}
	
	public static void remove(Channel channel) {
		CHANNELS.remove(channel);
		CHANNELS_BY_NAME.remove(channel);
	}
	
	public static Channel getChannelByName(String name) {
		return CHANNELS_BY_NAME.get(name);
	}
	
	public static Collection<Channel> channels() {
		return CHANNELS;
	}
}
