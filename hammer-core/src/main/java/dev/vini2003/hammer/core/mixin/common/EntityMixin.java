package dev.vini2003.hammer.core.mixin.common;

import dev.vini2003.hammer.core.api.common.util.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
	@Shadow
	private Vec3d velocity;
	
	@Inject(at = @At("HEAD"), method = "setVelocity(Lnet/minecraft/util/math/Vec3d;)V", cancellable = true)
	private void hammer$setVelocity(Vec3d velocity, CallbackInfo ci) {
		if ((Object) this instanceof PlayerEntity player && PlayerUtil.isFrozen(player)) {
			this.velocity = Vec3d.ZERO;
			
			ci.cancel();
		}
	}
	
	@Inject(at = @At("HEAD"), method = "addVelocity", cancellable = true)
	private void hammer$addVelocity(double deltaX, double deltaY, double deltaZ, CallbackInfo ci) {
		if ((Object) this instanceof PlayerEntity player && PlayerUtil.isFrozen(player)) {
			this.velocity = Vec3d.ZERO;
			
			ci.cancel();
		}
	}
}
