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
import dev.vini2003.hammer.core.api.client.texture.TiledSpriteTexture;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.provider.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.Sprite;

import java.util.function.Supplier;

public class SpriteBarWidget extends BarWidget implements ForegroundSpriteProvider, BackgroundSpriteProvider, SmoothProvider, InvertProvider, StepWidthProvider, StepHeightProvider {
	public static final Size STANDARD_VERTICAL_SIZE = new Size(24.0F, 48.0F);
	public static final Size STANDARD_HORIZONTAL_SIZE = new Size(48.0F, 24.0F);
	
	protected float stepWidth = -1.0F;
	protected float stepHeight = -1.0F;
	
	protected boolean smooth = false;
	protected boolean invert = false;
	
	@Override
	public Size getStandardSize() {
		return vertical ? STANDARD_VERTICAL_SIZE : STANDARD_HORIZONTAL_SIZE;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void draw(DrawContext context, float tickDelta) {
		var matrices = context.getMatrices();
		var provider = context.getVertexConsumers();
		
		var foregroundWidth = getWidth() / getMaximum().getAsDouble() * getCurrent().getAsDouble();
		var foregroundHeight = getHeight() / getMaximum().getAsDouble() * getCurrent().getAsDouble();
		
		if (stepWidth != 1.0F) {
			foregroundWidth -= foregroundWidth % stepWidth;
		}
		
		if (stepHeight != 1.0F) {
			foregroundHeight -= foregroundHeight % stepHeight;
		}
		
		if (!smooth) {
			foregroundWidth = (int) foregroundWidth;
			foregroundHeight = (int) foregroundHeight;
		}
		
		var foregroundTexture = getForegroundTexture().get();
		var backgroundTexture = getBackgroundTexture().get();
		
		if (vertical) {
			backgroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			
			var scissors = (Scissors) null;
			
			if (stepHeight == -1.0F) {
				if (!invert) {
					scissors = new Scissors(getX(), (float) (getY() + (getHeight() - foregroundHeight)), getWidth(), (float) foregroundHeight, provider);
				} else {
					scissors = new Scissors(getX(), getY(), getWidth(), (float) foregroundHeight, provider);
				}
			}
			
			if (!invert) {
				foregroundTexture.draw(matrices, provider, getX(), (float) (getY() + (getHeight() - foregroundHeight)), getWidth(), (float) foregroundHeight);
			} else {
				foregroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), (float) foregroundHeight);
			}
			
			if (stepHeight == -1.0F) {
				scissors.destroy();
			}
		}
		
		if (horizontal) {
			backgroundTexture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			
			var scissors = (Scissors) null;
			
			if (stepWidth == -1.0F) {
				if (!invert) {
					scissors = new Scissors(getX(), getY(), (float) foregroundWidth, getHeight(), provider);
				} else {
					scissors = new Scissors((float) (getX() + (getWidth() - foregroundWidth)), getY(), (float) foregroundWidth, getHeight(), provider);
				}
			}
			
			if (!invert) {
				foregroundTexture.draw(matrices, provider, getX(), getY(), (float) foregroundWidth, getHeight());
			} else {
				foregroundTexture.draw(matrices, provider, (float) (getX() + (getWidth() - foregroundWidth)), getY(), (float) foregroundWidth, getHeight());
			}
			
			if (stepWidth == -1.0F) {
				scissors.destroy();
			}
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public Supplier<Sprite> getForegroundSprite() {
		return null; // EnvType... we can't save it.
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void setForegroundSprite(Supplier<Sprite> foregroundSprite) {
		var foregroundTexture = new TiledSpriteTexture(foregroundSprite.get());
		this.foregroundTexture = () -> foregroundTexture;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public Supplier<Sprite> getBackgroundSprite() {
		return null; // EnvType... we can't save it.
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void setBackgroundSprite(Supplier<Sprite> backgroundSprite) {
		var backgroundTexture = new TiledSpriteTexture(backgroundSprite.get());
		this.backgroundTexture = () -> backgroundTexture;
	}
	
	@Override
	public void setStepWidth(float stepWidth) {
		this.stepWidth = stepWidth;
	}
	
	@Override
	public float getStepWidth() {
		return stepWidth;
	}
	
	@Override
	public void setStepHeight(float stepHeight) {
		this.stepHeight = stepHeight;
	}
	
	@Override
	public float getStepHeight() {
		return stepHeight;
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
	public boolean shouldInvert() {
		return invert;
	}
	
	@Override
	public void setInvert(boolean invert) {
		this.invert = invert;
	}
}
