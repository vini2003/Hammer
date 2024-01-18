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

package dev.vini2003.hammer.core.mixin.common;

import dev.vini2003.hammer.core.HC;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
	@Shadow
	public ServerPlayerEntity player;
	
	@Shadow
	protected abstract boolean isHost();
	
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;isHost()Z"), method = "onPlayerMove")
	private boolean hammer$onPlayerMove$isHost(ServerPlayNetworkHandler instance) {
		if (HC.CONFIG.disableMovementSpeedWarning) {
			return true;
		}
		
		return isHost();
	}
	
	@Inject(at = @At("HEAD"), method = "onPlayerMove", cancellable = true)
	private void hammer$onPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci) {
		if (player.hammer$isFrozen()) {
			ci.cancel();
		}
	}
	
	@Inject(at = @At("HEAD"), method = "onPlayerInteractItem", cancellable = true)
	private void hammer$onPlayerInteractItem(PlayerInteractItemC2SPacket packet, CallbackInfo ci) {
		var hand = packet.getHand();
		
		if (!player.hammer$hasLeftArm() && hand == Hand.OFF_HAND) {
			ci.cancel();
		}
		
		if (!player.hammer$hasRightArm() && hand == Hand.MAIN_HAND) {
			ci.cancel();
		}
		
		if (!player.hammer$allowInteraction()) {
			ci.cancel();
		}
	}
	
	@Inject(at = @At("HEAD"), method = "onPlayerInteractBlock", cancellable = true)
	private void hammer$onPlayerInteractBlock(PlayerInteractBlockC2SPacket packet, CallbackInfo ci) {
		var hand = packet.getHand();
		
		if (!player.hammer$hasLeftArm() && hand == Hand.OFF_HAND) {
			ci.cancel();
		}
		
		if (!player.hammer$hasRightArm() && hand == Hand.MAIN_HAND) {
			ci.cancel();
		}
		
		if (!player.hammer$allowInteraction()) {
			ci.cancel();
		}
	}
	
	@Inject(at = @At("HEAD"), method = "onPlayerInteractEntity", cancellable = true)
	private void hammer$onPlayerInteractEntity(PlayerInteractEntityC2SPacket packet, CallbackInfo ci) {
		if (!player.hammer$hasLeftArm() && !player.hammer$hasRightArm()) {
			ci.cancel();
		}
		
		if (!player.hammer$allowInteraction()) {
			ci.cancel();
		}
	}
}
