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

import com.mojang.serialization.DynamicLike;
import dev.vini2003.hammer.border.impl.common.border.CubicWorldBorder;
import dev.vini2003.hammer.border.impl.common.border.CubicWorldBorderProperties;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldBorder.Properties.class)
public class WorldBorderPropertiesMixin implements CubicWorldBorderProperties {
	private double centerY;
	
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/world/border/WorldBorder;)V")
	private void hammer$init(WorldBorder worldBorder, CallbackInfo ci) {
		var cubicWorldBorder = (CubicWorldBorder) worldBorder;
		
		centerY = cubicWorldBorder.getCenterY();
	}
	
	@Inject(at = @At("RETURN"), method = "fromDynamic", cancellable = true)
	private static void hammer$fromDynamic(DynamicLike<?> dynamic, WorldBorder.Properties properties, CallbackInfoReturnable<WorldBorder.Properties> cir) {
		var cubicProperties = (CubicWorldBorderProperties) properties;
		
		var returnProperties = cir.getReturnValue();
		
		var cubicReturnProperties = (CubicWorldBorderProperties) returnProperties;
		cubicReturnProperties.setCenterY(dynamic.get("BorderCenterY").asDouble(cubicProperties.getCenterY()));
		
		cir.setReturnValue((WorldBorder.Properties) cubicReturnProperties);
	}
	
	@Inject(at = @At("RETURN"), method = "writeNbt")
	private void hammer$writeNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putDouble("BorderCenterY", centerY);
	}
	
	@Override
	public double getCenterY() {
		return centerY;
	}
	
	@Override
	public void setCenterY(double centerY) {
		this.centerY = centerY;
	}
}
