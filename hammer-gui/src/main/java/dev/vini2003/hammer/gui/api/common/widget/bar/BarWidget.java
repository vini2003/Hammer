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
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.provider.*;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public abstract class BarWidget extends Widget implements BackgroundTextureProvider, ForegroundTextureProvider, HorizontalProvider, VerticalProvider, MaximumProvider, CurrentProvider {
	public static final Texture STANDARD_FOREGROUND_TEXTURE = new PartitionedTexture(HC.id("textures/widget/bar_foreground.png"), 18.0F, 18.0F, 0.055F, 0.055F, 0.055F, 0.055F);
	public static final Texture STANDARD_BACKGROUND_TEXTURE = new PartitionedTexture(HC.id("textures/widget/bar_background.png"), 18.0F, 18.0F, 0.055F, 0.055F, 0.055F, 0.055F);
	
	protected DoubleSupplier maximum = () -> 1.0D;
	protected DoubleSupplier current = () -> 0.5D;
	
	protected Supplier<Texture> foregroundTexture = () -> STANDARD_FOREGROUND_TEXTURE;
	protected Supplier<Texture> backgroundTexture = () -> STANDARD_BACKGROUND_TEXTURE;
	
	protected boolean horizontal = false;
	protected boolean vertical = true;
	
	protected BarWidget() {
		super();
		
		setTooltipSupplier(() -> {
			return ImmutableList.of(TextUtil.getPercentage(getCurrent().getAsDouble(), getMaximum().getAsDouble()));
		});
	}
	
	@Override
	public Supplier<Texture> getForegroundTexture() {
		return foregroundTexture;
	}
	
	@Override
	public void setForegroundTexture(Supplier<Texture> foregroundTexture) {
		this.foregroundTexture = foregroundTexture;
	}
	
	@Override
	public Supplier<Texture> getBackgroundTexture() {
		return backgroundTexture;
	}
	
	@Override
	public void setBackgroundTexture(Supplier<Texture> backgroundTexture) {
		this.backgroundTexture = backgroundTexture;
	}
	
	@Override
	public boolean isHorizontal() {
		return horizontal;
	}
	
	@Override
	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
		
		if (horizontal) {
			setVertical(false);
		}
	}
	
	@Override
	public boolean isVertical() {
		return vertical;
	}
	
	@Override
	public void setVertical(boolean vertical) {
		this.vertical = vertical;
		
		if (vertical) {
			setHorizontal(false);
		}
	}
	
	@Override
	public DoubleSupplier getMaximum() {
		return maximum;
	}
	
	@Override
	public void setMaximum(DoubleSupplier maximum) {
		this.maximum = maximum;
	}
	
	@Override
	public DoubleSupplier getCurrent() {
		return current;
	}
	
	@Override
	public void setCurrent(DoubleSupplier current) {
		this.current = current;
	}
}
