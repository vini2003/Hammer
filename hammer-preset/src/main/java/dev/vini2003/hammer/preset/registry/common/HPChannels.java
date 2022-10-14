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

package dev.vini2003.hammer.preset.registry.common;

import dev.vini2003.hammer.chat.api.common.channel.Channel;
import dev.vini2003.hammer.chat.api.common.manager.ChannelManager;
import dev.vini2003.hammer.preset.HP;

public class HPChannels {
	public static final Channel SPECTATOR = new Channel("spectator");
	public static final Channel STAFF = new Channel("staff");
	
	public static final Channel GENERAL = new Channel("general");
	
	public static void init() {
		if (HP.CONFIG.enableChannels) {
			ChannelManager.add(SPECTATOR);
			ChannelManager.add(STAFF);
			ChannelManager.add(GENERAL);
		}
	}
}
