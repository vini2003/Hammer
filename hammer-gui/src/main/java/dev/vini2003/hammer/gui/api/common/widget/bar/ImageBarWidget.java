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
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class ImageBarWidget extends BarWidget {
	protected DoubleSupplier maximum = () -> 1.0D;
	protected DoubleSupplier current = () -> 0.5D;
	
	protected Supplier<Texture> foregroundTexture = () -> STANDARD_FOREGROUND_TEXTURE;
	protected Supplier<Texture> backgroundTexture = () -> STANDARD_BACKGROUND_TEXTURE;
	
	protected boolean smooth = false;
	protected boolean scissor = true;
	protected boolean invert = false;
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		var foregroundWidth = getWidth() / getMaximum() * getCurrent();
		var foregroundHeight = getHeight() / getMaximum() * getCurrent();
		
		if (!smooth) {
			foregroundWidth = (int) foregroundWidth;
			foregroundHeight = (int) foregroundHeight;
		}
		
		var foregroundTexture = getForegroundTexture();
		var backgroundTexture = getBackgroundTexture();
		
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
	public double getMaximum() {
		return maximum.getAsDouble();
	}
	
	@Override
	public double getCurrent() {
		return current.getAsDouble();
	}
	
	@Override
	public Texture getForegroundTexture() {
		return foregroundTexture.get();
	}
	
	@Override
	public Texture getBackgroundTexture() {
		return backgroundTexture.get();
	}
	
	public void setMaximum(DoubleSupplier maximumSupplier) {
		this.maximum = maximumSupplier;
	}
	
	public void setMaximum(double maximum) {
		setMaximum(() -> maximum);
	}
	
	public void setCurrent(DoubleSupplier currentSupplier) {
		this.current = currentSupplier;
	}
	
	public void setCurrent(double current) {
		setCurrent(() -> current);
	}
	
	public void setForegroundTexture(Supplier<Texture> foregroundTextureSupplier) {
		this.foregroundTexture = foregroundTextureSupplier;
	}
	
	public void setForegroundTexture(Texture foregroundTexture) {
		setForegroundTexture(() -> foregroundTexture);
	}
	
	public void setBackgroundTexture(Supplier<Texture> backgroundTextureSupplier) {
		this.backgroundTexture = backgroundTextureSupplier;
	}
	
	public void setBackgroundTexture(Texture backgroundTexture) {
		setBackgroundTexture(() -> backgroundTexture);
	}
	
	public void setSmooth(boolean smooth) {
		this.smooth = smooth;
	}
	
	public void setScissor(boolean scissor) {
		this.scissor = scissor;
	}
	
	public void setInvert(boolean invert) {
		this.invert = invert;
	}
}
