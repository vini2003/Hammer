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

import dev.vini2003.hammer.chat.api.common.channel.Channel;
import dev.vini2003.hammer.chat.api.common.manager.ChannelManager;
import dev.vini2003.hammer.chat.impl.common.accessor.PlayerEntityAccessor;
import dev.vini2003.hammer.core.HC;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class HCNetworking {
	public static final Identifier SWITCH_SELECTED_CHANNEL = HC.id("switch_selected_channel");
	
	public static void init() {
		ServerPlayNetworking.registerGlobalReceiver(SWITCH_SELECTED_CHANNEL, (server, player, handler, buf, responseSender) -> {
			var up = buf.readBoolean();
			
			server.execute(() -> {
				var channels = new ArrayList<Channel>();
				
				for (var channel : ChannelManager.channels()) {
					if (channel.isIn(player)) {
						channels.add(channel);
					}
				}
				
				if (channels.size() == 0) {
					player.sendMessage(new TranslatableText("text.hammer.channel.none"), false);
					
					return;
				} else {
					var selectedChannel = ((PlayerEntityAccessor) player).hammer$getSelectedChannel();
					var selectedChannelIndex = channels.indexOf(selectedChannel);
					
					if (up) {
						if (selectedChannelIndex == channels.size() - 1) {
							selectedChannelIndex = 0;
						} else {
							selectedChannelIndex += 1;
						}
					} else {
						if (selectedChannelIndex == 0) {
							selectedChannelIndex = channels.size() - 1;
						} else {
							selectedChannelIndex -= 1;
						}
					}
					
					selectedChannel = channels.get(selectedChannelIndex);
					
					((PlayerEntityAccessor) player).hammer$setSelectedChannel(selectedChannel);
					
					player.sendMessage(new TranslatableText("text.hammer.channel.select.self", new LiteralText("#" + selectedChannel.getName()).formatted(Formatting.DARK_GRAY)), false);
				}
			});
		});
	}
}
