package dev.vini2003.hammer.util.mixin.client;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.util.api.common.item.TriggerItem;
import dev.vini2003.hammer.util.api.common.util.RaycastUtil;
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
		if (world.isClient) {
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
