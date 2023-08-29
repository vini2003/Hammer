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

import dev.vini2003.hammer.core.api.client.scissor.Scissors;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.provider.InvertProvider;
import dev.vini2003.hammer.gui.api.common.widget.provider.ScissorProvider;
import dev.vini2003.hammer.gui.api.common.widget.provider.SmoothProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class ImageBarWidget extends BarWidget implements SmoothProvider, ScissorProvider, InvertProvider {
	public static final Size STANDARD_VERTICAL_SIZE = new Size(24.0F, 48.0F);
	public static final Size STANDARD_HORIZONTAL_SIZE = new Size(48.0F, 24.0F);
	
	protected boolean smooth = false;
	protected boolean scissor = true;
	protected boolean invert = false;
	
	@Override
	public Size getStandardSize() {
		return vertical ? STANDARD_VERTICAL_SIZE : STANDARD_HORIZONTAL_SIZE;
	}
	
	@Override
	public void draw(DrawContext context, float tickDelta) {
		var matrices = context.getMatrices();
		var provider = context.getVertexConsumers();
	
		var foregroundWidth = getWidth() / getMaximum().getAsDouble() * getCurrent().getAsDouble();
		var foregroundHeight = getHeight() / getMaximum().getAsDouble() * getCurrent().getAsDouble();
		
		if (!smooth) {
			foregroundWidth = (int) foregroundWidth;
			foregroundHeight = (int) foregroundHeight;
		}
		
		var foregroundTexture = getForegroundTexture().get();
		var backgroundTexture = getBackgroundTexture().get();
		
		if (vertical) {
			backgroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			
			var scissors = (Scissors) null;
			
			if (scissor) {
				if (!invert) {
					scissors = new Scissors(getX(), (float) (getY() + (getHeight() - foregroundHeight)), getWidth(), (float) foregroundHeight, provider);
				} else {
					scissors = new Scissors(getX(), getY(), getWidth(), (float) foregroundHeight, provider);
				}
			}
			
			foregroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			
			if (scissor) {
				scissors.destroy();
			}
		}
		
		if (horizontal) {
			backgroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			
			var scissors = (Scissors) null;
			
			if (scissor) {
				if (!invert) {
					scissors = new Scissors(getX(), getY(), (float) foregroundWidth, getHeight(), provider);
				} else {
					scissors = new Scissors((float) (getX() + (getWidth() - foregroundWidth)), getY(), (float) foregroundWidth, getHeight(), provider);
				}
			}
			
			foregroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			
			if (scissor) {
				scissors.destroy();
			}
		}
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
	public boolean shouldScissor() {
		return scissor;
	}
	
	@Override
	public void setScissor(boolean scissor) {
		this.scissor = scissor;
	}
	
	@Override
	public boolean shouldInvert() {
		return invert;
	}
	
	@Override
	public void setInvert(boolean invert) {
		this.invert = invert;
	}
}
