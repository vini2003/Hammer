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

package dev.vini2003.hammer.chat.api.common.util;

import dev.vini2003.hammer.chat.api.common.channel.Channel;
import dev.vini2003.hammer.chat.impl.common.accessor.PlayerEntityAccessor;
import net.minecraft.entity.player.PlayerEntity;

import org.jetbrains.annotations.Nullable;

public class ChannelUtil {
	@Nullable
	public static Channel getSelected(PlayerEntity player) {
		if (player == null) return null;
		return ((PlayerEntityAccessor) player).hammer$getSelectedChannel();
	}
	
	public static void setSelected(PlayerEntity player, @Nullable Channel channel) {
		if (player == null) return;
		
		var prev = getSelected(player);
		
		if (prev != null) {
			((PlayerEntityAccessor) player).hammer$getPreviousSelectedChannels().add(prev);
		}
		
		((PlayerEntityAccessor) player).hammer$setSelectedChannel(channel);
	}
	
	@Nullable
	public static Channel getPreviousSelected(PlayerEntity player) {
		if (player == null) return null;
		
		var channels = ((PlayerEntityAccessor) player).hammer$getPreviousSelectedChannels();
		
		if (!channels.isEmpty()) {
			var first = channels.get(0);
			channels.remove(0);
			
			return first;
		} else {
			return null;
		}
	}
}
