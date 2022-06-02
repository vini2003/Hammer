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
