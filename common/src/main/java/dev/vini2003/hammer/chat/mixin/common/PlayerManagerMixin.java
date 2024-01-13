package dev.vini2003.hammer.chat.mixin.common;

import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	@Inject(method = "broadcast(Lnet/minecraft/network/message/SignedMessage;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/network/message/MessageType$Parameters;)V", at = @At("HEAD"), cancellable = true)
	private void hammer$broadcast(SignedMessage message, ServerPlayerEntity sender, MessageType.Parameters params, CallbackInfo ci) {
		var receiverPlayer = sender; // Fuck you for this, Yarn.
		
		var server = receiverPlayer.getServer();
		if (server == null) {
			ci.cancel();
			return;
		}
		
		var playerManager = server.getPlayerManager();
		if (playerManager == null) {
			ci.cancel();
			return;
		}
		
		var senderPlayer = playerManager.getPlayer(message.getSender());
		if (senderPlayer == null) {
			ci.cancel();
			return;
		}
		
		var receiverChannel = receiverPlayer.hammer$getSelectedChannel();
		var senderChannel = senderPlayer.hammer$getSelectedChannel();
		
		if (receiverChannel != senderChannel) {
			ci.cancel();
			return;
		}
	}
}
