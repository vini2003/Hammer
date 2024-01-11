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

import java.util.function.BooleanSupplier;

public class HudBarWidget extends ImageBarWidget {
	protected BooleanSupplier showSupplier = () -> false;
	
	protected Side side = Side.RIGHT;
	protected Type type = Type.CONTINUOS;
	
	public void setShow(BooleanSupplier showSupplier) {
		this.showSupplier = showSupplier;
	}
	
	public void setShow(boolean show) {
		setShow(() -> show);
	}
	
	public boolean shouldShow() {
		return showSupplier.getAsBoolean();
	}
	
	public Side getSide() {
		return side;
	}
	
	public void setSide(Side side) {
		this.side = side;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public enum Side {
		LEFT,
		RIGHT
	}
	
	public enum Type {
		CONTINUOS,
		TILED
	}
}
