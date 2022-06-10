package dev.vini2003.hammer.util.mixin.client;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.util.api.common.item.TriggerItem;
import dev.vini2003.hammer.core.api.common.util.RaycastUtil;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
	@Inject(at = @At("HEAD"), method = "render")
	private <E extends Entity> void hammer$render(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light, CallbackInfo ci) {
		var client = InstanceUtil.getClient();
		
		var player = client.player;
		
		if (player == null) {
			return;
		}
		
		var hand = player.getActiveHand();
		
		var stack = player.getStackInHand(hand);
		
		if (stack.getItem() instanceof TriggerItem triggerItem) {
			var raycastEntityHitResult = RaycastUtil.raycastEntity(player, tickDelta, 256.0F);
			
			if (raycastEntityHitResult != null) {
				if (triggerItem.shouldOutlineEntityRaycast(raycastEntityHitResult.getEntity())) {
					if (raycastEntityHitResult.getEntity().getId() == entity.getId()) {
						var outlineProvider = client.worldRenderer.bufferBuilders.getOutlineVertexConsumers();
						
						outlineProvider.setColor(255, 252, 71, 255);
					}
				}
			}
		}
	}
}
