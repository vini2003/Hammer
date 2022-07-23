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

package dev.vini2003.hammer.preset.mixin.client;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.preset.HP;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Inject(at = @At("RETURN"), method = "getWindowTitle", cancellable = true)
	private void hidenseek$getWindowTitle(CallbackInfoReturnable<String> cir) {
		if (!HP.CONFIG.windowName.isEmpty()) {
			cir.setReturnValue(HP.CONFIG.windowName + " - " + cir.getReturnValue());
		}
	}
	
	@ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setIcon(Ljava/io/InputStream;Ljava/io/InputStream;)V"), method = "<init>")
	private void hidenseek$init_setIcon(Args args) {
		args.set(0, HC.class.getResourceAsStream(HP.CONFIG.iconLowResPath));
		args.set(1, HC.class.getResourceAsStream(HP.CONFIG.iconHighResPath));
	}
}
