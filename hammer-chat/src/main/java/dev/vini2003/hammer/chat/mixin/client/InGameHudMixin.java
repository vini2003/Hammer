package dev.vini2003.hammer.chat.mixin.client;

import dev.vini2003.hammer.chat.registry.common.HCUuids;
import dev.vini2003.hammer.chat.registry.common.HCValues;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.UUID;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Inject(method = "addChatMessage", at = @At("HEAD"), cancellable = true)
	private void hammer$chat$addChatMessage(MessageType type, Text message, UUID sender, CallbackInfo ci) {
		if (type == MessageType.SYSTEM && Objects.equals(sender, HCUuids.COMMAND_FEEDBACK_UUID)) {
			if (!HCValues.SHOW_FEEDBACK) {
				ci.cancel();
			}
		} else if (type == MessageType.CHAT) {
			if (!HCValues.SHOW_CHAT || !HCValues.SHOW_GLOBAL_CHAT) {
				ci.cancel();
			}
		}
	}
}
