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

import java.util.Objects;

/**
 * A {@link Size} represents a three-dimensional shape
 * whose dimensions do not have a fixed storage medium.
 *
 * Implementations include {@link StaticSize}, whose dimensions
 * are fixed, and {@link DynamicSize}, whose dimensions are not.
 */
public abstract class Size implements SizeHolder {
	/**
	 * A factory for creating a StaticSize with specified width, height, and length components.
	 *
	 * @param width  the width component of the size.
	 * @param height the height component of the size.
	 * @param length the length component of the size.
	 * @return a new StaticSize instance with the specified dimensions.
	 */
	public static StaticSize of(float width, float height, float length) {
		return new StaticSize(width, height, length);
	}
	
	/**
	 * A factory for creating a StaticSize with specified width and height components, assuming length as zero.
	 *
	 * @param width  the width component of the size.
	 * @param height the height component of the size.
	 * @return a new StaticSize instance with the specified dimensions and length set to zero.
	 */
	public static StaticSize of(float width, float height) {
		return new StaticSize(width, height);
	}
	
	/**
	 * A factory for creating a StaticSize relative to an anchor with additional relative width, height, and length components.
	 *
	 * @param anchor         the size holder to serve as an anchor.
	 * @param relativeWidth  the relative width component to add to the anchor's width.
	 * @param relativeHeight the relative height component to add to the anchor's height.
	 * @param relativeLength the relative length component to add to the anchor's length.
	 * @return a new StaticSize instance offset from the anchor by the relative components.
	 */
	public static StaticSize of(SizeHolder anchor, float relativeWidth, float relativeHeight, float relativeLength) {
		return new StaticSize(anchor, relativeWidth, relativeHeight, relativeLength);
	}
	
	/**
	 * A factory for creating a StaticSize relative to an anchor with additional relative width and height components.
	 *
	 * @param anchor        the size holder to serve as an anchor.
	 * @param relativeWidth the relative width component to add to the anchor's width.
	 * @param relativeHeight the relative height component to add to the anchor's height.
	 * @return a new StaticSize instance offset from the anchor by the relative components with the same length as the anchor.
	 */
	public static StaticSize of(SizeHolder anchor, float relativeWidth, float relativeHeight) {
		return new StaticSize(anchor, relativeWidth, relativeHeight);
	}
	
	/**
	 * A factory for creating a StaticSize with the same dimensions as the given size holder.
	 *
	 * @param anchor the size holder whose dimensions should be copied.
	 * @return a new StaticSize instance with the same dimensions as the anchor.
	 */
	public static StaticSize of(SizeHolder anchor) {
		return new StaticSize(anchor);
	}
	
	/**
	 * A factory for creating a DynamicSize with suppliers for width, height, and length.
	 *
	 * @param width  the supplier for the width component of the size.
	 * @param height the supplier for the height component of the size.
	 * @param length the supplier for the length component of the size.
	 * @return a new DynamicSize instance with dynamically supplied dimensions.
	 */
	public static DynamicSize of(FloatSupplier width, FloatSupplier height, FloatSupplier length) {
		return new DynamicSize(width, height, length);
	}
	
	/**
	 * A factory for creating a DynamicSize with suppliers for width and height, assuming length as zero.
	 *
	 * @param width  the supplier for the width component of the size.
	 * @param height the supplier for the height component of the size.
	 * @return a new DynamicSize instance with dynamically supplied dimensions and length set to zero.
	 */
	public static DynamicSize of(FloatSupplier width, FloatSupplier height) {
		return new DynamicSize(width, height, () -> 0.0F);
	}
	
	/**
	 * A factory for creating a DynamicSize relative to an anchor with suppliers for relative width, height, and length.
	 *
	 * @param anchor         the size holder to serve as an anchor.
	 * @param relativeWidth  the supplier for the relative width component.
	 * @param relativeHeight the supplier for the relative height component.
	 * @param relativeLength the supplier for the relative length component.
	 * @return a new DynamicSize instance dynamically offset from the anchor by the relative components.
	 */
	public static DynamicSize of(SizeHolder anchor, FloatSupplier relativeWidth, FloatSupplier relativeHeight, FloatSupplier relativeLength) {
		return new DynamicSize(
				() -> anchor.getWidth() + relativeWidth.get(),
				() -> anchor.getHeight() + relativeHeight.get(),
				() -> anchor.getLength() + relativeLength.get()
		);
	}
	
	/**
	 * A factory for creating a DynamicSize relative to an anchor with suppliers for relative width and height.
	 *
	 * @param anchor        the size holder to serve as an anchor.
	 * @param relativeWidth the supplier for the relative width component.
	 * @param relativeHeight the supplier for the relative height component.
	 * @return a new DynamicSize instance dynamically offset from the anchor by the relative components with the same length as the anchor.
	 */
	public static DynamicSize of(SizeHolder anchor, FloatSupplier relativeWidth, FloatSupplier relativeHeight) {
		return new DynamicSize(
				() -> anchor.getWidth() + relativeWidth.get(),
				() -> anchor.getHeight() + relativeHeight.get(),
				anchor::getLength
		);
	}

	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (!(o instanceof Size)) {
			return false;
		}
		
		var size = (Size) o;
		
		return Float.compare(size.getWidth(), getWidth()) == 0 && Float.compare(size.getHeight(), getHeight()) == 0 && Float.compare(size.getLength(), getLength()) == 0;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getWidth(), getHeight(), getLength());
	}
}
