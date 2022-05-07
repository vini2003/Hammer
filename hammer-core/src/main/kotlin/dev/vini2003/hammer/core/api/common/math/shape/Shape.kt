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

package dev.vini2003.hammer.core.api.common.math.shape

import dev.vini2003.hammer.core.api.common.math.position.Position
import dev.vini2003.hammer.core.api.common.math.shape.modifier.BaseModifier
import kotlin.math.pow

/**
 * A [ShapeYeet] is a geometric form with a position.
 */
open class Shape(
	/**
	 * Constructs a shape.
	 *
	 * @param eequation the shape's equation, checks whether a point is within the shape or not.
	 * @param startPos the shape's start position.
	 * @param endPos the shape's end position.
	 */
	val startPos: Position,
	val endPos: Position,
	val equation: Shape.(Position) -> Boolean
) {
	class ScreenRectangle @JvmOverloads constructor(
		width: Float,
		height: Float,
		relativePos: Position = Position(0.0F, 0.0F, 0.0F),
		startPos: Position = relativePos + Position(0.0F, 0.0F),
		endPos: Position = relativePos + Position(width, height),
	) : Shape(
		startPos,
		endPos,
		{ true }
	)

	class ScreenEllipse @JvmOverloads constructor(
		a: Float,
		b: Float,
		relativePos: Position = Position(0.0F, 0.0F, 0.0F),
		startPos: Position = relativePos + Position(0.0F, 0.0F, 0.0F),
		endPos: Position = relativePos + Position(a, b, 0.0F)
	) : Shape(
		startPos,
		endPos,
		{ pos ->
			((pos.x - startPos.x).pow(2) / a.pow(2)) + ((pos.y - startPos.y).pow(2) / b.pow(2)) < 1.0F
		}
	)
	
	class Rectangle @JvmOverloads constructor(
		width: Float,
		depth: Float,
		relativePos: Position = Position(0.0F, 0.0F, 0.0F),
		startPos: Position = relativePos + Position(width / 2.0F, 0.0F, depth / 2.0F),
		endPos: Position = relativePos + Position(-width / 2.0F, 0.0F, depth / 2.0F)
	) : Shape(
		startPos,
		endPos,
		{ pos ->
			(pos.y - startPos.y) > 0.0F &&
			(pos.y - startPos.y) <= 1.0F
		}
	)
	
	class Ellipse @JvmOverloads constructor(
		a: Float,
		b: Float,
		relativePos: Position = Position(0.0F, 0.0F, 0.0F),
		startPos: Position = relativePos + Position(a, 0.0F, b),
		endPos: Position = relativePos + Position(-a, 0.0F, b)
	) : Shape(
		startPos,
		endPos,
		{ pos ->
			((pos.x - startPos.x).pow(2) / a.pow(2)) + ((pos.z - startPos.z).pow(2) / b.pow(2)) < 1.0F &&
			 (pos.y - startPos.y) > 0.0F &&
			 (pos.y - startPos.y) <= 1.0F
		}
	)
	
	class EllipticalPrism @JvmOverloads constructor(
		a: Float,
		b: Float,
		height: Float,
		relativePos: Position = Position(0.0F, 0.0F, 0.0F),
		startPos: Position = relativePos + Position(a, height / 2.0F, b),
		endPos: Position = relativePos + Position(-a, -height / 2.0F, b)
	) : Shape(
		startPos,
		endPos,
		{ pos ->
			((pos.x - startPos.x).pow(2) / a.pow(2)) + ((pos.z - startPos.z).pow(2) / b.pow(2)) < 1.0F &&
			 (pos.y - startPos.y) > -height / 2.0F &&
			 (pos.y - startPos.y) <  height / 2.0F
		}
	)
	
	class RectangularPrism @JvmOverloads constructor(
		width: Float,
		height: Float,
		depth: Float,
		relativePos: Position = Position(0.0F, 0.0F, 0.0F),
		startPos: Position = relativePos + Position(width / 2.0F, height / 2.0F, depth / 2.0F),
		endPos: Position = relativePos + Position(-width / 2.0F, -height / 2.0F, -depth / 2.0F)
	) : Shape(
		startPos,
		endPos,
		{ pos ->
			(pos.x - startPos.x) > -width / 2.0F &&
			(pos.x - startPos.x) <  width / 2.0F &&
			(pos.y - startPos.y) > -height / 2.0F &&
			(pos.y - startPos.y) <  height / 2.0F &&
			(pos.z - startPos.z) > -depth / 2.0F &&
			(pos.z - startPos.z) <  depth / 2.0F
		}
	)
	
	class TriangularPrism @JvmOverloads constructor(
		width: Float,
		height: Float,
		depth: Float,
		relativePos: Position = Position(0.0F, 0.0F, 0.0F),
		startPos: Position = relativePos + Position(width / 2.0F, height / 2.0F, depth / 2.0F),
		endPos: Position = relativePos + Position(-width / 2.0F, -height/ 2.0F, -depth / 2.0F)
	) : Shape (
		startPos,
		endPos,
		{ pos ->
			(pos.x - startPos.x) > -(width / 2.0F) &&
			(pos.x - startPos.x) <  (width / 2.0F) &&
			(pos.y - startPos.y) > -(height / 2.0F) &&
			(pos.y - startPos.y) <  (height / 2.0F) &&
			(pos.z - startPos.z) > -(depth * (1.0F - (((pos.y - startPos.y) + height / 2.0F) / height))) / 2.0F &&
			(pos.z - startPos.z) <  (depth * (1.0F - (((pos.y - startPos.y) + height / 2.0F) / height))) / 2.0F
		}
	)
	
	class RectangularPyramid @JvmOverloads constructor(
		width: Float,
		height: Float,
		depth: Float,
		relativePos: Position = Position(0.0F, 0.0F, 0.0F),
		startPos: Position = relativePos + Position(width / 2.0F, height, depth / 2.0F),
		endPos: Position = relativePos + Position(-width / 2.0F, 0.0F, -depth / 2.0F)
	) : Shape(
		startPos,
		endPos,
		{ pos ->
			(pos.x - startPos.x) > -(width * (1.0F - ((pos.y - startPos.y) / height))) / 2.0F &&
			(pos.x - startPos.x) <  (width * (1.0F - ((pos.y - startPos.y) / height))) / 2.0F &&
			(pos.y - startPos.y) >  0.0F && (pos.y - startPos.y) < height &&
			(pos.z - startPos.z) > -(depth * (1.0F - ((pos.y - startPos.y) / height))) / 2.0F &&
			(pos.z - startPos.z) <  (depth * (1.0F - ((pos.y - startPos.y) / height))) / 2.0F
		}
	)
	
	class EllipticalPyramid @JvmOverloads constructor(
		a: Float,
		b: Float,
		height: Float,
		relativePos: Position = Position(0.0F, 0.0F, 0.0F),
		startPos: Position = relativePos + Position( a, height, b),
		endPos: Position = relativePos + Position(-a, 0.0F, -b)
	): Shape(
		startPos,
		endPos,
		{ pos ->
			((pos.x - startPos.x).pow(2) / (a * (1.0F - ((pos.y - startPos.y) / height))).pow(2)) +
			((pos.z - startPos.z).pow(2) / (b * (1.0F - ((pos.y - startPos.y) / height))).pow(2)) < 1.0F &&
			 (pos.y - startPos.y) > 0.0F &&
			 (pos.y - startPos.y) < height
		}
	)
	
	class Ellipsoid @JvmOverloads constructor(
		a: Float,
		b: Float,
		c: Float,
		relativePos: Position = Position(0.0F, 0.0F, 0.0F),
		startPos: Position = relativePos + Position(a, c, b),
		endPos: Position = relativePos + Position(-a, -c, -b)
	) : Shape(
		startPos,
		endPos,
		{ pos ->
			((pos.x - startPos.x).pow(2) / a.pow(2)) +
			((pos.y - startPos.y).pow(2) / b.pow(2)) +
			((pos.z - startPos.z).pow(2) / c.pow(2)) < 1.0F
		}
	)
	
	class HemiEllipsoid @JvmOverloads constructor(
		a: Float,
		b: Float,
		c: Float,
		relativePos: Position = Position(0.0F, 0.0F, 0.0F),
		startPos: Position = relativePos + Position(a, c, b),
		endPos: Position = relativePos + Position(-a, 0.0F, -b)
	) : Shape(
		startPos,
		endPos,
		{ pos ->
			((pos.x - startPos.x).pow(2) / a.pow(2)) +
			((pos.z - startPos.z).pow(2) / b.pow(2)) +
			((pos.y - startPos.y).pow(2) / c.pow(2)) > 0.0F
		}
	)
	
	val width: Float by lazy { startPos.x - endPos.x }
	val height: Float by lazy { startPos.y - endPos.y }
	val depth: Float by lazy { startPos.z - endPos.z }
	
	/**
	 * Returns whether the position is within this shape or not.
	 *
	 * @parma position the position.
	 * @return the result.
	 */
	fun isPositionWithin(pos: Position): Boolean {
		return pos.x >= startPos.x &&
			   pos.x <= endPos.x &&
			   pos.y >= startPos.y &&
			   pos.y <= endPos.y &&
			   pos.z >= startPos.z &&
			   pos.z <= endPos.z &&
			   equation.invoke(this, pos - startPos)
	}
	
	/**
	 * Applies a modifier to this shape.
	 *
	 * @param modifier the modifier.
	 * @return a new shape with the modifier.
	 */
	fun applyModifier(modifier: BaseModifier): Shape {
		val chainEquation = equation
		
		return Shape(
			startPos,
			endPos
		) { pos ->
			chainEquation(modifier(pos))
		}
	}
	
	/**
	 * Returns a stream of this shape.
	 */
	fun sequence(): Sequence<Position> {
		return Position.sequence(startPos, endPos).filter { position ->
			isPositionWithin(position)
		}
	}
}