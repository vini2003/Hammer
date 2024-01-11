package dev.vini2003.hammer.chat.mixin.common;

import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class PlayerManagerMixin {
	@Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
	private void hammer$sendChatMessage(SentMessage message, boolean filterMaskEnabled, MessageType.Parameters params, CallbackInfo ci) {
	
	}
}
