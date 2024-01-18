package dev.vini2003.hammer.preset.mixin.common;

import dev.vini2003.hammer.preset.HP;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V"), method = "onPlayerConnect")
	private void hammer$onPlayerConnect$broadcast(PlayerManager instance, Text message, boolean overlay) {
		if (!HP.CONFIG.disableJoinMessages) {
			instance.broadcast(message, overlay);
		}
	}
}
