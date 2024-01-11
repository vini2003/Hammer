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
import org.joml.Quaternionf;

/**
 * A {@link RotateModifier} is a {@link Shape} modifier that applies a rotation to blocks inside the shape.
 */
public class RotateModifier implements Modifier {
	private final Quaternionf rotation;
	
	/**
	 * Constructs a rotation modifier.
	 * @param rotation the rotation to use.
	 * @return the modifier.
	 */
	public RotateModifier(Quaternionf rotation) {
		this.rotation = rotation;
	}
	
	/**
	 * Modifies the shape's starter position to account for rotation.
	 * @param shape the shape to modify.
	 * @return the modified shape.
	 */
	@Override
	public StaticPosition modifyStartPos(Shape shape) {
		var minPos = Position.of(Float.MAX_VALUE, Float.MAX_VALUE);
		
		for (var p : shape.getPositions()) {
			if (p.getX() < minPos.getX() || p.getY() < minPos.getY() || p.getZ() < minPos.getZ()) {
				minPos = p;
			}
		}
		
		if (minPos.getX() != Float.MAX_VALUE & minPos.getY() != Float.MAX_VALUE && minPos.getZ() != Float.MAX_VALUE) {
			return minPos;
		} else {
			return shape.getStartPos();
		}
	}
	
	/**
	 * Modifies the shape's end position to account for rotation.
	 * @param shape the shape to modify.
	 * @return the modified shape.
	 */
	@Override
	public StaticPosition modifyEndPos(Shape shape) {
		var maxPos = Position.of(Float.MIN_VALUE, Float.MIN_VALUE);
		
		for (var p : shape.getPositions()) {
			if (p.getX() > maxPos.getX() || p.getY() > maxPos.getY() || p.getZ() > maxPos.getZ()) {
				maxPos = p;
			}
		}
		
		if (maxPos.getX() != Float.MIN_VALUE & maxPos.getY() != Float.MIN_VALUE && maxPos.getZ() != Float.MIN_VALUE) {
			return maxPos;
		} else {
			return shape.getEndPos();
		}
	}
	
	/**
	 * Modifiers the position to apply rotation.
	 * @param pos the position to modify.
	 * @return the modified position.
	 */
	@Override
	public StaticPosition modifyEquation(StaticPosition pos) {
		return pos.rotate(rotation);
	}
}
