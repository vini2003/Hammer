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

package dev.vini2003.hammer.permission.mixin.common;

import dev.vini2003.hammer.permission.api.common.manager.RoleManager;
import dev.vini2003.hammer.permission.impl.common.accessor.PlayerEntityAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccessor {
	private static final TrackedData<Boolean> HAMMER$ROLE_OUTLINE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(at = @At("HEAD"), method = "initDataTracker")
	private void hammer$initDataTracker(CallbackInfo ci) {
		dataTracker.startTracking(HAMMER$ROLE_OUTLINE, false);
	}
	
	@Inject(at = @At("HEAD"), method = "writeCustomDataToNbt")
	private void hammer$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putBoolean("Hammer$RoleOutline", dataTracker.get(HAMMER$ROLE_OUTLINE));
	}
	
	@Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
	private void hammer$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		if (nbt.contains("Hammer$RoleOutline")) {
			dataTracker.set(HAMMER$ROLE_OUTLINE, nbt.getBoolean("Hammer$RoleOutline"));
		}
	}
	
	@Inject(at = @At("RETURN"), method = "getDisplayName", cancellable = true)
	private void hammer$getDisplayName(CallbackInfoReturnable<Text> cir) {
		var color = RoleManager.getRolePrefixColor((PlayerEntity) (Object) this);
		
		if (color != null) {
			var formatted = ((MutableText) cir.getReturnValue()).styled((style) -> style.withColor(color));
			
			cir.setReturnValue(formatted);
		}
	}
	
	@Override
	public void hammer$setRoleOutline(boolean roleOutline) {
		dataTracker.set(HAMMER$ROLE_OUTLINE, roleOutline);
	}
	
	@Override
	public boolean hammer$hasRoleOutline() {
		return dataTracker.get(HAMMER$ROLE_OUTLINE);
	}
}
