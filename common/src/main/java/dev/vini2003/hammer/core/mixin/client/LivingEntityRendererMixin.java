package dev.vini2003.hammer.core.mixin.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V", ordinal = 1), method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
	void hammer$render(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if (livingEntity instanceof PlayerEntity player) {
			var dimensions = livingEntity.getDimensions(livingEntity.getPose());
			
			if (player.hammer$hasHead()) {
				matrixStack.translate(0F, 1.8F - dimensions.height, 0F);
			} else if ((player.hammer$hasLeftArm() || player.hammer$hasRightArm() || player.hammer$hasTorso()) && (!player.hammer$hasLeftLeg() && !player.hammer$hasRightLeg())) {
				matrixStack.translate(0F, 0.675F, 0F);
			}
		}
	}
}
