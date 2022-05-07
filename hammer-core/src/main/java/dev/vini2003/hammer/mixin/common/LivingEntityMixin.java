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

package dev.vini2003.hammer.mixin.common;

import dev.vini2003.hammer.core.impl.common.accessor.PlayerEntityAccessor;
import dev.vini2003.hammer.core.registry.common.HCConfig;
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
		if (HCConfig.ENABLE_FALL_DAMAGE_MULTIPLIER) {
			if (this instanceof PlayerEntityAccessor player) {
				cir.setReturnValue((int) (cir.getReturnValueI() * player.hammer$getFallDamageMultiplier()));
			}
		}
	}
	
	@Inject(at = @At("RETURN"), method = "getJumpVelocity", cancellable = true)
	private void hammer$getJumpVelocity(CallbackInfoReturnable<Float> cir) {
		if (HCConfig.ENABLE_JUMP_MULTIPLIER) {
			if (this instanceof PlayerEntityAccessor player) {
				cir.setReturnValue(cir.getReturnValueF() * player.hammer$getJumpMultiplier());
			}
		}
	}
	
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setHealth(F)V"), method = "heal")
	private void hammer$heal_setHealth(LivingEntity instance, float health) {
		if (HCConfig.ENABLE_HEAL_MULTIPLIER) {
			if (this instanceof PlayerEntityAccessor player) {
				var amount = health - instance.getHealth();
				
				instance.setHealth(instance.getHealth() + (amount * player.hammer$getHealMultiplier()));
			}
		}
	}
}
