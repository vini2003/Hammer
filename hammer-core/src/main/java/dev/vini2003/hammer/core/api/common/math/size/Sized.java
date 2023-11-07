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

import dev.vini2003.hammer.core.api.common.supplier.FloatSupplier;

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
		setSize(new StaticSize(width, height, length));
	}
	
	/**
	 * Sets this object's size.
	 * @param anchor the new size's anchor.
	 * @param width the new size's width.
	 * @param height the new size's height.
	 * @param length the new size's length.
	 */
	default void setSize(SizeHolder anchor, float width, float height, float length) {
		setSize(new StaticSize(anchor, width, height, length));
	}
	
	/**
	 * Sets this object's size.
	 * @param width the new size's width.
	 * @param height the new size's height.
	 */
	default void setSize(float width, float height) {
		setSize(new StaticSize(width, height));
	}
	
	/**
	 * Sets this object's size.
	 * @param anchor the new size's anchor.
	 * @param relativeWidth the new size's relative width.
	 * @param relativeHeight the new size's relative height.
	 */
	default void setSize(SizeHolder anchor, float relativeWidth, float relativeHeight) {
		setSize(new StaticSize(anchor, relativeWidth, relativeHeight));
	}
	
	/**
	 * Sets this object's size.
	 * @param anchor the new size's anchor.
	 * @param relativeSize the new size's relative size.
	 */
	default void setSize(SizeHolder anchor, SizeHolder relativeSize) {
		setSize(new StaticSize(anchor, relativeSize));
	}
	
	/**
	 * Sets this object's size using dynamic width, height, and length.
	 * @param widthSupplier the supplier for the new size's width.
	 * @param heightSupplier the supplier for the new size's height.
	 * @param lengthSupplier the supplier for the new size's length.
	 */
	default void setSize(FloatSupplier widthSupplier, FloatSupplier heightSupplier, FloatSupplier lengthSupplier) {
		setSize(new DynamicSize(widthSupplier, heightSupplier, lengthSupplier));
	}
	
	/**
	 * Sets this object's size relative to an anchor with dynamic width, height, and length.
	 * @param anchor the new size's anchor.
	 * @param widthSupplier the supplier for the new size's relative width.
	 * @param heightSupplier the supplier for the new size's relative height.
	 * @param lengthSupplier the supplier for the new size's relative length.
	 */
	default void setSize(SizeHolder anchor, FloatSupplier widthSupplier, FloatSupplier heightSupplier, FloatSupplier lengthSupplier) {
		setSize(new DynamicSize(
				() -> anchor.getWidth() + widthSupplier.get(),
				() -> anchor.getHeight() + heightSupplier.get(),
				() -> anchor.getLength() + lengthSupplier.get()
		));
	}
	
	/**
	 * Sets this object's size using dynamic width and height, assuming length as 0.
	 * @param widthSupplier the supplier for the new size's width.
	 * @param heightSupplier the supplier for the new size's height.
	 */
	default void setSize(FloatSupplier widthSupplier, FloatSupplier heightSupplier) {
		setSize(new DynamicSize(widthSupplier, heightSupplier, () -> 0.0F));
	}
	
	/**
	 * Sets this object's size relative to an anchor with dynamic width and height, assuming length as the anchor's length.
	 * @param anchor the new size's anchor.
	 * @param widthSupplier the supplier for the new size's relative width.
	 * @param heightSupplier the supplier for the new size's relative height.
	 */
	default void setSize(SizeHolder anchor, FloatSupplier widthSupplier, FloatSupplier heightSupplier) {
		setSize(new DynamicSize(
				() -> anchor.getWidth() + widthSupplier.get(),
				() -> anchor.getHeight() + heightSupplier.get(),
				anchor::getLength
		));
	}
	
	/**
	 * Sets this object's width.
	 * @param width the new width.
	 */
	default void setWidth(float width) {
		setSize(new StaticSize(width, getHeight(), getLength()));
	}
	
	/**
	 * Sets this object's height.
	 * @param height the new height.
	 */
	default void setHeight(float height) {
		setSize(new StaticSize(getWidth(), height, getLength()));
	}
	
	/**
	 * Sets this object's length.
	 * @param length the new length.
	 */
	default void setLength(float length) {
		setSize(new StaticSize(getWidth(), getHeight(), length));
	}
	
	/**
	 * Sets this object's width using a supplier.
	 * @param widthSupplier the supplier for the new width.
	 */
	default void setWidth(FloatSupplier widthSupplier) {
		setSize(widthSupplier, getSize()::getHeight, getSize()::getLength);
	}
	
	/**
	 * Sets this object's height using a supplier.
	 * @param heightSupplier the supplier for the new height.
	 */
	default void setHeight(FloatSupplier heightSupplier) {
		setSize(getSize()::getWidth, heightSupplier, getSize()::getLength);
	}
	
	/**
	 * Sets this object's length using a supplier.
	 * @param lengthSupplier the supplier for the new length.
	 */
	default void setLength(FloatSupplier lengthSupplier) {
		setSize(getSize()::getWidth, getSize()::getHeight, lengthSupplier);
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
