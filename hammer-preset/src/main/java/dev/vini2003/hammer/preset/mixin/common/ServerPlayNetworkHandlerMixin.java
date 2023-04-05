package dev.vini2003.hammer.preset.mixin.common;

import dev.vini2003.hammer.preset.HP;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V"), method = "onDisconnected")
	private void hammer$onDisconnected(PlayerManager instance, Text message, boolean overlay) {
		if (!HP.CONFIG.disableLeaveMessages) {
			instance.broadcast(message, overlay);
		}
	}
}
