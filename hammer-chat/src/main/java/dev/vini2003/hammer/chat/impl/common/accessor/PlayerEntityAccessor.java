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

package dev.vini2003.hammer.chat.impl.common.accessor;

import dev.vini2003.hammer.chat.api.common.channel.Channel;
import dev.vini2003.hammer.core.api.common.exception.NoMixinException;

import java.util.List;

public interface PlayerEntityAccessor {
	default void hammer$setShowChat(boolean showChat) {
		throw new NoMixinException();
	}
	
	default boolean hammer$shouldShowChat() {
		throw new NoMixinException();
	}
	
	default void hammer$setShowGlobalChat(boolean showGlobalChat) {
		throw new NoMixinException();
	}
	
	default boolean hammer$shouldShowGlobalChat() {
		throw new NoMixinException();
	}
	
	default void hammer$setShowCommandFeedback(boolean showFeedback) {
		throw new NoMixinException();
	}
	
	default boolean hammer$shouldShowCommandFeedback() {
		throw new NoMixinException();
	}
	
	default void hammer$setShowWarnings(boolean showWarnings) {
		throw new NoMixinException();
	}
	
	default boolean hammer$shouldShowWarnings() {
		throw new NoMixinException();
	}
	
	default void hammer$setMuted(boolean muted) {
		throw new NoMixinException();
	}
	
	default boolean hammer$shouldShowDirectMessages() {
		throw new NoMixinException();
	}
	
	default void hammer$setShowDirectMessages(boolean showDirectMessages) {
		throw new NoMixinException();
	}
	
	default boolean hammer$isMuted() {
		throw new NoMixinException();
	}
	
	default void hammer$setSelectedChannel(Channel selectedChannel) {
		throw new NoMixinException();
	}
	
	default boolean hammer$hasFastChatFade() {
		throw new NoMixinException();
	}
	
	default void hammer$setFastChatFade(boolean fastChatFade) {
		throw new NoMixinException();
	}
	
	default boolean hammer$isInChannel(Channel channel) {
		throw new NoMixinException();
	}
	
	default void hammer$joinChannel(Channel channel) {
		throw new NoMixinException();
	}
	
	default void hammer$leaveChannel(Channel channel) {
		throw new NoMixinException();
	}
	
	default Channel hammer$getSelectedChannel() {
		throw new NoMixinException();
	}
	
	default Channel hammer$getPreviousSelectedChannel() {
		throw new NoMixinException();
	}
	
	default List<Channel> hammer$getPreviousSelectedChannels() {
		throw new NoMixinException();
	}
}