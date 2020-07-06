package dev.vini2003.hammer.mixin.client;

import dev.vini2003.hammer.client.util.InstanceUtils;
import dev.vini2003.hammer.common.entity.ConfigurablePlayerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@Inject(at = @At("HEAD"), method = "drawBlockOutline", cancellable = true)
	private void hammer$drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos pos, BlockState state, CallbackInfo ci) {
		var client = InstanceUtils.getClient();
		
		if (((ConfigurablePlayerEntity) client.player).hammer$isFrozen()) {
			ci.cancel();
		}
	}
}
