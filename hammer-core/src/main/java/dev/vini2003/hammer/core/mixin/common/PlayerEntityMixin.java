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

package dev.vini2003.hammer.core.mixin.common;

import dev.vini2003.hammer.core.impl.common.accessor.PlayerEntityAccessor;
import dev.vini2003.hammer.core.registry.common.HCConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccessor {
	@Shadow
	protected HungerManager hungerManager;
	@Shadow
	@Final
	private PlayerAbilities abilities;
	
	@Unique
	private static final TrackedData<Boolean> FROZEN = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	private static final TrackedData<Float> ATTACK_DAMAGE_MULTIPLIER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> DAMAGE_MULTIPLIER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> FALL_DAMAGE_MULTIPLIER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> MOVEMENT_SPEED_MULTIPLIER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> JUMP_MULTIPLIER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> HEAL_MULTIPLIER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> EXHAUSTION_MULTIPLIER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(at = @At("RETURN"), method = "initDataTracker")
	private void hammer$initDataTracker(CallbackInfo ci) {
		dataTracker.startTracking(FROZEN, false);
		
		if (HCConfig.ENABLE_ATTACK_DAMAGE_MULTIPLIER) {
			dataTracker.startTracking(ATTACK_DAMAGE_MULTIPLIER, 1.0F);
		}
		
		if (HCConfig.ENABLE_DAMAGE_MULTIPLIER) {
			dataTracker.startTracking(DAMAGE_MULTIPLIER, 1.0F);
		}
		
		if (HCConfig.ENABLE_FALL_DAMAGE_MULTIPLIER) {
			dataTracker.startTracking(FALL_DAMAGE_MULTIPLIER, 1.0F);
		}
		
		if (HCConfig.ENABLE_MOVEMENT_SPEED_MULTIPLIER) {
			dataTracker.startTracking(MOVEMENT_SPEED_MULTIPLIER, 1.0F);
		}
		
		if (HCConfig.ENABLE_JUMP_MULTIPLIER) {
			dataTracker.startTracking(JUMP_MULTIPLIER, 1.0F);
		}
		
		if (HCConfig.ENABLE_HEAL_MULTIPLIER) {
			dataTracker.startTracking(HEAL_MULTIPLIER, 1.0F);
		}
		
		if (HCConfig.ENABLE_EXHAUSTION_MULTIPLIER) {
			dataTracker.startTracking(EXHAUSTION_MULTIPLIER, 1.0F);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "writeCustomDataToNbt")
	private void hammer$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putBoolean("Hammer$Frozen", dataTracker.get(FROZEN));
		
		if (HCConfig.ENABLE_ATTACK_DAMAGE_MULTIPLIER) {
			nbt.putFloat("Hammer$AttackDamageMultiplier", dataTracker.get(ATTACK_DAMAGE_MULTIPLIER));
		}
		
		if (HCConfig.ENABLE_DAMAGE_MULTIPLIER) {
			nbt.putFloat("Hammer$DamageMultiplier", dataTracker.get(DAMAGE_MULTIPLIER));
		}
		
		if (HCConfig.ENABLE_FALL_DAMAGE_MULTIPLIER) {
			nbt.putFloat("Hammer$FallDamageMultiplier", dataTracker.get(FALL_DAMAGE_MULTIPLIER));
		}
		
		if (HCConfig.ENABLE_MOVEMENT_SPEED_MULTIPLIER) {
			nbt.putFloat("Hammer$MovementSpeedMultiplier", dataTracker.get(MOVEMENT_SPEED_MULTIPLIER));
		}
		
		if (HCConfig.ENABLE_JUMP_MULTIPLIER) {
			nbt.putFloat("Hammer$JumpMultiplier", dataTracker.get(JUMP_MULTIPLIER));
		}
		
		if (HCConfig.ENABLE_HEAL_MULTIPLIER) {
			nbt.putFloat("Hammer$HealMultiplier", dataTracker.get(HEAL_MULTIPLIER));
		}
		
		if (HCConfig.ENABLE_EXHAUSTION_MULTIPLIER) {
			nbt.putFloat("Hammer$ExhaustionMultiplier", dataTracker.get(EXHAUSTION_MULTIPLIER));
		}
	}
	
	@Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
	private void hammer$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		if (nbt.contains("Hammer$Frozen")) {
			dataTracker.set(FROZEN, nbt.getBoolean("Hammer$Frozen"));
		}
		
		if (HCConfig.ENABLE_ATTACK_DAMAGE_MULTIPLIER && nbt.contains("Hammer$AttackDamageMultiplier")) {
			dataTracker.set(ATTACK_DAMAGE_MULTIPLIER, nbt.getFloat("Hammer$AttackDamageMultiplier"));
		}
		
		if (HCConfig.ENABLE_DAMAGE_MULTIPLIER && nbt.contains("Hammer$DamageMultiplier")) {
			dataTracker.set(DAMAGE_MULTIPLIER, nbt.getFloat("Hammer$DamageMultiplier"));
		}
		
		if (HCConfig.ENABLE_FALL_DAMAGE_MULTIPLIER && nbt.contains("Hammer$FallDamageMultiplier")) {
			dataTracker.set(FALL_DAMAGE_MULTIPLIER, nbt.getFloat("Hammer$FallDamageMultiplier"));
		}
		
		if (HCConfig.ENABLE_MOVEMENT_SPEED_MULTIPLIER && nbt.contains("Hammer$MovementSpeedMultiplier")) {
			dataTracker.set(MOVEMENT_SPEED_MULTIPLIER, nbt.getFloat("Hammer$MovementSpeedMultiplier"));
		}
		
		if (HCConfig.ENABLE_JUMP_MULTIPLIER && nbt.contains("Hammer$JumpMultiplier")) {
			dataTracker.set(JUMP_MULTIPLIER, nbt.getFloat("Hammer$JumpMultiplier"));
		}
		
		if (HCConfig.ENABLE_HEAL_MULTIPLIER && nbt.contains("Hammer$HealMultiplier")) {
			dataTracker.set(HEAL_MULTIPLIER, nbt.getFloat("Hammer$HealMultiplier"));
		}
		
		if (HCConfig.ENABLE_EXHAUSTION_MULTIPLIER && nbt.contains("Hammer$ExhaustionMultiplier")) {
			dataTracker.set(EXHAUSTION_MULTIPLIER, nbt.getFloat("Hammer$ExhaustionMultiplier"));
		}
	}
	
	@Override
	public void hammer$setFrozen(boolean frozen) {
		dataTracker.set(FROZEN, frozen);
	}
	
	@Override
	public boolean hammer$isFrozen() {
		return dataTracker.get(FROZEN);
	}
	
	@Override
	public float hammer$getAttackDamageMultiplier() {
		return dataTracker.get(ATTACK_DAMAGE_MULTIPLIER);
	}
	
	@Override
	public void hammer$setAttackDamageMultiplier(float attackDamageMultiplier) {
		dataTracker.set(ATTACK_DAMAGE_MULTIPLIER, attackDamageMultiplier);
	}
	
	@Override
	public float hammer$getDamageMultiplier() {
		return dataTracker.get(DAMAGE_MULTIPLIER);
	}
	
	@Override
	public void hammer$setDamageMultiplier(float damageMultiplier) {
		dataTracker.set(DAMAGE_MULTIPLIER, damageMultiplier);
	}
	
	@Override
	public float hammer$getFallDamageMultiplier() {
		return dataTracker.get(FALL_DAMAGE_MULTIPLIER);
	}
	
	@Override
	public void hammer$setFallDamageMultiplier(float fallDamageMultiplier) {
		dataTracker.set(FALL_DAMAGE_MULTIPLIER, fallDamageMultiplier);
	}
	
	@Override
	public float hammer$getMovementSpeedMultiplier() {
		return dataTracker.get(MOVEMENT_SPEED_MULTIPLIER);
	}
	
	@Override
	public void hammer$setMovementSpeedMultiplier(float movementSpeedMultiplier) {
		dataTracker.set(MOVEMENT_SPEED_MULTIPLIER, movementSpeedMultiplier);
	}
	
	@Override
	public float hammer$getJumpMultiplier() {
		return dataTracker.get(JUMP_MULTIPLIER);
	}
	
	@Override
	public void hammer$setJumpMultiplier(float jumpMultiplier) {
		dataTracker.set(JUMP_MULTIPLIER, jumpMultiplier);
	}
	
	@Override
	public float hammer$getHealMultiplier() {
		return dataTracker.get(HEAL_MULTIPLIER);
	}
	
	@Override
	public void hammer$setHealMultiplier(float healMultiplier) {
		dataTracker.set(HEAL_MULTIPLIER, healMultiplier);
	}
	
	@Override
	public float hammer$getExhaustionMultiplier() {
		return dataTracker.get(EXHAUSTION_MULTIPLIER);
	}
	
	@Override
	public void hammer$setExhaustionMultiplier(float exhaustionMultiplier) {
		dataTracker.set(EXHAUSTION_MULTIPLIER, exhaustionMultiplier);
	}
	
	@Inject(at = @At("RETURN"), method = "isBlockBreakingRestricted", cancellable = true)
	private void hammer$isBlockBreakingRestricted(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
		if (hammer$isFrozen()) {
			cir.setReturnValue(true);
			cir.cancel();
		}
	}
	
	@ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"), method = "damage")
	private void hammer$damage(Args args) {
		if (HCConfig.ENABLE_DAMAGE_MULTIPLIER) {
			args.set(1, ((float) args.get(1)) * hammer$getDamageMultiplier());
		}
	}
	
	@Inject(at = @At("HEAD"), method = "addExhaustion", cancellable = true)
	private void hammer$addExhaustion(float exhaustion, CallbackInfo ci) {
		if (HCConfig.ENABLE_EXHAUSTION_MULTIPLIER) {
			if (!abilities.invulnerable) {
				if (!world.isClient) {
					hungerManager.addExhaustion(exhaustion * hammer$getExhaustionMultiplier());
					
					ci.cancel();
				}
			}
		}
	}
	
	@Inject(at = @At("RETURN"), method = "getMovementSpeed", cancellable = true)
	private void hammer$getMovementSpeed(CallbackInfoReturnable<Float> cir) {
		if (HCConfig.ENABLE_MOVEMENT_SPEED_MULTIPLIER) {
			cir.setReturnValue(cir.getReturnValueF() * hammer$getMovementSpeedMultiplier());
		}
	}
	
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D", ordinal = 0), method = "attack")
	private double hammer$attack_getAttributeValue(PlayerEntity instance, EntityAttribute entityAttribute) {
		if (HCConfig.ENABLE_ATTACK_DAMAGE_MULTIPLIER) {
			return instance.getAttributeValue(entityAttribute) * hammer$getAttackDamageMultiplier();
		}
		
		return instance.getAttributeValue(entityAttribute);
	}
	
	@Inject(at = @At("HEAD"), method = "handleFallDamage", cancellable = true)
	private void hammer$handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		if (HCConfig.ENABLE_FALL_DAMAGE_MULTIPLIER) {
			if (hammer$getFallDamageMultiplier() == 0.0F) {
				cir.setReturnValue(false);
				cir.cancel();
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "travel", cancellable = true)
	private void hammer$travel(CallbackInfo ci) {
		if (hammer$isFrozen()) {
			ci.cancel();
		}
	}
}
