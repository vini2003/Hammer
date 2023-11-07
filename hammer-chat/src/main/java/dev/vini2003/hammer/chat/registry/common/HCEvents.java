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

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;

public class HCEvents {
	public static void init() {
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			newPlayer.hammer$setShowChat(oldPlayer.hammer$shouldShowChat());
			newPlayer.hammer$setShowGlobalChat(oldPlayer.hammer$shouldShowGlobalChat());
			newPlayer.hammer$setShowCommandFeedback(oldPlayer.hammer$shouldShowCommandFeedback());
			newPlayer.hammer$setShowWarnings(oldPlayer.hammer$shouldShowWarnings());
			newPlayer.hammer$setShowDirectMessages(oldPlayer.hammer$shouldShowDirectMessages());
			newPlayer.hammer$setFastChatFade(oldPlayer.hammer$hasFastChatFade());
			newPlayer.hammer$setMuted(oldPlayer.hammer$isMuted());
			
			newPlayer.hammer$setSelectedChannel(oldPlayer.hammer$getSelectedChannel());
		});
		
		ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
			var receiverPlayer = sender; // Fuck you for this, Yarn.
			
			var server = receiverPlayer.getServer();
			if (server == null) return false;
			
			var playerManager = server.getPlayerManager();
			if (playerManager == null) return false;
			
			var senderPlayer = playerManager.getPlayer(message.getSender());
			if (senderPlayer == null) return false;
			
			var receiverChannel = receiverPlayer.hammer$getSelectedChannel();
			var senderChannel = senderPlayer.hammer$getSelectedChannel();
			
			if (receiverChannel != senderChannel) {
				return false;
			}
			
			return true;
		});
	}
}
