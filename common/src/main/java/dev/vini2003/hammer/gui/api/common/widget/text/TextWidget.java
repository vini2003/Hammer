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

package dev.vini2003.hammer.gui.api.common.widget.text;

import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.provider.ColorProvider;
import dev.vini2003.hammer.gui.api.common.widget.provider.ShadowProvider;
import dev.vini2003.hammer.gui.api.common.widget.provider.TextProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.function.Supplier;

/**
 * <b>This widget is client-side only.</b>
 */
public class TextWidget extends Widget implements TextProvider, ShadowProvider, ColorProvider {
	protected Supplier<Text> text = () -> null;
	
	protected boolean shadow = false;
	
	protected Color color = new Color(0x404040FFL);
	
	@Override
	public Size getStandardSize() {
		if (text.get() != null) {
			return Size.of(TextUtil.getWidth(text.get()), TextUtil.getHeight(text.get()));
		} else {
			return Size.of(8.0F, TextUtil.getHeight(text.get()));
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		onBeginDraw(matrices, provider, tickDelta);
		
		var textRenderer = DrawingUtil.getTextRenderer();
		
		if (text.get() != null) {
			if (shadow) {
				textRenderer.drawWithShadow(matrices, text.get(), getX(), getY(), color.toRgb());
			} else {
				textRenderer.draw(matrices, text.get(), getX(), getY(), color.toRgb());
			}
		}
		
		onEndDraw(matrices, provider, tickDelta);
	}
	
	@Override
	public boolean hasShadow() {
		return false;
	}
	
	@Override
	public void setShadow(boolean shadow) {
		this.shadow = shadow;
	}
	
	public Color getColor() {
		return color;
	}
	
	@Override
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public Supplier<Text> getText() {
		return text;
	}
	
	@Override
	public void setText(Supplier<Text> text) {
		this.text = text;
	}
}
