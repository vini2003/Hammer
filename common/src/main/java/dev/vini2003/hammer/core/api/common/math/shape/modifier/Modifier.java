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

package dev.vini2003.hammer.core.api.common.math.shape.modifier;

import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.position.StaticPosition;
import dev.vini2003.hammer.core.api.common.math.shape.Shape;

/**
 * A {@link Modifier} is applied to {@link Shape}s to modify their positions.
 */
@FunctionalInterface
public interface Modifier {
	/**
	 * Modifies the given shape's start position.
	 * <b>This is important if the positions inside the shape are distorted and may now be outside the original three-dimensional space.</b>
	 * @param shape the shape to modify.
	 * @return the modified shape.
	 */
	default Position modifyStartPos(Shape shape) {
		return shape.getStartPos();
	}
	
	/**
	 * Modifies the given shape's end position.
	 * <b>This is important if the positions inside the shape are distorted and may now be outside the original three-dimensional space.</b>
	 * @param shape the shape to modify.
	 * @return the modified shape.
	 */
	default Position modifyEndPos(Shape shape) {
		return shape.getEndPos();
	}
	
	/**
	 * Modifies the given position.
	 * @param pos the position to modify.
	 * @return the modified position.
	 */
	Position modifyEquation(Position pos);
}
