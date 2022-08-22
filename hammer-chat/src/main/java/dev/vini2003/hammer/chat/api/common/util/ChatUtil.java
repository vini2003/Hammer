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

import dev.vini2003.hammer.chat.impl.common.accessor.PlayerEntityAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;


public class ChatUtil {
	public static void setShowChat(PlayerEntity player, boolean showChat) {
		((PlayerEntityAccessor) player).hammer$setShowChat(showChat);
	}
	
	public static boolean shouldShowChat(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$shouldShowChat();
	}
	
	public static void setShowGlobalChat(PlayerEntity player, boolean showGlobalChat) {
		((PlayerEntityAccessor) player).hammer$setShowGlobalChat(showGlobalChat);
	}
	
	public static boolean shouldShowGlobalChat(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$shouldShowGlobalChat();
	}
	
	public static void setShowCommandFeedback(PlayerEntity player, boolean showFeedback) {
		((PlayerEntityAccessor) player).hammer$setShowCommandFeedback(showFeedback);
	}
	
	public static boolean shouldShowCommandFeedback(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$shouldShowCommandFeedback();
	}
	
	public static void setShowWarnings(PlayerEntity player, boolean showWarnings) {
		((PlayerEntityAccessor) player).hammer$setShowWarnings(showWarnings);
	}
	
	public static boolean shouldShowWarnings(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$shouldShowWarnings();
	}
	
	public static void setShowDirectMessages(PlayerEntity player, boolean showDirectMessages) {
		((PlayerEntityAccessor) player).hammer$setShowDirectMessages(showDirectMessages);
	}
	
	public static boolean shouldShowDirectMessages(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$shouldShowDirectMessages();
	}

	public static void setFastChatFade(PlayerEntity player, boolean fastChatFade) {
		((PlayerEntityAccessor) player).hammer$setFastChatFade(fastChatFade);
	}
	
	public static boolean hasFastChatFade(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$hasFastChatFade();
	}
	
	public static void setMuted(PlayerEntity player, boolean muted) {
		((PlayerEntityAccessor) player).hammer$setMuted(muted);
	}
	
	public static boolean isMuted(PlayerEntity player) {
		return ((PlayerEntityAccessor) player).hammer$isMuted();
	}
}
