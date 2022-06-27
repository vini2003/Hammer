package dev.vini2003.hammer.permission.mixin.client;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@ModifyVariable(
			method = "render",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilderStorage;getEntityVertexConsumers()Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;"),
			ordinal = 3
	)
	private boolean hammer$render$getEntityVertexConsumers(boolean original) {
		return true;
	}
}
