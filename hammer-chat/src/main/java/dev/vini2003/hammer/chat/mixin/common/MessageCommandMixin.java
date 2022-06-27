package dev.vini2003.hammer.chat.mixin.common;

import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import net.minecraft.server.command.MessageCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;

@Mixin(MessageCommand.class)
public class MessageCommandMixin {
	@Inject(at = @At("HEAD"), method = "execute", cancellable = true)
	private static void hammer$execute(ServerCommandSource source, Collection<ServerPlayerEntity> targets, Text message, CallbackInfoReturnable<Integer> cir) {
		var targetsToRemove = new ArrayList<ServerPlayerEntity>();
		
		for (var target : targets) {
			if (!ChatUtil.shouldShowDirectMessages(target)) {
				source.sendFeedback(new TranslatableText("text.hammer.message_command.direct_messages_disabled", target.getDisplayName()), false);
			}
		}
		
		targets.removeAll(targetsToRemove);
	}
}
