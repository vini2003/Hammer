package dev.vini2003.hammer.core.mixin.client;

import dev.vini2003.hammer.core.api.common.util.PlayerUtil;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
	@Inject(at = @At("HEAD"), method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", cancellable = true)
	void hammer$renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		// TODO: Check if this is equivalent to the 1.19.2 code.
		
		if (entity instanceof PlayerEntity player) {
			var hand = leftHanded ? Hand.OFF_HAND : Hand.MAIN_HAND;
			
			if ((!PlayerUtil.hasLeftArm(player) && hand == Hand.OFF_HAND) || (!PlayerUtil.hasRightArm(player) && hand == Hand.MAIN_HAND)) {
				ci.cancel();
			}
		}
	}
}
