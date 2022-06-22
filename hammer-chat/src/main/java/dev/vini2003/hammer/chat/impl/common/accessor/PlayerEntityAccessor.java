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

import java.util.List;

public interface PlayerEntityAccessor {
	void hammer$setShowChat(boolean showChat);
	
	boolean hammer$shouldShowChat();
	
	void hammer$setShowGlobalChat(boolean showGlobalChat);
	
	boolean hammer$shouldShowGlobalChat();
	
	void hammer$setShowCommandFeedback(boolean showFeedback);
	
	boolean hammer$shouldShowCommandFeedback();
	
	void hammer$setShowWarnings(boolean showWarnings);
	
	boolean hammer$shouldShowWarnings();
	
	void hammer$setMuted(boolean muted);
	
	boolean hammer$isMuted();
	
	void hammer$setSelectedChannel(Channel selectedChannel);
	
	Channel hammer$getSelectedChannel();
	
	List<Channel> hammer$getPreviousSelectedChannels();
}
