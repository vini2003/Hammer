package dev.vini2003.hammer.mixin.common;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
	// Stop movement speed warnings!
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;isHost()Z"), method = "onPlayerMove")
	private boolean hammer$onPlayerMove$isHost(ServerPlayNetworkHandler instance) {
		return true;
	}
}
