package dev.vini2003.hammer.chat.mixin.common;

import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SharedConstants.class)
public class SharedConstantsMixin {
	@Inject(at = @At("RETURN"), method = "isValidChar", cancellable = true)
	private static void hammer$isValidChar(char chr, CallbackInfoReturnable<Boolean> cir) {
		if (chr == '\u00a7' && !cir.getReturnValueZ()) {
			cir.setReturnValue(true);
		}
	}
}
