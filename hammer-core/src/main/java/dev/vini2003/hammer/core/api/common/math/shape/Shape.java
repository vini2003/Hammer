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

package dev.vini2003.hammer.core.api.common.math.shape;

import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.shape.modifier.Modifier;
import dev.vini2003.hammer.core.api.common.math.shape.modifier.NoiseModifier;
import dev.vini2003.hammer.core.api.common.math.shape.modifier.RotateModifier;
import dev.vini2003.hammer.core.api.common.math.shape.modifier.TranslateModifier;
import dev.vini2003.hammer.core.api.common.math.size.SizeHolder;
import net.minecraft.util.math.Quaternion;

import java.util.Collection;
import java.util.Random;
import java.util.function.BiPredicate;

/**
 * A {@link Shape} represents a volume in three-dimensional space
 * whose positions may be calculated.
 * <br>
 * Multiple types are available and can be modified using {@link Modifier}s.
 * <br>
 * Available shapes:
 * <ul>
 *     <li>{@link Rectangle2D}</li>
 *     <li>{@link Ellipse2D}</li>
 *     <li>{@link Rectangle3D}</li>
 *     <li>{@link Ellipse3D}</li>
 *     <li>{@link EllipticalPyramid3D}</li>
 *     <li>{@link RectangularPrism3D}</li>
 *     <li>{@link TriangularPrism3D}</li>
 *     <li>{@link RectangularPrism3D}</li>
 *     <li>{@link EllipticalPyramid3D}</li>
 *     <li>{@link Ellipsoid3D}</li>
 *     <li>{@link HemiEllipsoid3D}</li>
 * </ul>
 * <br>
 * Available modifiers:
 * <ul>
 *     <li>{@link NoiseModifier}</li>
 *     <li>{@link RotateModifier}</li>
 *     <li>{@link TranslateModifier}</li>
 * </ul>
 */
public class Shape implements SizeHolder {
	protected final Position startPos;
	protected final Position endPos;
	
	protected BiPredicate<Shape, Position> equation;
	
	/**
	 * Constructs a shape.
	 *
	 * @param startPos  the shape's start position.
	 * @param endPos    the shape's end position.
	 */
	public Shape(Position startPos, Position endPos) {
		this.startPos = startPos;
		this.endPos = endPos;
	}
	
	/**
	 * Constructs a shape.
	 *
	 * @param startPos  the shape's start position.
	 * @param endPos    the shape's end position.
	 * @param equation the shape's equation, checks whether a point is within the shape or not.
	 */
	public Shape(Position startPos, Position endPos, BiPredicate<Shape, Position> equation) {
		this.startPos = startPos;
		this.endPos = endPos;
		this.equation = equation;
	}
	
	
	/**
	 * Returns whether the position is within this shape or not.
	 *
	 * @return the result.
	 *
	 * @param pos the position.
	 */
	public boolean isPositionWithin(Position pos) {
		return pos.getX() >= startPos.getX() && pos.getX() <= endPos.getX() &&
			   pos.getY() >= startPos.getY() && pos.getY() <= endPos.getY() && pos.getZ() >= startPos.getZ() &&
			   pos.getZ() <= endPos.getZ() &&
			   equation.test(this, pos.minus(startPos));
	}
	
	/**
	 * Returns the positions within this shape.
	 *
	 * @return the result.
 	 */
	public Collection<Position> getPositions() {
		return Position.collect(startPos, endPos);
	}
	
	/**
	 * Applies a modifier to this shape.
	 *
	 * @param modifier the modifier.
	 *
	 * @return a new shape with the modifier.
	 */
	public Shape applyModifier(Modifier modifier) {
		var chainEquation = equation;
		
		return new Shape(modifier.modifyStartPos(this), modifier.modifyEndPos(this), (shape, pos) -> chainEquation.test(this, modifier.modifyEquation(pos)));
	}
	
	/**
	 * Applies a translate modifier to this shape.
	 *
	 * @return a new shape with the modifier.
	 */
	public Shape translate(float x, float y, float z) {
		return applyModifier(new TranslateModifier(x, y, z));
	}
	
	/**
	 * Applies a translate modifier to this shape.
	 *
	 * @return a new shape with the modifier.
	 */
	public Shape translate(float x, float y) {
		return applyModifier(new TranslateModifier(x, y, 0.0F));
	}
	
	/**
	 * Applies a translate modifier to this shape.
	 *
	 * @return a new shape with the modifier.
	 */
	public Shape translate(Position pos) {
		return applyModifier(new TranslateModifier(pos.getX(), pos.getY(), pos.getZ()));
	}
	
	/**
	 * Applies a noise modifier to this shape.
	 *
	 * @return a new shape with the modifier.
	 */
	public Shape noise(Random random, float magnitude) {
		return applyModifier(new NoiseModifier(random, magnitude));
	}
	
	/**
	 * Applies a rotation modifier to this shape.
	 *
	 * @return a new shape with the modifier.
	 */
	public Shape rotate(Quaternion rotation) {
		return applyModifier(new RotateModifier(rotation));
	}
	
	/**
	 * Returns this shape's start position.
	 * @return this shape's start position.
	 */
	public Position getStartPos() {
		return startPos;
	}
	
	/**
	 * Returns this shape's end position.
	 * @return this shape's end position.
	 */
	public Position getEndPos() {
		return endPos;
	}
	
	@Override
	public float getWidth() {
		return Math.abs(startPos.getX() - endPos.getX());
	}
	
	@Override
	public float getHeight() {
		return Math.abs(startPos.getY() - endPos.getY());
	}
	
	@Override
	public float getLength() {
		return Math.abs(startPos.getZ() - endPos.getZ());
	}
	
	/**
	 * A {@link Rectangle2D} represents a two-dimensional rectangle.
	 */
	public static class Rectangle2D extends Shape {
		public Rectangle2D(float width, float height) {
			super(new Position(0.0F, 0.0F), new Position(width, height));
			
			this.equation = (shape, pos) -> {
				return true;
			};
		}
	}
	
	/**
	 * An {@link Ellipse2D} represents a two-dimensional ellipse.
	 */
	public static class Ellipse2D extends Shape {
		public Ellipse2D(float a, float b) {
			super(new Position(0.0F, 0.0F), new Position(a, b));
			
			this.equation = (shape, pos) -> {
				return (Math.pow(pos.getX() - startPos.getX(), 2.0D) / Math.pow(a, 2.0D)) + (Math.pow(pos.getY() - startPos.getY(), 2.0D) / Math.pow(b, 2.0D)) < 1.0F;
			};
		}
	}
	
	/**
	 * A {@link Rectangle3D} represents a three-dimensional rectangle.
	 */
	public static class Rectangle3D extends Shape {
		public Rectangle3D(float width, float depth) {
			super(new Position(width / 2.0F, 0.0F, depth / 2.0F), new Position(-width / 2.0F, 0.0F, -depth / 2.0F));
			
			this.equation = (shape, pos) -> {
				return  (pos.getY() - startPos.getY()) > 0.0F &&
						(pos.getY() - startPos.getY()) <= 1.0F;
			};
		}
	}
	
	/**
	 * An {@link Ellipse3D} represents a three-dimensional ellipse.
	 */
	public static class Ellipse3D extends Shape {
		public Ellipse3D(float a, float b) {
			super(new Position(a, 0.0F, b), new Position(-a, 0.0F, -b));
			
			this.equation = (shape, pos) -> {
				return  (Math.pow(pos.getX() - startPos.getX(), 2.0D) / Math.pow(a, 2.0D)) + (Math.pow(pos.getZ() - startPos.getZ(), 2.0D) / Math.pow(b, 2.0D)) < 1.0F &&
						(pos.getY() - startPos.getY()) > 0.0F &&
						(pos.getY() - startPos.getY()) <= 1.0F;
			};
		}
	}
	
	/**
	 * An {@link EllipticalPyramid3D} represents a three-dimensional elliptical pyramid.
	 */
	public static class EllipticalPrism3D extends Shape {
		public EllipticalPrism3D(float a, float b, float height) {
			super(new Position(a, height / 2.0F, b), new Position(-a, -height / 2.0F, -b));
			
			this.equation = (shape, pos) -> {
				return  (Math.pow(pos.getX() - startPos.getX(), 2.0D) / Math.pow(a, 2.0D)) + (Math.pow(pos.getZ() - startPos.getZ(), 2.0D) / Math.pow(b, 2.0D)) < 1.0F &&
						(pos.getY() - startPos.getY()) > -height / 2.0F &&
						(pos.getY() - startPos.getY()) < height / 2.0F;
			};
		}
	}
	
	/**
	 * A {@link RectangularPrism3D} represents a three-dimensional rectangular prism.
	 */
	public static class RectangularPrism3D extends Shape {
		public RectangularPrism3D(float width, float height, float depth) {
			super(new Position(width / 2.0F, height / 2.0F, depth / 2.0F), new Position(-width / 2.0F, -height / 2.0F, -depth / 2.0F));
			
			this.equation = (shape, pos) -> {
				return  (pos.getX() - startPos.getX()) > -width / 2.0F &&
						(pos.getX() - startPos.getX()) < width / 2.0F &&
						(pos.getY() - startPos.getY()) > -height / 2.0F &&
						(pos.getY() - startPos.getY()) < height / 2.0F &&
						(pos.getZ() - startPos.getZ()) > -depth / 2.0F &&
						(pos.getZ() - startPos.getZ()) < depth / 2.0F;
			};
		}
	}
	
	/**
	 * A {@link TriangularPrism3D} represents a three-dimensional triangular prism.
	 */
	public static class TriangularPrism3D extends Shape {
		public TriangularPrism3D(float width, float height, float depth) {
			super(new Position(width / 2.0F, height / 2.0F, depth / 2.0F), new Position(-width / 2.0F, -height / 2.0F, -depth / 2.0F));
			
			this.equation = (shape, pos) -> {
				return  (pos.getX() - startPos.getX()) > -(width / 2.0F) &&
						(pos.getX() - startPos.getX()) < (width / 2.0F) &&
						(pos.getY() - startPos.getY()) > -(height / 2.0F) &&
						(pos.getY() - startPos.getY()) < (height / 2.0F) &&
						(pos.getZ() - startPos.getZ()) > -(depth * (1.0F - (((pos.getY() - startPos.getY()) + height / 2.0F) / height))) / 2.0F &&
						(pos.getZ() - startPos.getZ()) < (depth * (1.0F - (((pos.getY() - startPos.getY()) + height / 2.0F) / height))) / 2.0F;
			};
		}
	}
	
	/**
	 * A {@link RectangularPrism3D} represents a three-dimensional rectangular pyramid.
	 */
	public static class RectangularPyramid3D extends Shape {
		public RectangularPyramid3D(float width, float height, float depth) {
			super(new Position(width / 2.0F, height, depth / 2.0F), new Position(-width / 2.0F, 0.0F, -depth / 2.0F));
			
			this.equation = (shape, pos) -> {
				return  (pos.getX() - startPos.getX()) > -(width * (1.0F - ((pos.getY() - startPos.getY()) / height))) / 2.0F &&
						(pos.getX() - startPos.getX()) < (width * (1.0F - ((pos.getY() - startPos.getY()) / height))) / 2.0F &&
						(pos.getY() - startPos.getY()) > 0.0F && (pos.getY() - startPos.getY()) < height &&
						(pos.getZ() - startPos.getZ()) > -(depth * (1.0F - ((pos.getY() - startPos.getY()) / height))) / 2.0F &&
						(pos.getZ() - startPos.getZ()) < (depth * (1.0F - ((pos.getY() - startPos.getY()) / height))) / 2.0F;
			};
		}
	}
	
	/**
	 * An {@link EllipticalPyramid3D} represents a three-dimensional elliptical pyramid.
	 */
	public static class EllipticalPyramid3D extends Shape {
		public EllipticalPyramid3D(float a, float b, float height) {
			super(new Position(a, height, b), new Position(-a, 0.0F, -b));
			
			this.equation = (shape, pos) -> {
				return  (Math.pow(pos.getX() - startPos.getX(), 2.0D) / Math.pow(a * (1.0F - ((pos.getY() - startPos.getY()) / height)), 2.0D)) +
						(Math.pow(pos.getZ() - startPos.getZ(), 2.0D) / Math.pow(b * (1.0F - ((pos.getY() - startPos.getY()) / height)), 2.0D)) < 1.0F &&
						(pos.getY() - startPos.getY()) > 0.0F &&
						(pos.getY() - startPos.getY()) < height;
			};
		}
	}
	
	/**
	 * An {@link Ellipsoid3D} represents a three-dimensional ellipsoid.
	 */
	public static class Ellipsoid3D extends Shape {
		public Ellipsoid3D(float a, float b, float c) {
			super(new Position(a, c, b), new Position(-a, -c, -b));
			
			this.equation = (shape, pos) -> {
				return  (Math.pow(pos.getX() - startPos.getX(), 2.0D) / Math.pow(a, 2.0D)) +
						(Math.pow(pos.getY() - startPos.getY(), 2.0D) / Math.pow(b, 2.0D)) +
						(Math.pow(pos.getZ() - startPos.getZ(), 2.0D) / Math.pow(c, 2.0D)) < 1.0F;
			};
		}
	}
	
	/**
	 * A {@link HemiEllipsoid3D} represents a three-dimensional hemi-ellipsoid.
	 */
	public static class HemiEllipsoid3D extends Shape {
		public HemiEllipsoid3D(float a, float b, float c) {
			super(new Position(a, c, b), new Position(-a, 0.0F, -b));
			
			this.equation = (shape, pos) -> {
				return  (Math.pow(pos.getX() - startPos.getX(), 2.0D) / Math.pow(a, 2.0D)) +
						(Math.pow(pos.getZ() - startPos.getZ(), 2.0D) / Math.pow(b, 2.0D)) +
						(Math.pow(pos.getY() - startPos.getY(), 2.0D) / Math.pow(c, 2.0D)) > 0.0F;
			};
		}
	}
}
