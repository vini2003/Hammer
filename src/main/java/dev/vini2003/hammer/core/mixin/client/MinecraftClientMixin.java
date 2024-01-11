package dev.vini2003.hammer.core.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
}
