package dev.vini2003.hammer.chat.mixin.client;

import dev.vini2003.hammer.chat.registry.common.HCOptions;
import net.minecraft.client.gui.screen.option.ChatOptionsScreen;
import net.minecraft.client.option.Option;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatOptionsScreen.class)
public class ChatOptionsScreenMixin {
	@Shadow
	@Final
	private static Option[] OPTIONS;
	
	@Inject(at = @At("TAIL"), method = "<clinit>")
	private static void hammer$init(CallbackInfo ci) {
		var prevOptions = OPTIONS;
		
		OPTIONS = new Option[OPTIONS.length + 3];
		
		for (var i = 0; i < OPTIONS.length - 3; ++i) {
			OPTIONS[i] = prevOptions[i];
		}
		
		OPTIONS[prevOptions.length] = HCOptions.SHOW_CHAT;
		OPTIONS[prevOptions.length + 1] = HCOptions.SHOW_COMMAND_FEEDBACK;
		OPTIONS[prevOptions.length + 2] = HCOptions.SHOW_WARNINGS;
	}
}
