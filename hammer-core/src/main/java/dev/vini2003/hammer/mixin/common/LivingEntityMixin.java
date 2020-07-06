package dev.vini2003.hammer.mixin.common;

import dev.vini2003.hammer.common.entity.ConfigurablePlayerEntity;
import dev.vini2003.hammer.registry.common.HConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(at = @At("RETURN"), method = "computeFallDamage", cancellable = true)
	private void hammer$computeFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir) {
		if (HConfig.ENABLE_FALL_DAMAGE_MULTIPLIER) {
			if (this instanceof ConfigurablePlayerEntity player) {
				cir.setReturnValue((int) (cir.getReturnValueI() * player.hammer$getFallDamageMultiplier()));
			}
		}
	}
	
	@Inject(at = @At("RETURN"), method = "getJumpVelocity", cancellable = true)
	private void hammer$getJumpVelocity(CallbackInfoReturnable<Float> cir) {
		if (HConfig.ENABLE_JUMP_MULTIPLIER) {
			if (this instanceof ConfigurablePlayerEntity player) {
				cir.setReturnValue(cir.getReturnValueF() * player.hammer$getJumpMultiplier());
			}
		}
	}
	
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setHealth(F)V"), method = "heal")
	private void hammer$heal_setHealth(LivingEntity instance, float health) {
		if (HConfig.ENABLE_HEAL_MULTIPLIER) {
			if (this instanceof ConfigurablePlayerEntity player) {
				var amount = health - instance.getHealth();
				
				instance.setHealth(instance.getHealth() + (amount * player.hammer$getHealMultiplier()));
			}
		}
	}
}
