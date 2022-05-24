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

package dev.vini2003.hammer.border.mixin.common;

import dev.vini2003.hammer.border.impl.common.border.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.WorldBorderListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(WorldBorder.class)
public abstract class WorldBorderMixin implements CubicWorldBorder {
	@Shadow
	private double centerX;
	@Shadow
	private double centerZ;
	
	private double centerY;
	
	@Shadow
	protected abstract List<WorldBorderListener> getListeners();
	
	@Shadow
	private int maxRadius;
	
	@Shadow
	public abstract double getBoundWest();
	
	@Shadow
	public abstract double getBoundEast();
	
	@Shadow
	public abstract double getBoundNorth();
	
	@Shadow
	public abstract double getBoundSouth();
	
	@Shadow
	public abstract double getDistanceInsideBorder(Entity entity);
	
	private CubicWorldBorderArea hammer$area = new CubicWorldBorderStaticArea((WorldBorder) (Object) this, 5.9999968E7);
	
	@Inject(at = @At("HEAD"), method = "getBoundWest", cancellable = true)
	private void hammer$getBoundWest(CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(hammer$area.getBoundWest());
	}
	
	@Inject(at = @At("HEAD"), method = "getBoundEast", cancellable = true)
	private void hammer$getBoundEast(CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(hammer$area.getBoundEast());
	}
	
	@Inject(at = @At("HEAD"), method = "getBoundNorth", cancellable = true)
	private void hammer$getBoundNorth(CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(hammer$area.getBoundNorth());
	}
	
	@Inject(at = @At("HEAD"), method = "getBoundSouth", cancellable = true)
	private void hammer$getBoundSouth(CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(hammer$area.getBoundSouth());
	}
	
	@Override
	public double getBoundUp() {
		return hammer$area.getBoundUp();
	}
	
	@Override
	public double getBoundDown() {
		return hammer$area.getBoundDown();
	}
	
	@Override
	public double getCenterY() {
		return centerY;
	}
	
	@Inject(at = @At("HEAD"), method = "contains(Lnet/minecraft/util/math/BlockPos;)Z", cancellable = true)
	private void hammer$contains$1(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(pos.getX() > getBoundWest() - 1
				&& pos.getX() < getBoundEast()
				&& pos.getY() < getBoundUp()
				&& pos.getY() > getBoundDown()
				&& pos.getZ() > getBoundNorth() - 1
				&& pos.getZ() < getBoundSouth());
	}
	
	@Inject(at = @At("HEAD"), method = "contains(Lnet/minecraft/util/math/Box;)Z", cancellable = true)
	private void hammer$contains$2(Box box, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(box.maxX > getBoundWest()
				&& box.minX < getBoundEast()
				&& box.maxY < getBoundUp()
				&& box.minY > getBoundDown()
				&& box.maxZ > getBoundNorth()
				&& box.minZ < getBoundSouth());
	}
	
	@Override
	public boolean contains(double x, double y, double z) {
		return x > getBoundWest() - 1
				&& x < getBoundEast()
				&& y < getBoundUp()
				&& y > getBoundDown()
				&& z > getBoundNorth() - 1
				&& z < getBoundSouth();
	}
	
	@Override
	public boolean contains(double x, double y, double z, double margin) {
		var a = x > getBoundWest() - 1 - margin;
		var b = x < getBoundEast() + margin;
		var c = y < getBoundUp() + margin;
		var d = y > getBoundDown() - margin;
		var e = z > getBoundNorth() - 1 - margin;
		var f = z < getBoundSouth() + margin;
		;
		
		return a && b && c && d && e && f;
	}
	
	@Inject(at = @At("HEAD"), method = "clamp", cancellable = true)
	private void hammer$clamp(double x, double y, double z, CallbackInfoReturnable<BlockPos> cir) {
		cir.setReturnValue(new BlockPos(MathHelper.clamp(x, getBoundWest(), getBoundEast()), MathHelper.clamp(y, getBoundDown(), getBoundUp()), MathHelper.clamp(z, getBoundNorth(), getBoundSouth())));
	}
	
	@Inject(at = @At("HEAD"), method = "asVoxelShape", cancellable = true)
	private void hammer$asVoxelShape(CallbackInfoReturnable<VoxelShape> cir) {
		cir.setReturnValue(hammer$area.asVoxelShape());
	}
	
	@Inject(at = @At("HEAD"), method = "getDistanceInsideBorder(Lnet/minecraft/entity/Entity;)D", cancellable = true)
	private void hammer$getDistanceInsideBorder(Entity entity, CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(getDistanceInsideBorder(entity.getX(), entity.getY(), entity.getZ()));
	}
	
	@Override
	public double getDistanceInsideBorder(double x, double y, double z) {
		var distNorth = z - getBoundNorth();
		var distSouth = getBoundSouth() - z;
		var distUp = getBoundUp() - y;
		var distDown = y - getBoundDown();
		var distWest = x - getBoundWest();
		var distEast = getBoundEast() - x;
		
		return Math.min(distNorth, Math.min(distSouth, Math.min(distUp, Math.min(distDown, Math.min(distWest, distEast)))));
	}
	
	@Inject(at = @At("HEAD"), method = "canCollide", cancellable = true)
	private void hammer$canCollide(Entity entity, Box box, CallbackInfoReturnable<Boolean> cir) {
		var length = Math.max(MathHelper.absMax(box.getXLength(), MathHelper.absMax(box.getYLength(), box.getZLength())), 1.0);
		
		cir.setReturnValue(getDistanceInsideBorder(entity) < length * 2.0 && contains(entity.getX(), entity.getY(), entity.getZ(), length));
	}
	
	@Inject(at = @At("HEAD"), method = "setCenter", cancellable = true)
	private void hammer$setCenter(double x, double z, CallbackInfo ci) {
		centerX = x;
		centerZ = z;
		
		hammer$area.onCenterChanged();
		
		for (var listener : getListeners()) {
			listener.onCenterChanged((WorldBorder) (Object) this, x, z);
		}
		
		ci.cancel();
	}
	
	@Override
	public void setCenter(double x, double y, double z) {
		centerX = x;
		centerY = y;
		centerZ = z;
		
		hammer$area.onCenterChanged();
		
		for (var listener : getListeners()) {
			listener.onCenterChanged((WorldBorder) (Object) this, x, z);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "getSize", cancellable = true)
	private void hammer$getSize(CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(hammer$area.getSize());
	}
	
	@Inject(at = @At("HEAD"), method = "getSizeLerpTime", cancellable = true)
	private void hammer$getSizeLerpTime(CallbackInfoReturnable<Long> cir) {
		cir.setReturnValue(hammer$area.getSizeLerpTime());
	}
	
	@Inject(at = @At("HEAD"), method = "getSizeLerpTarget", cancellable = true)
	private void hammer$getSizeLerpTarget(CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(hammer$area.getSizeLerpTarget());
	}
	
	@Inject(at = @At("HEAD"), method = "setSize", cancellable = true)
	private void hammer$setSize(double size, CallbackInfo ci) {
		hammer$area = new CubicWorldBorderStaticArea((WorldBorder) (Object) this, size);
		
		for (var listener : this.getListeners()) {
			listener.onSizeChange((WorldBorder) (Object) this, size);
		}
		
		ci.cancel();
	}
	
	@Inject(at = @At("HEAD"), method = "interpolateSize", cancellable = true)
	private void hammer$interpolateSize(double fromSize, double toSize, long time, CallbackInfo ci) {
		this.hammer$area = (fromSize == toSize ? new CubicWorldBorderStaticArea((WorldBorder) (Object) this, toSize) : new CubicWorldBorderMovingArea((WorldBorder) (Object) this, fromSize, toSize, time));
		
		for (var listener : getListeners()) {
			listener.onInterpolateSize((WorldBorder) (Object) this, fromSize, toSize, time);
		}
		
		ci.cancel();
	}
	
	@Inject(at = @At("HEAD"), method = "setMaxRadius", cancellable = true)
	private void hammer$setMaxRadius(int maxRadius, CallbackInfo ci) {
		this.maxRadius = maxRadius;
		
		hammer$area.onMaxRadiusChanged();
		
		ci.cancel();
	}
	
	@Inject(at = @At("HEAD"), method = "getShrinkingSpeed", cancellable = true)
	private void hammer$getShrinkingSpeed(CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(hammer$area.getShrinkingSpeed());
	}
	
	@Inject(at = @At("HEAD"), method = "tick", cancellable = true)
	private void hammer$tick(CallbackInfo ci) {
		hammer$area = hammer$area.getAreaInstance();
		
		ci.cancel();
	}
	
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorder;setCenter(DD)V"), method = "load")
	private void hammer$load_setCenter(WorldBorder instance, double x, double z) {
		instance.setCenter(x, z);
	}
	
	@Inject(at = @At("HEAD"), method = "load")
	private void hammer$load(WorldBorder.Properties properties, CallbackInfo ci) {
		var cubicProperties = (CubicWorldBorderProperties) properties;
		
		setCenter(properties.getCenterX(), cubicProperties.getCenterY(), properties.getCenterZ());
	}
}
