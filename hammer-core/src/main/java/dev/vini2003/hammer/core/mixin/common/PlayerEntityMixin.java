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
	private static final TrackedData<Boolean> HAMMER$FROZEN = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	private static final TrackedData<Float> HAMMER$ATTACK_DAMAGE_MULTIPLIER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> HAMMER$DAMAGE_MULTIPLIER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> HAMMER$FALL_DAMAGE_MULTIPLIER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> HAMMER$MOVEMENT_SPEED_MULTIPLIER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> HAMMER$JUMP_MULTIPLIER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> HAMMER$HEAL_MULTIPLIER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private static final TrackedData<Float> HAMMER$EXHAUSTION_MULTIPLIER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(at = @At("RETURN"), method = "initDataTracker")
	private void hammer$initDataTracker(CallbackInfo ci) {
		dataTracker.startTracking(HAMMER$FROZEN, false);
		
		dataTracker.startTracking(HAMMER$ATTACK_DAMAGE_MULTIPLIER, 1.0F);
		dataTracker.startTracking(HAMMER$DAMAGE_MULTIPLIER, 1.0F);
		dataTracker.startTracking(HAMMER$FALL_DAMAGE_MULTIPLIER, 1.0F);
		dataTracker.startTracking(HAMMER$MOVEMENT_SPEED_MULTIPLIER, 1.0F);
		dataTracker.startTracking(HAMMER$JUMP_MULTIPLIER, 1.0F);
		dataTracker.startTracking(HAMMER$HEAL_MULTIPLIER, 1.0F);
		dataTracker.startTracking(HAMMER$EXHAUSTION_MULTIPLIER, 1.0F);
	}
	
	@Override
	public void hammer$setFrozen(boolean frozen) {
		dataTracker.set(HAMMER$FROZEN, frozen);
	}
	
	@Override
	public boolean hammer$isFrozen() {
		return dataTracker.get(HAMMER$FROZEN);
	}
	
	@Override
	public float hammer$getAttackDamageMultiplier() {
		return dataTracker.get(HAMMER$ATTACK_DAMAGE_MULTIPLIER);
	}
	
	@Override
	public void hammer$setAttackDamageMultiplier(float attackDamageMultiplier) {
		dataTracker.set(HAMMER$ATTACK_DAMAGE_MULTIPLIER, attackDamageMultiplier);
	}
	
	@Override
	public float hammer$getDamageMultiplier() {
		return dataTracker.get(HAMMER$DAMAGE_MULTIPLIER);
	}
	
	@Override
	public void hammer$setDamageMultiplier(float damageMultiplier) {
		dataTracker.set(HAMMER$DAMAGE_MULTIPLIER, damageMultiplier);
	}
	
	@Override
	public float hammer$getFallDamageMultiplier() {
		return dataTracker.get(HAMMER$FALL_DAMAGE_MULTIPLIER);
	}
	
	@Override
	public void hammer$setFallDamageMultiplier(float fallDamageMultiplier) {
		dataTracker.set(HAMMER$FALL_DAMAGE_MULTIPLIER, fallDamageMultiplier);
	}
	
	@Override
	public float hammer$getMovementSpeedMultiplier() {
		return dataTracker.get(HAMMER$MOVEMENT_SPEED_MULTIPLIER);
	}
	
	@Override
	public void hammer$setMovementSpeedMultiplier(float movementSpeedMultiplier) {
		dataTracker.set(HAMMER$MOVEMENT_SPEED_MULTIPLIER, movementSpeedMultiplier);
	}
	
	@Override
	public float hammer$getJumpMultiplier() {
		return dataTracker.get(HAMMER$JUMP_MULTIPLIER);
	}
	
	@Override
	public void hammer$setJumpMultiplier(float jumpMultiplier) {
		dataTracker.set(HAMMER$JUMP_MULTIPLIER, jumpMultiplier);
	}
	
	@Override
	public float hammer$getHealMultiplier() {
		return dataTracker.get(HAMMER$HEAL_MULTIPLIER);
	}
	
	@Override
	public void hammer$setHealMultiplier(float healMultiplier) {
		dataTracker.set(HAMMER$HEAL_MULTIPLIER, healMultiplier);
	}
	
	@Override
	public float hammer$getExhaustionMultiplier() {
		return dataTracker.get(HAMMER$EXHAUSTION_MULTIPLIER);
	}
	
	@Override
	public void hammer$setExhaustionMultiplier(float exhaustionMultiplier) {
		dataTracker.set(HAMMER$EXHAUSTION_MULTIPLIER, exhaustionMultiplier);
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
		args.set(1, ((float) args.get(1)) * hammer$getDamageMultiplier());
	}
	
	@Inject(at = @At("HEAD"), method = "addExhaustion", cancellable = true)
	private void hammer$addExhaustion(float exhaustion, CallbackInfo ci) {
		if (!abilities.invulnerable) {
			if (!world.isClient) {
				hungerManager.addExhaustion(exhaustion * hammer$getExhaustionMultiplier());
				
				ci.cancel();
			}
		}
	}
	
	@Inject(at = @At("RETURN"), method = "getMovementSpeed", cancellable = true)
	private void hammer$getMovementSpeed(CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(cir.getReturnValueF() * hammer$getMovementSpeedMultiplier());
	}
	
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D", ordinal = 0), method = "attack")
	private double hammer$attack_getAttributeValue(PlayerEntity instance, EntityAttribute entityAttribute) {
		return instance.getAttributeValue(entityAttribute) * hammer$getAttackDamageMultiplier();
	}
	
	@Inject(at = @At("HEAD"), method = "handleFallDamage", cancellable = true)
	private void hammer$handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		if (hammer$getFallDamageMultiplier() == 0.0F) {
			cir.setReturnValue(false);
			cir.cancel();
		}
	}
	
	@Inject(at = @At("HEAD"), method = "travel", cancellable = true)
	private void hammer$travel(CallbackInfo ci) {
		if (hammer$isFrozen()) {
			ci.cancel();
		}
	}
}
