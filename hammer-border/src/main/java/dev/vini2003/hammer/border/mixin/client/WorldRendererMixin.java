package dev.vini2003.hammer.border.mixin.client;

import dev.vini2003.hammer.border.client.renderer.WorldBorderRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@Shadow @Nullable private ClientWorld world;
	
	@Shadow @Final private MinecraftClient client;
	
	@Shadow @Final private static Identifier FORCEFIELD;
	
	@Inject(at = @At("HEAD"), method = "renderWorldBorder", cancellable = true)
	private void hammer$renderWorldBorder(Camera camera, CallbackInfo ci) {
		WorldBorderRenderer.render(client, world, camera, FORCEFIELD);
			
		ci.cancel();
	}
}
