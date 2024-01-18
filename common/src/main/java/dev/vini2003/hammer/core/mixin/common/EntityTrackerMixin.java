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

import dev.architectury.networking.NetworkManager;
import dev.vini2003.hammer.core.impl.common.component.holder.ComponentHolder;
import dev.vini2003.hammer.core.registry.common.HCNetworking;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(EntityTrackerEntry.class)
public abstract class EntityTrackerMixin {
	@Shadow
	@Final
	private Entity entity;
	
	@Shadow
	protected abstract void sendSyncPacket(Packet<?> packet);
	
	@Inject(at = @At("HEAD"), method = "sendPackets")
	private void hammer$sendPackets(ServerPlayerEntity player, Consumer<Packet<ClientPlayPacketListener>> sender, CallbackInfo ci) {
		var packet = hammer$createSyncPacket();
		
		if (packet != null) {
			sender.accept(packet);
		}
	}
	
	@Inject(method = "syncEntityData", at = @At("HEAD"))
	public void hammer$syncEntityData(CallbackInfo ci) {
		var packet = this.hammer$createSyncPacket();
		
		if (packet != null) {
			sendSyncPacket(packet);
		}
	}
	
	@Nullable
	private Packet<ClientPlayPacketListener> hammer$createSyncPacket() {
		if (entity instanceof ComponentHolder holder) {
			var containerNbt = new NbtCompound();
			holder.getComponentContainer().writeToNbt(containerNbt);
			
			var buf = new PacketByteBuf(Unpooled.buffer());
			buf.writeInt(ComponentHolder.ENTITY);
			buf.writeNbt(containerNbt);
			buf.writeInt(entity.getId());
			
			return (Packet<ClientPlayPacketListener>) NetworkManager.toPacket(NetworkManager.Side.S2C, HCNetworking.SYNC_COMPONENT_CONTAINER, buf);
		}
		
		return null;
	}
}
