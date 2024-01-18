package dev.vini2003.hammer.core.mixin.client;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow
	@Nullable
	public ClientPlayerEntity player;
	
	@Inject(at = @At("HEAD"), method = "doAttack", cancellable = true)
	void hammer$doAttack(CallbackInfoReturnable<Boolean> cir) {
		if (!player.hammer$hasRightArm() || !player.hammer$allowInteraction()) {
			cir.setReturnValue(false);
			cir.cancel();
		}
	}
	
	@Redirect(at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V"), method = "createUserApiService")
	void hammer$createUserApiService(Logger logger, String message, Throwable throwable) {
		if (HC.CONFIG.disableYggdrasilInDevelopmentEnvironment) {
			if (InstanceUtil.isDevelopmentEnvironment()) {
				HC.LOGGER.warn("Yggdrasil authentication is disabled in development environment.");
			}
		}
	}
}
