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
import dev.vini2003.hammer.core.impl.common.util.HeightUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Map;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccessor {
	@Shadow
	protected HungerManager hungerManager;
	
	@Shadow
	@Final
	private PlayerAbilities abilities;
	
	@Shadow
	@Final
	private static Map<EntityPose, EntityDimensions> POSE_DIMENSIONS;
	private final TrackedDataHandler<Boolean> hammer$frozen = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, false, "Frozen");
	
	private final TrackedDataHandler<Float> hammer$attackDamageMultiplier = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Float.class, 1.0F,"AttackDamageMultiplier");
	private final TrackedDataHandler<Float> hammer$damageMultiplier = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Float.class, 1.0F, "DamageMultiplier");
	private final TrackedDataHandler<Float> hammer$fallDamageMultiplier = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Float.class, 1.0F, "FallDamageMultiplier");
	private final TrackedDataHandler<Float> hammer$movementSpeedMultiplier = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Float.class, 1.0F, "MovementSpeedMultiplier");
	private final TrackedDataHandler<Float> hammer$jumpMultiplier = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Float.class, 1.0F, "JumpMultiplier");
	private final TrackedDataHandler<Float> hammer$healMultiplier = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Float.class, 1.0F, "HealMultiplier");
	private final TrackedDataHandler<Float> hammer$exhaustionMultiplier = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Float.class, 1.0F, "ExhaustionMultiplier");
	
	private final TrackedDataHandler<Boolean> hammer$head = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, true, "Head");
	private final TrackedDataHandler<Boolean> hammer$torso = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, true, "Torso");
	private final TrackedDataHandler<Boolean> hammer$leftArm = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, true, "LeftArm");
	private final TrackedDataHandler<Boolean> hammer$rightArm = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, true, "RightArm");
	private final TrackedDataHandler<Boolean> hammer$leftLeg = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, true, "LeftLeg");
	private final TrackedDataHandler<Boolean> hammer$rightLeg = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, true, "RightLeg");
	
	private final TrackedDataHandler<Boolean> hammer$allowMovement = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, true, "AllowMovement");
	private final TrackedDataHandler<Boolean> hammer$allowInteraction = new TrackedDataHandler<>(() -> TrackedDataComponent.get(this), Boolean.class, true, "AllowInteraction");

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	private PlayerEntity hammer$self() {
		return (PlayerEntity) (Object) this;
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
	
	@Override
	public boolean hammer$hasHead() {
		return hammer$head.get();
	}
	
	@Override
	public void hammer$setHead(boolean head) {
		hammer$head.set(head);
		hammer$updateBoundingBox();
	}
	
	@Override
	public boolean hammer$hasTorso() {
		return hammer$torso.get();
	}
	
	@Override
	public void hammer$setTorso(boolean torso) {
		hammer$torso.set(torso);
		hammer$updateBoundingBox();
	}
	
	@Override
	public boolean hammer$hasLeftArm() {
		return hammer$leftArm.get();
	}
	
	@Override
	public void hammer$setLeftArm(boolean leftArm) {
		hammer$leftArm.set(leftArm);
		hammer$updateBoundingBox();
	}
	
	@Override
	public boolean hammer$hasRightArm() {
		return hammer$rightArm.get();
	}
	
	@Override
	public void hammer$setRightArm(boolean rightArm) {
		hammer$rightArm.set(rightArm);
		hammer$updateBoundingBox();
	}
	
	@Override
	public boolean hammer$hasLeftLeg() {
		return hammer$leftLeg.get();
	}
	
	@Override
	public void hammer$setLeftLeg(boolean leftLeg) {
		hammer$leftLeg.set(leftLeg);
		hammer$updateBoundingBox();
	}
	
	@Override
	public boolean hammer$hasRightLeg() {
		return hammer$rightLeg.get();
	}
	
	@Override
	public void hammer$setRightLeg(boolean rightLeg) {
		hammer$rightLeg.set(rightLeg);
		hammer$updateBoundingBox();
	}
	
	@Override
	public boolean hammer$allowMovement() {
		return hammer$allowMovement.get();
	}
	
	@Override
	public void hammer$setAllowMovement(boolean allowMovement) {
		hammer$allowMovement.set(allowMovement);
	}
	
	@Override
	public boolean hammer$allowInteraction() {
		return hammer$allowInteraction.get();
	}
	
	@Override
	public void hammer$setAllowInteraction(boolean allowInteraction) {
		hammer$allowInteraction.set(allowInteraction);
	}
	
	@Inject(at = @At("RETURN"), method = "getDimensions", cancellable = true)
	private void hammer$getDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> cir) {
		if (pose == EntityPose.STANDING || pose == EntityPose.CROUCHING) {
			var dimensions = POSE_DIMENSIONS.get(pose);
			
			cir.setReturnValue(EntityDimensions.changing(dimensions.width, HeightUtil.getHeight(hammer$self(), dimensions)));
			cir.cancel();
		}
	}
	
	@Inject(at = @At("RETURN"), method = "getActiveEyeHeight", cancellable = true)
	private void hammer$getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions, CallbackInfoReturnable<Float> cir) {
		if (pose == EntityPose.STANDING || pose == EntityPose.CROUCHING) {
			cir.setReturnValue(dimensions.height * 0.85F);
			cir.cancel();
		}
	}
	
	@Inject(at = @At("RETURN"), method = "getMovementSpeed", cancellable = true)
	private void hammer$getMovementSpeed(CallbackInfoReturnable<Float> cir) {
		if (!hammer$hasHead() && !hammer$hasTorso() && !hammer$hasLeftArm() && !hammer$hasRightArm()) {
			var multiplier = 1.0F;
			
			if (hammer$hasLeftLeg()) multiplier += 0.125;
			if (hammer$hasRightLeg()) multiplier += 0.125F;
			
			cir.setReturnValue(cir.getReturnValueF() * multiplier);
			cir.cancel();
		} else if (!hammer$hasHead() && !hammer$hasTorso() && !hammer$hasLeftLeg() && !hammer$hasRightLeg()) {
			var multiplier = 1F;
			
			if (hammer$hasLeftArm()) multiplier -= 0.125F;
			if (hammer$hasRightArm()) multiplier -= 0.125F;
			
			cir.setReturnValue(cir.getReturnValueF() * multiplier);
			cir.cancel();
		}
		
		cir.setReturnValue(cir.getReturnValueF() * hammer$getMovementSpeedMultiplier());
		cir.cancel();
	}
	
	@Inject(at = @At("RETURN"), method = "isBlockBreakingRestricted", cancellable = true)
	private void hammer$isBlockBreakingRestricted(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
		if (hammer$isFrozen()) {
			cir.setReturnValue(true);
			cir.cancel();
		}
		
		if (!hammer$hasRightArm()) {
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
			if (!getWorld().isClient()) {
				hungerManager.addExhaustion(exhaustion * hammer$getExhaustionMultiplier());
				
				ci.cancel();
			}
		}
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
	
	private void hammer$updateBoundingBox() {
		var self = hammer$self();
		self.calculateDimensions();
		self.updatePosition(self.getX(), self.getY(), self.getZ());
	}
}
