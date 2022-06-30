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

import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.minecraft.client.gui.DrawableHelper.drawCenteredText;

@Mixin(VideoOptionsScreen.class)
public abstract class VideoOptionsScreenMixin extends GameOptionsScreen {
	@Shadow
	@Final
	private static Option[] OPTIONS;
	
	private static final Text HAMMER$FAST_GRAPHICS_TOOLTIP = new TranslatableText("options.graphics.fast.tooltip");
	private static final Text HAMMER$FANCY_GRAPHICS_TOOLTIP = new TranslatableText("options.graphics.fancy.tooltip");;
	
	private static final Option HAMMER$GRAPHICS = CyclingOption.create("options.graphics", Stream.of(GraphicsMode.values()).filter(graphicsMode -> graphicsMode != GraphicsMode.FABULOUS).collect(Collectors.toList()),
	mode -> {
		var text = new TranslatableText(mode.getTranslationKey());
		
		if (mode == GraphicsMode.FABULOUS) {
			return text.formatted(Formatting.ITALIC);
		}
		
		return text;
	}, gameOptions -> gameOptions.graphicsMode, (gameOptions, option, mode) -> {
		var client = MinecraftClient.getInstance();
		var manager = client.getVideoWarningManager();
		
		if (mode == GraphicsMode.FABULOUS && manager.canWarn()) {
			manager.scheduleWarning();
			
			return;
		}
		
		gameOptions.graphicsMode = mode;
		
		client.worldRenderer.reload();
	}).tooltip(client -> {
		return graphicsMode -> {
			switch (graphicsMode) {
				case FAST: {
					return client.textRenderer.wrapLines(HAMMER$FAST_GRAPHICS_TOOLTIP, 200);
				}
				
				case FANCY: {
					return client.textRenderer.wrapLines(HAMMER$FANCY_GRAPHICS_TOOLTIP, 200);
				}
			}
			
			return ImmutableList.of();
		};
	});
	
	public VideoOptionsScreenMixin(Screen screen, GameOptions gameOptions, Text text) {
		super(screen, gameOptions, text);
	}
	
	@Inject(at = @At("TAIL"), method = "<clinit>")
	private static void hammer$init(CallbackInfo ci) {
		OPTIONS[0] = HAMMER$GRAPHICS;
	}
	
	@Inject(at = @At("RETURN"), method = "render")
	private void hammer$render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		drawCenteredText(matrices, this.textRenderer, new TranslatableText("text.hammer.fabulous_graphics_warning"), this.width / 2, 5 + 9 + 5, 0xFF5370);
	}
}
