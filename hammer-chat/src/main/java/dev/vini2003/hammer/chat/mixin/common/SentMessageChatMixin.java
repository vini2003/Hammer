package dev.vini2003.hammer.chat.mixin.common;

import net.minecraft.network.message.SentMessage;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SentMessage.Chat.class)
public class SentMessageChatMixin {
	// TODO: Check if this is equivalent to the 1.19.2 code.
//	@Shadow
//	@Final
//	private Set<ServerPlayerEntity> recipients;
//
//	@Inject(at = @At("HEAD"), method = "send")
//	private void hammer$send(ServerPlayerEntity sender, boolean filterMaskEnabled, MessageType.Parameters params, CallbackInfo ci) {
//		if (ChatUtil.isMuted(sender)) {
//			sender.sendMessage(Text.translatable("text.hammer.muted").formatted(Formatting.RED), false);
//
//			return;
//		}
//
//		var senderChannel = ChannelUtil.getSelected(sender);
//
//		recipients.removeIf(recipient -> {
//			var recipientChannel = ChannelUtil.getSelected(recipient);
//
//			return senderChannel == recipientChannel;
//		});
//	}
}
