package dev.vini2003.hammer.chat.mixin.common;

import dev.vini2003.hammer.chat.HC;
import net.minecraft.server.ServerMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerMetadata.class)
public class ServerMetadataMixin {
	@Inject(method = "secureChatEnforced", at = @At("HEAD"), cancellable = true)
	public void hammer$secureChatCheck(CallbackInfoReturnable<Boolean> info) {
		if (HC.CONFIG.disableChatSigning) {
			info.setReturnValue(true);
		}
	}
}
