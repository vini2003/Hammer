package dev.vini2003.hammer.chat.mixin.common;

import dev.vini2003.hammer.chat.api.common.util.ChannelUtil;
import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SentMessage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(SentMessage.Chat.class)
public class SentMessageChatMixin {
	@Shadow
	@Final
	private Set<ServerPlayerEntity> recipients;
	
	@Inject(at = @At("HEAD"), method = "send")
	private void hammer$send(ServerPlayerEntity sender, boolean filterMaskEnabled, MessageType.Parameters params, CallbackInfo ci) {
		if (ChatUtil.isMuted(sender)) {
			sender.sendMessage(Text.translatable("text.hammer.muted").formatted(Formatting.RED), false);
			
			return;
		}
		
		var senderChannel = ChannelUtil.getSelected(sender);
		
		recipients.removeIf(recipient -> {
			var recipientChannel = ChannelUtil.getSelected(recipient);
			
			return senderChannel == recipientChannel;
		});
	}
}
