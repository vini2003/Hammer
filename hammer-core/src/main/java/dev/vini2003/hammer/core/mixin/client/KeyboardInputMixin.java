package dev.vini2003.hammer.core.mixin.client;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.util.PlayerUtil;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input {
	@Inject(at = @At("RETURN"), method = "tick")
	private void hammer$tick(boolean slowDown, float f, CallbackInfo ci) {
		var client = InstanceUtil.getClient();
		
		if (client != null && client.player != null) {
			var player = client.player;
			
			if (!PlayerUtil.hasAnyLeg(player) || !PlayerUtil.allowMovement(player)) {
				pressingForward = false;
				pressingBack = false;
				pressingLeft = false;
				pressingRight = false;
				movementForward = 0.0F;
				movementSideways = 0.0F;
				jumping = false;
				sneaking = false;
			}
		}
	}
}
