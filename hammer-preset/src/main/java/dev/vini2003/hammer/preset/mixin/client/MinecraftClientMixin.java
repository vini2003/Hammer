package dev.vini2003.hammer.preset.mixin.client;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Inject(at = @At("RETURN"), method = "getWindowTitle", cancellable = true)
	private void hidenseek$getWindowTitle(CallbackInfoReturnable<String> cir) {
		for (var container : InstanceUtil.getFabric().getAllMods()) {
			if (container.getMetadata().containsCustomValue("windowName")) {
				var windowName = container.getMetadata().getCustomValue("windowName").getAsString();
				
				cir.setReturnValue(windowName + " - " + cir.getReturnValue());
			}
		}
	}
	
	@ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setIcon(Ljava/io/InputStream;Ljava/io/InputStream;)V"), method = "<init>")
	private void hidenseek$init_setIcon(Args args) {
		args.set(0, HC.class.getResourceAsStream("/assets/hammer/icons/icon_16x16.png"));
		args.set(1, HC.class.getResourceAsStream("/assets/hammer/icons/icon_32x32.png"));
	}
}
