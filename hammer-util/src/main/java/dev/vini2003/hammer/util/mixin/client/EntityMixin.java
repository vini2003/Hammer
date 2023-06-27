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
import dev.vini2003.hammer.util.api.common.item.TriggerItem;
import dev.vini2003.hammer.core.api.common.util.RaycastUtil;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public World world;
	
	@Shadow
	public abstract int getId();
	
	@Inject(at = @At("RETURN"), method = "isGlowing", cancellable = true)
	private void hammer$isGlowing(CallbackInfoReturnable<Boolean> cir) {
		if (world.isClient()) {
			var client = InstanceUtil.getClient();
			
			var player = client.player;
			
			if (player == null) {
				return;
			}
			
			var hand = player.getActiveHand();
			
			var stack = player.getStackInHand(hand);
			
			if (stack.getItem() instanceof TriggerItem triggerItem) {
				var raycastEntityHitResult = RaycastUtil.raycastEntity(player, 0.0F, 256.0F);
					
				if (raycastEntityHitResult != null) {
					if (raycastEntityHitResult.getEntity().getId() == getId()) {
						if (triggerItem.shouldOutlineEntityRaycast(raycastEntityHitResult.getEntity())) {
							cir.setReturnValue(true);
						}
					}
				}
			}
		}
	}
}
