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

import dev.vini2003.hammer.chat.api.common.util.ChannelUtil;
import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;

public class HCEvents {
	public static void init() {
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			ChatUtil.setShowChat(newPlayer, ChatUtil.shouldShowChat(oldPlayer));
			ChatUtil.setShowGlobalChat(newPlayer, ChatUtil.shouldShowGlobalChat(oldPlayer));
			ChatUtil.setShowCommandFeedback(newPlayer, ChatUtil.shouldShowCommandFeedback(oldPlayer));
			ChatUtil.setShowWarnings(newPlayer, ChatUtil.shouldShowWarnings(oldPlayer));
			
			ChatUtil.setShowDirectMessages(newPlayer, ChatUtil.shouldShowDirectMessages(oldPlayer));
			
			ChatUtil.setFastChatFade(newPlayer, ChatUtil.hasFastChatFade(oldPlayer));
			
			ChatUtil.setMuted(newPlayer, ChatUtil.isMuted(oldPlayer));
			
			ChannelUtil.setSelected(newPlayer, ChannelUtil.getSelected(oldPlayer));
		});
		
		ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
			var receiverPlayer = sender; // Fuck you for this, Yarn.
			
			var server = receiverPlayer.getServer();
			if (server == null) return false;
			
			var playerManager = server.getPlayerManager();
			if (playerManager == null) return false;
			
			var senderPlayer = playerManager.getPlayer(message.getSender());
			if (senderPlayer == null) return false;
			
			var receiverChannel = ChannelUtil.getSelected(receiverPlayer);
			var senderChannel = ChannelUtil.getSelected(senderPlayer);
			
			if (receiverChannel != senderChannel) {
				return false;
			}
			
			return true;
		});
	}
}
