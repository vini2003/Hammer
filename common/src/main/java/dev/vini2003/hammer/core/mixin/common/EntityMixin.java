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
import dev.vini2003.hammer.core.impl.common.accessor.EntityAccessor;
import dev.vini2003.hammer.core.impl.common.component.container.ComponentContainer;
import dev.vini2003.hammer.core.impl.common.component.holder.ComponentHolder;
import dev.vini2003.hammer.core.impl.common.util.ComponentUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements ComponentHolder, EntityAccessor {
	@Shadow
	private Vec3d velocity;
	
	private Entity hammer$self() {
		return (Entity) (Object) this;
	}
	
	@Override
	public <T> TrackedDataHandler<T> hammer$registerTrackedData(T defaultValue, String id) {
		return TrackedDataComponent.get(this).register((Class<T>) defaultValue.getClass(), defaultValue, id);
	}
	
	@Inject(at = @At("HEAD"), method = "setVelocity(Lnet/minecraft/util/math/Vec3d;)V", cancellable = true)
	private void hammer$setVelocity(Vec3d velocity, CallbackInfo ci) {
		if (hammer$self() instanceof PlayerEntity player && player.hammer$isFrozen()) {
			this.velocity = Vec3d.ZERO;
			
			ci.cancel();
		}
	}
	
	@Inject(at = @At("HEAD"), method = "addVelocity", cancellable = true)
	private void hammer$addVelocity(double deltaX, double deltaY, double deltaZ, CallbackInfo ci) {
		if (hammer$self() instanceof PlayerEntity player && player.hammer$isFrozen()) {
			this.velocity = Vec3d.ZERO;
			
			ci.cancel();
		}
	}
	
	private ComponentContainer hammer$componentContainer = new ComponentContainer();
	
	@Inject(at = @At("RETURN"), method = "<init>")
	private void hammer$init(EntityType<?> entityType, World world, CallbackInfo ci) {
		ComponentUtil.attachToEntity(hammer$self());
	}
	
	@Inject(at = @At("HEAD"), method = "copyFrom")
	private void hammer$copyFrom(Entity original, CallbackInfo ci) {
		if (original instanceof ComponentHolder originalHolder) {
			hammer$componentContainer = originalHolder.getComponentContainer();
		}
	}
	
	@Inject(method = "readNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
	public void hammer$readNbt$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		hammer$componentContainer.readFromNbt(nbt);
	}
	
	@Inject(method = "writeNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
	public void hammer$writeNbt$writeCustomDataToNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
		hammer$componentContainer.writeToNbt(nbt);
	}
	
	@Override
	public ComponentContainer getComponentContainer() {
		return hammer$componentContainer;
	}
}
