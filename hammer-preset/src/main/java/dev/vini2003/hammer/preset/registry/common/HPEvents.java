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

import dev.vini2003.hammer.chat.api.common.util.ChannelUtil;
import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import dev.vini2003.hammer.core.api.common.queue.ServerTaskQueue;
import dev.vini2003.hammer.preset.HP;
import dev.vini2003.hammer.preset.api.common.util.PlayerListUtil;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class HPEvents {
	private static final List<UUID> FIRST_JOINS = new CopyOnWriteArrayList<>();
	
	public static void init() {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			var player = handler.getPlayer();
			
			if (FIRST_JOINS.contains(player.getUuid())) return;
			else FIRST_JOINS.add(player.getUuid());
			
			if (HP.CONFIG.enableChannels) {
				ChannelUtil.setSelected(player, HPChannels.GENERAL);
			}
			
			ChatUtil.setShowDirectMessages(player, HP.CONFIG.defaultShowDirectMessages);
			ChatUtil.setShowWarnings(player, HP.CONFIG.defaultShowWarnings);
			ChatUtil.setShowCommandFeedback(player, HP.CONFIG.defaultShowCommandFeedback);
			ChatUtil.setShowChat(player, HP.CONFIG.defaultShowChat);
			ChatUtil.setShowGlobalChat(player, HP.CONFIG.defaultShowGlobalChat);
			
			ChatUtil.setFastChatFade(player, HP.CONFIG.defaultFastChatFade);
			
			ServerTaskQueue.enqueue(($) -> {
				ChatUtil.setFastChatFade(handler.player, HP.CONFIG.defaultFastChatFade);
			}, 5000L);
			
			if (HP.CONFIG.enableWelcome) {
				for (var i = 0; i < 2; ++i) {
					player.sendMessage(Text.translatable("text.hammer.welcome_" + i), false);
				}
			}
		});
	}
}
