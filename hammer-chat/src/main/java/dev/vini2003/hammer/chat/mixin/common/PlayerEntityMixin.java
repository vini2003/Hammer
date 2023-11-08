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

package dev.vini2003.hammer.chat.mixin.common;

import dev.vini2003.hammer.chat.api.common.channel.Channel;
import dev.vini2003.hammer.chat.api.common.event.ChannelEvents;
import dev.vini2003.hammer.chat.impl.common.accessor.PlayerEntityAccessor;
import dev.vini2003.hammer.core.api.common.component.TrackedDataComponent;
import dev.vini2003.hammer.core.api.common.data.TrackedDataHandler;
import dev.vini2003.hammer.core.registry.common.HCComponents;
import net.fabricmc.loader.impl.game.minecraft.launchwrapper.FabricServerTweaker;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccessor {
	@Shadow
	public abstract void remove(RemovalReason reason);
	
	private final TrackedDataHandler<Boolean> hammer$showChat = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, true,"ShowChat");
	private final TrackedDataHandler<Boolean> hammer$showGlobalChat = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, true, "ShowGlobalChat");
	private final TrackedDataHandler<Boolean> hammer$showCommandFeedback = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, false, "ShowCommandFeedback");
	private final TrackedDataHandler<Boolean> hammer$showWarnings = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, false, "ShowWarnings");
	private final TrackedDataHandler<Boolean> hammer$showDirectMessages = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, true, "ShowDirectMessages");
	
	private final TrackedDataHandler<Boolean> hammer$fastChatFade = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, false, "FastChatFade");
	
	private final TrackedDataHandler<Boolean> hammer$muted = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, false, "Muted");
	
	private Channel hammer$selectedChannel = null;
	
	private final List<Channel> hammer$previousSelectedChannels = new ArrayList<>();
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	private PlayerEntity hammer$self() {
		return (PlayerEntity) (Object) this;
	}
	
	@Override
	public void hammer$setShowChat(boolean showChat) {
		hammer$showChat.set(showChat);
	}
	
	@Override
	public boolean hammer$shouldShowChat() {
		return hammer$showChat.get();
	}
	
	@Override
	public void hammer$setShowGlobalChat(boolean showGlobalChat) {
		hammer$showGlobalChat.set(showGlobalChat);
	}
	
	@Override
	public boolean hammer$shouldShowGlobalChat() {
		return hammer$showGlobalChat.get();
	}
	
	@Override
	public void hammer$setShowCommandFeedback(boolean showFeedback) {
		hammer$showCommandFeedback.set(showFeedback);
	}
	
	@Override
	public boolean hammer$shouldShowCommandFeedback() {
		return hammer$showCommandFeedback.get();
	}
	
	@Override
	public void hammer$setShowWarnings(boolean showWarnings) {
		hammer$showWarnings.set(showWarnings);
	}
	
	@Override
	public boolean hammer$shouldShowWarnings() {
		return hammer$showWarnings.get();
	}
	
	@Override
	public void hammer$setShowDirectMessages(boolean showDirectMessages) {
		hammer$showDirectMessages.set(showDirectMessages);
	}
	
	@Override
	public boolean hammer$shouldShowDirectMessages() {
		return hammer$showDirectMessages.get();
	}
	
	@Override
	public void hammer$setMuted(boolean muted) {
		hammer$muted.set(muted);
	}
	
	@Override
	public boolean hammer$isMuted() {
		return hammer$muted.get();
	}
	
	@Override
	public void hammer$setFastChatFade(boolean fastChatFade) {
		hammer$fastChatFade.set(fastChatFade);
	}
	
	@Override
	public boolean hammer$hasFastChatFade() {
		return hammer$fastChatFade.get();
	}
	
	@Override
	public boolean hammer$isInChannel(Channel channel) {
		return channel.getHolders().contains(getUuid());
	}
	
	@Override
	public void hammer$joinChannel(Channel channel) {
		channel.getHolders().add(getUuid());
		
		ChannelEvents.ADD.invoker().onAdd(hammer$self(), channel);
	}
	
	@Override
	public void hammer$leaveChannel(Channel channel) {
		channel.getHolders().remove(getUuid());
		
		ChannelEvents.REMOVE.invoker().onRemove(hammer$self(), channel);
	}
	
	@Override
	public void hammer$setSelectedChannel(Channel hammer$selectedChannel) {
		var prev = this.hammer$selectedChannel;
		
		if (prev != null) {
			hammer$previousSelectedChannels.add(prev);
		}
		
		this.hammer$selectedChannel = hammer$selectedChannel;
	}
	
	@Override
	public Channel hammer$getSelectedChannel() {
		return hammer$selectedChannel;
	}
	
	@Override
	public Channel hammer$getPreviousSelectedChannel() {
		if (!hammer$previousSelectedChannels.isEmpty()) {
			var first = hammer$previousSelectedChannels.get(0);
			hammer$previousSelectedChannels.remove(0);
			
			return first;
		} else {
			return null;
		}
	}
	
	@Override
	public List<Channel> hammer$getPreviousSelectedChannels() {
		return hammer$previousSelectedChannels;
	}
}
