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
import dev.vini2003.hammer.core.api.common.math.shape.Shape;

/**
 * A {@link TranslateModifier} is a {@link Shape} modifier that applies a translation to blocks inside the shape.
 */
public class TranslateModifier implements Modifier {
	private final float x;
	private final float y;
	private final float z;
	
	/**
	 * Constructs a translate modifier.
	 * @param x the X translation component.
	 * @param y the Y translation component.
	 * @param z the Z translation component.
	 * @return the modifier.
	 */
	public TranslateModifier(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Modifies the shape's starter position to account for translation.
	 * @param shape the shape to modify.
	 * @return the modified shape.
	 */
	@Override
	public Position modifyStartPos(Shape shape) {
		return shape.getStartPos().offset(x, y, z);
	}
	
	/**
	 * Modifies the shape's end position to account for rotation.
	 * @param shape the shape to modify.
	 * @return the modified shape.
	 */
	@Override
	public Position modifyEndPos(Shape shape) {
		return shape.getEndPos().offset(x, y, z);
	}
	
	/**
	 * Modifiers the position to apply rotation.
	 * @param pos the position to modify.
	 * @return the modified position.
	 */
	@Override
	public Position modifyEquation(Position pos) {
		return new Position(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
	}
}
