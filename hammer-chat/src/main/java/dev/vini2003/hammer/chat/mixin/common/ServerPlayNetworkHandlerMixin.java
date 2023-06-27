package dev.vini2003.hammer.chat.mixin.common;

import dev.vini2003.hammer.chat.HC;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.MessageAcknowledgmentC2SPacket;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
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
	
	private ServerPlayNetworkHandler hammer$self() {
		return (ServerPlayNetworkHandler) (Object) this;
	}
	
	@Inject(method = "sendPacket(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
	private void onSend(Packet<?> packet, CallbackInfo ci) {
		if (HC.CONFIG.disableChatSigning) {
			// TODO: Check if this is equivalent to the 1.19.2 code.
			// if (packet instanceof MessageHeaderS2CPacket) {
			// 	ci.cancel();
			// } else
			if (packet instanceof ChatMessageS2CPacket chat) {
				if (HC.CONFIG.disableChatPrefix) {
					packet = new GameMessageS2CPacket(Text.of(chat.body().content()), false);
				} else {
					packet = new GameMessageS2CPacket(chat.serializedParameters().toParameters(player.getWorld().getRegistryManager()).get().applyChatDecoration(Text.of(chat.body().content())), false);
				}
				
				ci.cancel();
				
				this.sendPacket(packet);
			}
		}
	}
	
	@Inject(method = "sendPacket(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at = @At("HEAD"), cancellable = true)
	private void onSend(Packet<?> packet, @Nullable PacketCallbacks packetSendListener, CallbackInfo info) {
		if (HC.CONFIG.disableChatSigning) {
			// if (packet instanceof MessageHeaderS2CPacket) {
			// 	ci.cancel();
			// } else
			if (packet instanceof ChatMessageS2CPacket chat && packetSendListener != null) {
				info.cancel();
				hammer$self().sendPacket(chat);
			}
		}
	}
}
