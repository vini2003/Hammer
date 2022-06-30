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

import dev.vini2003.hammer.core.api.common.util.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
	@Shadow
	private Vec3d velocity;
	
	@Inject(at = @At("HEAD"), method = "setVelocity(Lnet/minecraft/util/math/Vec3d;)V", cancellable = true)
	private void hammer$setVelocity(Vec3d velocity, CallbackInfo ci) {
		if ((Object) this instanceof PlayerEntity player && PlayerUtil.isFrozen(player)) {
			this.velocity = Vec3d.ZERO;
			
			ci.cancel();
		}
	}
	
	@Inject(at = @At("HEAD"), method = "addVelocity", cancellable = true)
	private void hammer$addVelocity(double deltaX, double deltaY, double deltaZ, CallbackInfo ci) {
		if ((Object) this instanceof PlayerEntity player && PlayerUtil.isFrozen(player)) {
			this.velocity = Vec3d.ZERO;
			
			ci.cancel();
		}
	}
}
