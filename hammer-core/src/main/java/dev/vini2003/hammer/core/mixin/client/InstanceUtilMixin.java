package dev.vini2003.hammer.core.mixin.client;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InstanceUtil.class)
public class InstanceUtilMixin {
	@Inject(at = @At("HEAD"), method = "getServer", remap = false, cancellable = true)
	private static void getServer(CallbackInfoReturnable<MinecraftServer> cir) {
		cir.setReturnValue(MinecraftClient.getInstance().getServer());
	}
}
