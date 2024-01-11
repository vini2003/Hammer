/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.util.mixin.client;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.util.RaycastUtil;
import dev.vini2003.hammer.util.api.common.item.TriggerItem;
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
