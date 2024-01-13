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

package dev.vini2003.hammer.border.mixin.client;

import dev.vini2003.hammer.border.impl.common.border.CubicWorldBorder;
import dev.vini2003.hammer.border.impl.common.packet.border.CubicWorldBorderCenterChangedS2CPacket;
import dev.vini2003.hammer.border.impl.common.packet.border.CubicWorldBorderInitializeS2CPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.WorldBorderCenterChangedS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderInitializeS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	@Shadow
	private ClientWorld world;
	@Shadow
	@Final
	private MinecraftClient client;
	
	private ClientPlayNetworkHandler hammer$self() {
		return (ClientPlayNetworkHandler) (Object) this;
	}
	
	@Inject(at = @At("HEAD"), method = "onWorldBorderCenterChanged", cancellable = true)
	private void hammer$onWorldBorderCenterChanged(WorldBorderCenterChangedS2CPacket packet, CallbackInfo ci) {
		NetworkThreadUtils.forceMainThread(packet, hammer$self(), client);
		
		var worldBorder = world.getWorldBorder();
		var cubicWorldBorder = (CubicWorldBorder) worldBorder;
		
		var cubicPacket = (CubicWorldBorderCenterChangedS2CPacket) packet;
		
		cubicWorldBorder.setCenter(packet.getCenterX(), cubicPacket.getCenterY(), packet.getCenterZ());
		
		ci.cancel();
	}
	
	@Inject(at = @At("HEAD"), method = "onWorldBorderInitialize")
	private void hammer$onWorldBorderInitialize(WorldBorderInitializeS2CPacket packet, CallbackInfo ci) {
		NetworkThreadUtils.forceMainThread(packet, hammer$self(), this.client);
		
		var worldBorder = this.world.getWorldBorder();
		var cubicWorldBorder = (CubicWorldBorder) worldBorder;
		
		var cubicPacket = (CubicWorldBorderInitializeS2CPacket) packet;
		
		cubicWorldBorder.setCenter(packet.getCenterX(), cubicPacket.getCenterY(), packet.getCenterZ());
		
		var lerpTime = packet.getSizeLerpTime();
		
		if (lerpTime > 0L) {
			worldBorder.interpolateSize(packet.getSize(), packet.getSizeLerpTarget(), lerpTime);
		} else {
			worldBorder.setSize(packet.getSizeLerpTarget());
		}
		
		worldBorder.setMaxRadius(packet.getMaxRadius());
		worldBorder.setWarningBlocks(packet.getWarningBlocks());
		worldBorder.setWarningTime(packet.getWarningTime());
	}
}
