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

package dev.vini2003.hammer.gui.api.common.widget.arrow;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.gui.api.common.widget.bar.ImageBarWidget;

public class ArrowWidget extends ImageBarWidget {
	public static final Texture STANDARD_VERTICAL_FOREGROUND_TEXTURE = new ImageTexture(HC.id("textures/widget/vertical_arrow_foreground.png"));
	public static final Texture STANDARD_VERTICAL_BACKGROUND_TEXTURE = new ImageTexture(HC.id("textures/widget/vertical_arrow_background.png"));
	
	public static final Texture STANDARD_HORIZONTAL_FOREGROUND_TEXTURE = new ImageTexture(HC.id("textures/widget/horizontal_arrow_foreground.png"));
	public static final Texture STANDARD_HORIZONTAL_BACKGROUND_TEXTURE = new ImageTexture(HC.id("textures/widget/horizontal_arrow_background.png"));
	
	public ArrowWidget() {
		super();
		
		setHorizontal(true);
		setVertical(false);
		
		setSmooth(false);
		setScissor(true);
	}
	
	@Override
	public void setHorizontal(boolean horizontal) {
		super.setHorizontal(horizontal);
		
		foregroundTexture = () -> STANDARD_HORIZONTAL_FOREGROUND_TEXTURE;
		backgroundTexture = () -> STANDARD_HORIZONTAL_BACKGROUND_TEXTURE;
	}
	
	@Override
	public void setVertical(boolean vertical) {
		super.setVertical(vertical);
		
		foregroundTexture = () -> STANDARD_VERTICAL_FOREGROUND_TEXTURE;
		backgroundTexture = () -> STANDARD_VERTICAL_BACKGROUND_TEXTURE;
	}
}

