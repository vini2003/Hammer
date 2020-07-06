package dev.vini2003.hammer.gravity.mixin.common;

import dev.vini2003.hammer.gravity.common.manager.GravityManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> entityType, World world) {
		super(entityType, world);
	}
	
	@ModifyVariable(at = @At("HEAD"), method = "handleFallDamage(FFLnet/minecraft/entity/damage/DamageSource;)Z", index = 1)
	float hammer$handleFallDamage$getDamageMultiplier(float damageMultiplier) {
		var gravity = GravityManager.get(world.getRegistryKey());
		
		if (gravity != null) {
			return damageMultiplier * (gravity / 0.08F);
		}
		
		return damageMultiplier;
	}
	
	@ModifyConstant(method = "travel(Lnet/minecraft/util/math/Vec3d;)V", constant = @Constant(doubleValue = 0.08D, ordinal = 0))
	private double hammer$travel(double original) {
		var gravity = GravityManager.get(world.getRegistryKey());
		
		if (gravity != null) {
			return gravity;
		}
		
		return original;
	}
}
