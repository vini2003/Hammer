package dev.vini2003.hammer.core.mixin.server;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InstanceUtil.class)
public class InstanceUtilMixin {
	@Inject(at = @At("HEAD"), method = "getServer", remap = false, cancellable = true)
	private static void getServer(CallbackInfoReturnable<MinecraftServer> cir) {
		cir.setReturnValue((MinecraftServer) FabricLoader.getInstance().getGameInstance());
	}
}
