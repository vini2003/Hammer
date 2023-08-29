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

package dev.vini2003.hammer.core.api.common.math.size;

/**
 * A {@link Sized} represents an object which has
 * a three-dimensional size, delegated to a {@link Size}.
 */
public interface Sized extends SizeHolder {
	/**
	 * Provides a default size for this object.
	 * @return a default size for this object.
	 */
	Size getStandardSize();
	
	/**
	 * Returns this object's size.
	 * @return this object's size.
	 */
	Size getSize();
	
	/**
	 * Sets this object's size.
	 * @param size the new size.
	 */
	void setSize(Size size);
	
	/**
	 * Sets this object's size.
	 * @param width the new size's width.
	 * @param height the new size's height.
	 * @param length the new size's length.
	 */
	default void setSize(float width, float height, float length) {
		setSize(new Size(width, height, length));
	}
	
	/**
	 * Sets this object's size.
	 * @param width the new size's width.
	 * @param height the new size's height.
	 */
	default void setSize(float width, float height) {
		setSize(new Size(width, height));
	}
	
	/**
	 * Sets this object's width.
	 * @param width the new width.
	 */
	default void setWidth(float width) {
		setSize(new Size(width, getHeight(), getLength()));
	}
	
	/**
	 * Sets this object's height.
	 * @param height the new height.
	 */
	default void setHeight(float height) {
		setSize(new Size(getWidth(), height, getLength()));
	}
	
	/**
	 * Sets this object's length.
	 * @param length the new length.
	 */
	default void setLength(float length) {
		setSize(new Size(getWidth(), getHeight(), length));
	}
	
	@Override
	default float getWidth() {
		return getSize().getWidth();
	}
	
	@Override
	default float getHeight() {
		return getSize().getHeight();
	}
	
	@Override
	default float getLength() {
		return getSize().getLength();
	}
	
}
