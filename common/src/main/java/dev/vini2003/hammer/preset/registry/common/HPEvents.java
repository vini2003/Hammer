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

import dev.architectury.event.events.common.PlayerEvent;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.queue.ServerTaskQueue;
import dev.vini2003.hammer.preset.HP;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;


public class HPEvents {
	private static final List<UUID> FIRST_JOINS = new CopyOnWriteArrayList<>();
	
	public static void init() {
		PlayerEvent.PLAYER_JOIN.register((player) ->  {
			if (InstanceUtil.isDevelopmentEnvironment()) {
				player.getServer().getPlayerManager().addToOperators(player.getGameProfile());
				
				player.changeGameMode(GameMode.CREATIVE);
				player.sendMessage(Text.translatable("text.hammer.information"), false);
			}
		});
		
		PlayerEvent.PLAYER_JOIN.register((player) ->  {
			if (FIRST_JOINS.contains(player.getUuid())) return;
			else FIRST_JOINS.add(player.getUuid());
			
			if (HP.CONFIG.enableChannels) {
				player.hammer$setSelectedChannel(HPChannels.GENERAL);
			}
			
			player.hammer$setShowDirectMessages(HP.CONFIG.defaultShowDirectMessages);
			player.hammer$setShowWarnings(HP.CONFIG.defaultShowWarnings);
			player.hammer$setShowCommandFeedback(HP.CONFIG.defaultShowCommandFeedback);
			player.hammer$setShowChat(HP.CONFIG.defaultShowChat);
			
			player.hammer$setFastChatFade(HP.CONFIG.defaultFastChatFade);
			
			ServerTaskQueue.enqueue(($) -> {
				player.hammer$setFastChatFade(HP.CONFIG.defaultFastChatFade);
			}, 5000L);
			
			if (HP.CONFIG.enableWelcome) {
				for (var i = 0; i < 2; ++i) {
					player.sendMessage(Text.translatable("text.hammer.welcome_" + i), false);
				}
			}
		});
	}
}
