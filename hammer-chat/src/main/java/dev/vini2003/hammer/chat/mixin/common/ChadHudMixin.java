package dev.vini2003.hammer.chat.mixin.common;

import dev.vini2003.hammer.chat.api.common.util.ChatUtil;
import dev.vini2003.hammer.chat.registry.client.HCKeyBinds;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatHud.class)
public class ChadHudMixin {
	@Inject(method = "isChatFocused", at = @At("RETURN"), cancellable = true)
	private void isChatFocused(CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValueZ() && HCKeyBinds.SHOW_CHAT.isPressed()) {
			cir.setReturnValue(true);
		}
	}
	
	@ModifyConstant(method = "render", constant = @Constant(intValue = 200))
	private int modifyTime(int original) {
		var client = InstanceUtil.getClient();
		
		if (client.player != null && ChatUtil.hasFastChatFade(client.player)) {
			return 200 / 4;
		} else {
			return original;
		}
	}
	
	@ModifyConstant(method = "getMessageOpacityMultiplier", constant = @Constant(doubleValue = 200.0D))
	private static double modifyTime(double original) {
		var client = InstanceUtil.getClient();
		
		if (client.player != null && ChatUtil.hasFastChatFade(client.player)) {
			return 200.0D / 4.0D;
		} else {
			return original;
		}
	}
}
