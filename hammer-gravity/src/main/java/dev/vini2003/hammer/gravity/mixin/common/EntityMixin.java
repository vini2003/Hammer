package dev.vini2003.hammer.gravity.mixin.common;

import dev.vini2003.hammer.gravity.common.manager.GravityManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow public World world;
	
	@ModifyVariable(at = @At("HEAD"), method = "handleFallDamage(FFLnet/minecraft/entity/damage/DamageSource;)Z", index = 1)
	float hammer$handleFallDamage$getDamageMultiplier(float damageMultiplier) {
		var gravity = GravityManager.get(world.getRegistryKey());
		
		if (gravity != null) {
			return damageMultiplier * (gravity / 0.08F);
		}
		
		return damageMultiplier;
	}
}
