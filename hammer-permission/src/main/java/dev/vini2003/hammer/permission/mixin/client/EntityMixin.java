package dev.vini2003.hammer.permission.mixin.client;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.util.RaycastUtil;
import dev.vini2003.hammer.permission.api.common.util.RoleUtil;
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
			
			if (RoleUtil.hasRoleOutline(player)) {
				cir.setReturnValue(true);
			}
		}
	}
}
