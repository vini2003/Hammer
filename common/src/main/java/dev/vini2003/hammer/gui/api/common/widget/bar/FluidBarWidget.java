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

package dev.vini2003.hammer.gui.api.common.widget.bar;

import com.google.common.collect.ImmutableList;
import dev.architectury.fluid.FluidStack;
import dev.vini2003.hammer.core.api.client.scissor.Scissors;
import dev.vini2003.hammer.core.api.client.texture.TiledFluidTexture;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.core.api.common.util.FluidTextUtil;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import dev.vini2003.hammer.gui.api.common.widget.provider.SmoothProvider;
import dev.vini2003.hammer.gui.api.common.widget.provider.TiledProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class FluidBarWidget extends BarWidget implements SmoothProvider, TiledProvider {
	public static final Size STANDARD_VERTICAL_SIZE = Size.of(24.0F, 48.0F);
	public static final Size STANDARD_HORIZONTAL_SIZE = Size.of(48.0F, 24.0F);
	
	protected Supplier<FluidStack> stack = () -> null;
	protected Supplier<Long> maximum = () -> 81_000L;
	
	protected boolean smooth = false;
	protected boolean tiled = true;
	
	public FluidBarWidget() {
		super();
		
		setTooltip(() -> {
			var stack = this.stack.get();
			
			if (stack != null) {
				if (Screen.hasShiftDown()) {
					return FluidTextUtil.getDetailedStorageTooltips(stack.getFluid(), stack.getAmount(), maximum.get()).stream().map(Text::asOrderedText).toList();
				} else {
					return FluidTextUtil.getTooltip(stack.getFluid(), stack.getAmount(), maximum.get()).stream().map(Text::asOrderedText).toList();
				}
			} else {
				return ImmutableList.of(TextUtil.getPercentage(getCurrent().getAsDouble(), getMaximum().getAsDouble()).asOrderedText());
			}
		});
	}
	
	@Override
	public Size getStandardSize() {
		return vertical ? STANDARD_VERTICAL_SIZE : STANDARD_HORIZONTAL_SIZE;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		onBeginDraw(matrices, provider, tickDelta);
		
		var foregroundWidth = (getWidth() / getMaximum().getAsDouble() * getCurrent().getAsDouble());
		var foregroundHeight = (getHeight() / getMaximum().getAsDouble() * getCurrent().getAsDouble());
		
		if (!smooth) {
			foregroundWidth = (int) foregroundWidth;
			foregroundHeight = (int) foregroundHeight;
		}
		
		var foregroundTexture = getForegroundTexture().get();
		var backgroundTexture = getBackgroundTexture().get();
		
		if (vertical) {
			backgroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			
			var scissors = new Scissors(getX(), (float) (getY() + (getHeight() - foregroundHeight)), getWidth(), (float) foregroundHeight, provider);
			
			foregroundTexture.draw(matrices, provider, getX() + 1.0F, getY() + 1.0F, getWidth() - 2.0F, getHeight() - 2.0F);
			
			scissors.destroy();
		}
		
		if (horizontal) {
			backgroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			
			var scissors = new Scissors(getX(), getY(), (float) foregroundWidth, getHeight(), provider);
			
			foregroundTexture.draw(matrices, provider, getX() + 1.0F, getY() + 1.0F, getWidth() - 2.0F, getHeight() - 2.0F);
			
			scissors.destroy();
		}
		
		onEndDraw(matrices, provider, tickDelta);
	}
	
	public void setStack(Supplier<FluidStack> stackSuppleir) {
		if (stackSuppleir.get() != null) {
			this.stack = stackSuppleir;
			
			this.current = () -> (float) stackSuppleir.get().getAmount();
			
			this.foregroundTexture = () -> new TiledFluidTexture(stackSuppleir.get().getFluid());
			this.backgroundTexture = () -> STANDARD_BACKGROUND_TEXTURE;
		}
	}
	
	public void setStack(FluidStack stack) {
		setStack(() -> stack);
	}
	
	@Override
	public boolean isSmooth() {
		return smooth;
	}
	
	@Override
	public void setSmooth(boolean smooth) {
		this.smooth = smooth;
	}
	
	@Override
	public boolean isTiled() {
		return tiled;
	}
	
	@Override
	public void setTiled(boolean tiled) {
		this.tiled = tiled;
	}
}
