package dev.vini2003.hammer.preset.mixin.client;

import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastManager.class)
public class ToastManagerMixin {
	@Inject(at = @At("HEAD"), method = "add", cancellable = true)
	public void hammer$add(Toast toast, CallbackInfo ci) {
		ci.cancel();
	}
}
