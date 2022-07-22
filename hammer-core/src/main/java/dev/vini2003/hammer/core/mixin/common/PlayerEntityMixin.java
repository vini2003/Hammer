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

import dev.vini2003.hammer.core.api.common.component.TrackedDataComponent;
import dev.vini2003.hammer.core.api.common.data.TrackedDataHandler;
import dev.vini2003.hammer.core.impl.common.accessor.PlayerEntityAccessor;
import dev.vini2003.hammer.core.registry.common.HCComponents;
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
	
	private final TrackedDataHandler<Boolean> hammer$frozen = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, false, "Frozen");
	
	private final TrackedDataHandler<Float> hammer$attackDamageMultiplier = new TrackedDataHandler(() -> TrackedDataComponent.get(this), Float.class, 1.0F,"AttackDamageMultiplier");
	private final TrackedDataHandler<Float> hammer$damageMultiplier = new TrackedDataHandler(() -> TrackedDataComponent.get(this), Float.class, 1.0F, "DamageMultiplier");
	private final TrackedDataHandler<Float> hammer$fallDamageMultiplier = new TrackedDataHandler(() -> TrackedDataComponent.get(this), Float.class, 1.0F, "FallDamageMultiplier");
	private final TrackedDataHandler<Float> hammer$movementSpeedMultiplier = new TrackedDataHandler(() -> TrackedDataComponent.get(this), Float.class, 1.0F, "MovementSpeedMultiplier");
	private final TrackedDataHandler<Float> hammer$jumpMultiplier = new TrackedDataHandler(() -> TrackedDataComponent.get(this), Float.class, 1.0F, "JumpMultiplier");
	private final TrackedDataHandler<Float> hammer$healMultiplier = new TrackedDataHandler(() -> TrackedDataComponent.get(this), Float.class, 1.0F, "HealMultiplier");
	private final TrackedDataHandler<Float> hammer$exhaustionMultiplier = new TrackedDataHandler(() -> TrackedDataComponent.get(this), Float.class, 1.0F, "ExhaustionMultiplier");
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Override
	public boolean hammer$isFrozen() {
		return hammer$frozen.get();
	}
	
	@Override
	public void hammer$setFrozen(boolean frozen) {
		hammer$frozen.set(frozen);
	}
	
	@Override
	public float hammer$getAttackDamageMultiplier() {
		return hammer$attackDamageMultiplier.get();
	}
	
	@Override
	public void hammer$setAttackDamageMultiplier(float attackDamageMultiplier) {
		hammer$attackDamageMultiplier.set(attackDamageMultiplier);
	}
	
	@Override
	public float hammer$getDamageMultiplier() {
		return hammer$damageMultiplier.get();
	}
	
	@Override
	public void hammer$setDamageMultiplier(float damageMultiplier) {
		hammer$damageMultiplier.set(damageMultiplier);
	}
	
	@Override
	public float hammer$getFallDamageMultiplier() {
		return hammer$fallDamageMultiplier.get();
	}
	
	@Override
	public void hammer$setFallDamageMultiplier(float fallDamageMultiplier) {
		hammer$fallDamageMultiplier.set(fallDamageMultiplier);
	}
	
	@Override
	public float hammer$getMovementSpeedMultiplier() {
		return hammer$movementSpeedMultiplier.get();
	}
	
	@Override
	public void hammer$setMovementSpeedMultiplier(float movementSpeedMultiplier) {
		hammer$movementSpeedMultiplier.set(movementSpeedMultiplier);
	}
	
	@Override
	public float hammer$getJumpMultiplier() {
		return hammer$jumpMultiplier.get();
	}
	
	@Override
	public void hammer$setJumpMultiplier(float jumpMultiplier) {
		hammer$jumpMultiplier.set(jumpMultiplier);
	}
	
	@Override
	public float hammer$getHealMultiplier() {
		return hammer$healMultiplier.get();
	}
	
	@Override
	public void hammer$setHealMultiplier(float healMultiplier) {
		hammer$healMultiplier.set(healMultiplier);
	}
	
	@Override
	public float hammer$getExhaustionMultiplier() {
		return hammer$exhaustionMultiplier.get();
	}
	
	@Override
	public void hammer$setExhaustionMultiplier(float exhaustionMultiplier) {
		hammer$exhaustionMultiplier.set(exhaustionMultiplier);
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
	private double hammer$attack$getAttributeValue(PlayerEntity instance, EntityAttribute entityAttribute) {
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
