package dev.vini2003.hammer.chat.mixin.common;

import dev.vini2003.hammer.chat.HC;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.MessageHeaderS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
	@Shadow
	public ServerPlayerEntity player;
	
	@Shadow
	public abstract void sendPacket(Packet<?> packet);
	
	@Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
	private void onSend(Packet<?> packet, CallbackInfo info) {
		if (HC.CONFIG.disableChatSigning) {
			if (packet instanceof MessageHeaderS2CPacket) {
				info.cancel();
			} else if (packet instanceof ChatMessageS2CPacket chat) {
				packet = new GameMessageS2CPacket(chat.message().getContent(), false);
				
				info.cancel();
				
				this.sendPacket(packet);
			}
		}
	}
	
	@Inject(method = "sendPacket(Lnet/minecraft/network/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at = @At("HEAD"), cancellable = true)
	private void onSend(Packet<?> packet, @Nullable PacketCallbacks packetSendListener, CallbackInfo info) {
		if (HC.CONFIG.disableChatSigning) {
			if (packet instanceof MessageHeaderS2CPacket) {
				info.cancel();
			} else if (packet instanceof ChatMessageS2CPacket chat && packetSendListener != null) {
				info.cancel();
				((ServerPlayNetworkHandler) (Object) this).sendPacket(chat);
			}
		}
	}
}
