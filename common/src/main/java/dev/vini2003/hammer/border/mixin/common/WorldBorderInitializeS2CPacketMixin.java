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

package dev.vini2003.hammer.border.mixin.common;

import dev.vini2003.hammer.border.impl.common.border.CubicWorldBorder;
import dev.vini2003.hammer.border.impl.common.packet.border.CubicWorldBorderInitializeS2CPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.WorldBorderInitializeS2CPacket;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldBorderInitializeS2CPacket.class)
public class WorldBorderInitializeS2CPacketMixin implements CubicWorldBorderInitializeS2CPacket {
	private double centerY;
	
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/world/border/WorldBorder;)V")
	private void hammer$init$0(WorldBorder border, CallbackInfo ci) {
		var extendedBorder = (CubicWorldBorder) border;
		
		centerY = extendedBorder.getCenterY();
	}
	
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V")
	private void hammer$init$1(PacketByteBuf buf, CallbackInfo ci) {
		centerY = buf.readDouble();
	}
	
	@Inject(at = @At("RETURN"), method = "write")
	private void hammer$write(PacketByteBuf buf, CallbackInfo ci) {
		buf.writeDouble(centerY);
	}
	
	@Override
	public double getCenterY() {
		return centerY;
	}
}
