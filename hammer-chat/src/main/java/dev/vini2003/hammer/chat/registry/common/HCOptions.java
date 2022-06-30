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

package dev.vini2003.hammer.chat.registry.common;

import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.Option;

public class HCOptions {
	public static final Option SHOW_CHAT = CyclingOption.create("options.show_chat", gameOptions -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return false;
		}
		
		return ChatUtil.shouldShowChat(client.player);
	}, ((gameOptions, option, showChat) -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return;
		}
		
		ChatUtil.setShowChat(client.player, showChat);
	}));
	
	public static final Option SHOW_COMMAND_FEEDBACK = CyclingOption.create("options.show_command_feedback", gameOptions -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return false;
		}
		
		return ChatUtil.shouldShowCommandFeedback(client.player);
	}, ((gameOptions, option, showChat) -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return;
		}
		
		ChatUtil.setShowCommandFeedback(client.player, showChat);
	}));
	
	public static final Option SHOW_WARNINGS = CyclingOption.create("options.show_warnings", gameOptions -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return false;
		}
		
		return ChatUtil.shouldShowWarnings(client.player);
	}, ((gameOptions, option, showChat) -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return;
		}
		
		ChatUtil.setShowWarnings(client.player, showChat);
	}));
	
	public static final Option SHOW_DIRECT_MESSAGES = CyclingOption.create("options.show_direct_messages", gameOptions -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return false;
		}
		
		return ChatUtil.shouldShowDirectMessages(client.player);
	}, ((gameOptions, option, showChat) -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return;
		}
		
		ChatUtil.setShowDirectMessages(client.player, showChat);
	}));
	
	public static final Option FAST_CHAT_FADE = CyclingOption.create("options.fast_chat_fade", gameOptions -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return false;
		}
		
		return ChatUtil.hasFastChatFade(client.player);
	}, ((gameOptions, option, fastChatFade) -> {
		var client = InstanceUtil.getClient();
		
		if (client == null || client.player == null) {
			return;
		}
		
		ChatUtil.setFastChatFade(client.player, fastChatFade);
	}));
}
