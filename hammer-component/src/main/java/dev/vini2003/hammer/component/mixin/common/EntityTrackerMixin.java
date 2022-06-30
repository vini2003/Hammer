package dev.vini2003.hammer.component.mixin.common;

import dev.vini2003.hammer.component.impl.common.component.holder.ComponentHolder;
import dev.vini2003.hammer.component.registry.common.HCNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.server.network.EntityTrackerEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mixin(EntityTrackerEntry.class)
public abstract class EntityTrackerMixin {
	@Shadow
	@Final
	private Entity entity;
	
	@Shadow
	protected abstract void sendSyncPacket(Packet<?> packet);
	
	@Inject(at = @At("HEAD"), method = "sendPackets")
	private void hammer$sendPackets(Consumer<Packet<?>> sender, CallbackInfo ci) {
		var packet = hammer$createSyncPacket();
		
		if (packet != null) {
			sender.accept(packet);
		}
	}
	
	@Inject(method = "syncEntityData", at = @At("HEAD"))
	public void sync(CallbackInfo ci) {
		var packet = this.hammer$createSyncPacket();
		
		if (packet != null) {
			sendSyncPacket(packet);
		}
	}
	
	@Nullable
	private Packet<?> hammer$createSyncPacket() {
		if (entity instanceof ComponentHolder holder) {
			var containerNbt = new NbtCompound();
			holder.getComponentContainer().writeToNbt(containerNbt);
			
			var buf = PacketByteBufs.create();
			buf.writeInt(ComponentHolder.ENTITY);
			buf.writeNbt(containerNbt);
			buf.writeInt(entity.getId());
			
			return ServerPlayNetworking.createS2CPacket(HCNetworking.SYNC_COMPONENT_CONTAINER, buf);
		}
		
		return null;
	}
}
