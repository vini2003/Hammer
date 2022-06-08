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
import dev.vini2003.hammer.chat.impl.common.accessor.PlayerEntityAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccessor {
	private static final TrackedData<Boolean> HAMMER$SHOW_CHAT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> HAMMER$SHOW_GLOBAL_CHAT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> HAMMER$SHOW_COMMAND_FEEDBACK = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> HAMMER$SHOW_WARNINGS = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> HAMMER$MUTED = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	private Channel hammer$selectedChannel = null;
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(at = @At("HEAD"), method = "initDataTracker")
	private void hammer$initDataTracker(CallbackInfo ci) {
		dataTracker.startTracking(HAMMER$SHOW_CHAT, true);
		dataTracker.startTracking(HAMMER$SHOW_GLOBAL_CHAT, true);
		dataTracker.startTracking(HAMMER$SHOW_COMMAND_FEEDBACK, true);
		dataTracker.startTracking(HAMMER$SHOW_WARNINGS, true);
		
		dataTracker.startTracking(HAMMER$MUTED, false);
	}
	
	@Inject(at = @At("HEAD"), method = "writeCustomDataToNbt")
	private void hammer$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putBoolean("Hammer$ShowChat", dataTracker.get(HAMMER$SHOW_CHAT));
		nbt.putBoolean("Hammer$ShowGlobalChat", dataTracker.get(HAMMER$SHOW_GLOBAL_CHAT));
		nbt.putBoolean("Hammer$ShowCommandFeedback", dataTracker.get(HAMMER$SHOW_COMMAND_FEEDBACK));
		nbt.putBoolean("Hammer$ShowWarnings", dataTracker.get(HAMMER$SHOW_WARNINGS));
		
		nbt.putBoolean("Hammer$Muted", dataTracker.get(HAMMER$MUTED));
	}
	
	@Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
	private void hammer$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		if (nbt.contains("Hammer$ShowChat")) {
			dataTracker.set(HAMMER$SHOW_CHAT, nbt.getBoolean("Hammer$ShowChat"));
		}
		
		if (nbt.contains("Hammer$ShowGlobalChat")) {
			dataTracker.set(HAMMER$SHOW_GLOBAL_CHAT, nbt.getBoolean("Hammer$ShowGlobalChat"));
		}
		
		if (nbt.contains("Hammer$ShowCommandFeedback")) {
			dataTracker.set(HAMMER$SHOW_COMMAND_FEEDBACK, nbt.getBoolean("Hammer$ShowCommandFeedback"));
		}
		
		if (nbt.contains("Hammer$ShowWarnings")) {
			dataTracker.set(HAMMER$SHOW_WARNINGS, nbt.getBoolean("Hammer$ShowWarnings"));
		}
		
		if (nbt.contains("Hammer$Muted")) {
			dataTracker.set(HAMMER$MUTED, nbt.getBoolean("Hammer$Muted"));
		}
	}
	
	@Override
	public void hammer$setShowChat(boolean showChat) {
		dataTracker.set(HAMMER$SHOW_CHAT, showChat);
	}
	
	@Override
	public boolean hammer$shouldShowChat() {
		return dataTracker.get(HAMMER$SHOW_CHAT);
	}
	
	@Override
	public void hammer$setShowGlobalChat(boolean showGlobalChat) {
		dataTracker.set(HAMMER$SHOW_GLOBAL_CHAT, showGlobalChat);
	}
	
	@Override
	public boolean hammer$shouldShowGlobalChat() {
		return dataTracker.get(HAMMER$SHOW_GLOBAL_CHAT);
	}
	
	@Override
	public void hammer$setShowCommandFeedback(boolean showFeedback) {
		dataTracker.set(HAMMER$SHOW_COMMAND_FEEDBACK, showFeedback);
	}
	
	@Override
	public boolean hammer$shouldShowCommandFeedback() {
		return dataTracker.get(HAMMER$SHOW_COMMAND_FEEDBACK);
	}
	
	@Override
	public void hammer$setShowWarnings(boolean showWarnings) {
		dataTracker.set(HAMMER$SHOW_WARNINGS, showWarnings);
	}
	
	@Override
	public boolean hammer$shouldShowWarnings() {
		return dataTracker.get(HAMMER$SHOW_WARNINGS);
	}
	
	@Override
	public void hammer$setMuted(boolean muted) {
		dataTracker.set(HAMMER$MUTED, muted);
	}
	
	@Override
	public boolean hammer$isMuted() {
		return dataTracker.get(HAMMER$MUTED);
	}
	
	@Override
	public void hammer$setSelectedChannel(Channel hammer$selectedChannel) {
		this.hammer$selectedChannel = hammer$selectedChannel;
	}
	
	@Override
	public Channel hammer$getSelectedChannel() {
		return hammer$selectedChannel;
	}
}
