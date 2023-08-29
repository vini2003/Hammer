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
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class TextWidget extends Widget implements TextProvider, ShadowProvider, ColorProvider {
	protected Supplier<Text> text = () -> null;
	
	protected boolean shadow = false;
	
	protected Color color = new Color(0x404040FFL);
	
	@Override
	public Size getStandardSize() {
		if (text.get() != null) {
			return new Size(TextUtil.getWidth(text.get()) + 8.0F, 18.0F);
		} else {
			return new Size(8.0F, 18.0F);
		}
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void draw(DrawContext context, float tickDelta) {
		var textRenderer = DrawingUtil.getTextRenderer();
		
		if (text.get() != null) {
			// TODO: Check if this is equivalent to the 1.19.2 code.
			context.drawText(textRenderer, text.get().asOrderedText(), (int) getX(), (int) getY(), color.toRgb(), shadow);
		}
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
