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

import com.mojang.blaze3d.systems.RenderSystem;
import dev.vini2003.hammer.preset.HP;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.MacWindowUtil;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.net.URL;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow
	@Final
	private Window window;
	
	@Inject(at = @At("RETURN"), method = "getWindowTitle", cancellable = true)
	private void hammer$getWindowTitle(CallbackInfoReturnable<String> cir) {
		if (!HP.CONFIG.windowName.isEmpty()) {
			cir.setReturnValue(HP.CONFIG.windowName + " - " + cir.getReturnValue());
		}
	}


	@Inject(at = @At("RETURN"), method = "<init>")
	private void hammer$init(RunArgs args, CallbackInfo ci) {
		if (!HP.CONFIG.windowIcon.isEmpty()) {
			try {
				hammer$setIconFromUrl(HP.CONFIG.windowIcon);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void hammer$setIconFromUrl(String imageUrl) throws IOException {
		RenderSystem.assertInInitPhase();
		
		var url = new URL(imageUrl);
		
		try (var is = url.openStream()) {
			
			try (var nativeImage = NativeImage.read(is)) {
				if (MinecraftClient.IS_SYSTEM_MAC) {
					MacWindowUtil.setApplicationIconImage(is);
				} else {
					try (var memoryStack = MemoryStack.stackPush()) {
						var buffer = GLFWImage.malloc(1, memoryStack);
						
						var byteBuffer = MemoryUtil.memAlloc(nativeImage.getWidth() * nativeImage.getHeight() * 4);
						byteBuffer.asIntBuffer().put(nativeImage.makePixelArray());
						
						buffer.position(0);
						buffer.width(nativeImage.getWidth());
						buffer.height(nativeImage.getHeight());
						buffer.pixels(byteBuffer);
						
						GLFW.glfwSetWindowIcon(this.window.getHandle(), buffer);
						
						MemoryUtil.memFree(byteBuffer);
					}
				}
			}
		}
	}
	
}
