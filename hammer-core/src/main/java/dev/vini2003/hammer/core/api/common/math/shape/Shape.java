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
import dev.vini2003.hammer.core.api.common.math.shape.modifier.TranslateModifier;
import dev.vini2003.hammer.core.api.common.math.size.SizeHolder;

import java.util.Random;
import java.util.function.BiPredicate;

public class Shape implements SizeHolder {
	protected final Position startPos;
	protected final Position endPos;
	
	protected BiPredicate<Shape, Position> equation;
	
	/**
	 * Constructs a shape.
	 *
	 * @param eequation the shape's equation, checks whether a point is within the shape or not.
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
	 * @param eequation the shape's equation, checks whether a point is within the shape or not.
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
	 * @parma position the position.
	 */
	public boolean isPositionWithin(Position pos) {
		return pos.getZ() >= startPos.getZ() &&
				pos.getZ() <= endPos.getZ() &&
				pos.getY() >= startPos.getY() &&
				pos.getY() <= endPos.getY() &&
				pos.getZ() >= startPos.getZ() &&
				pos.getZ() <= endPos.getZ() &&
				equation.test(this, pos.minus(startPos));
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
		
		return new Shape(startPos, endPos, (shape, pos) -> chainEquation.test(this, modifier.modify(pos)));
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
	 * Applies a noise modifier to this shape.
	 *
	 * @return a new shape with the modifier.
	 */
	public Shape noise(Random random, float magnitude) {
		return applyModifier(new NoiseModifier(random, magnitude));
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
	
	public Position getStartPos() {
		return startPos;
	}
	
	public Position getEndPos() {
		return endPos;
	}
	
	public static class ScreenRectangle extends Shape {
		public ScreenRectangle(float width, float height) {
			super(new Position(0.0F, 0.0F), new Position(width, height));
			
			this.equation = (shape, pos) -> {
				return true;
			};
		}
	}
	
	public static class ScreenEllipse extends Shape {
		public ScreenEllipse(float a, float b) {
			super(new Position(0.0F, 0.0F), new Position(a, b));
			
			this.equation = (shape, pos) -> {
				return (Math.pow(pos.getX() - startPos.getX(), 2.0D) / Math.pow(a, 2.0D)) + (Math.pow(pos.getY() - startPos.getY(), 2.0D) / Math.pow(b, 2.0D)) < 1.0F;
			};
		}
	}
	
	public static class Rectangle extends Shape {
		public Rectangle(float width, float depth) {
			super(new Position(width / 2.0F, 0.0F, depth / 2.0F), new Position(-width / 2.0F, 0.0F, -depth / 2.0F));
			
			this.equation = (shape, pos) -> {
				return (pos.getY() - startPos.getY()) > 0.0F &&
						(pos.getY() - startPos.getY()) <= 1.0F;
			};
		}
	}
	
	public static class Ellipse extends Shape {
		public Ellipse(float a, float b) {
			super(new Position(a, 0.0F, b), new Position(-a, 0.0F, -b));
			
			this.equation = (shape, pos) -> {
				return (Math.pow(pos.getX() - startPos.getX(), 2.0D) / Math.pow(a, 2.0D)) + (Math.pow(pos.getZ() - startPos.getZ(), 2.0D) / Math.pow(b, 2.0D)) < 1.0F &&
						(pos.getY() - startPos.getY()) > 0.0F &&
						(pos.getY() - startPos.getY()) <= 1.0F;
			};
		}
	}
	
	public static class EllipitcalPrism extends Shape {
		public EllipitcalPrism(float a, float b, float height) {
			super(new Position(a, height / 2.0F, b), new Position(-a, -height / 2.0F, -b));
			
			this.equation = (shape, pos) -> {
				return (Math.pow(pos.getZ() - startPos.getZ(), 2.0D) / Math.pow(a, 2.0D)) + (Math.pow(pos.getZ() - startPos.getZ(), 2.0D) / Math.pow(b, 2.0D)) < 1.0F &&
						(pos.getY() - startPos.getY()) > -height / 2.0F &&
						(pos.getY() - startPos.getY()) < height / 2.0F;
			};
		}
	}
	
	public static class RectangularPrism extends Shape {
		public RectangularPrism(float width, float height, float depth) {
			super(new Position(width / 2.0F, height / 2.0F, depth / 2.0F), new Position(-width / 2.0F, -height / 2.0F, -depth / 2.0F));
			
			this.equation = (shape, pos) -> {
				return (pos.getZ() - startPos.getZ()) > -width / 2.0F &&
						(pos.getZ() - startPos.getZ()) < width / 2.0F &&
						(pos.getY() - startPos.getY()) > -height / 2.0F &&
						(pos.getY() - startPos.getY()) < height / 2.0F &&
						(pos.getZ() - startPos.getZ()) > -depth / 2.0F &&
						(pos.getZ() - startPos.getZ()) < depth / 2.0F;
			};
		}
	}
	
	public static class TriangularPrism extends Shape {
		public TriangularPrism(float width, float height, float depth) {
			super(new Position(width / 2.0F, height / 2.0F, depth / 2.0F), new Position(-width / 2.0F, -height / 2.0F, -depth / 2.0F));
			
			this.equation = (shape, pos) -> {
				return (pos.getZ() - startPos.getZ()) > -(width / 2.0F) &&
						(pos.getZ() - startPos.getZ()) < (width / 2.0F) &&
						(pos.getY() - startPos.getY()) > -(height / 2.0F) &&
						(pos.getY() - startPos.getY()) < (height / 2.0F) &&
						(pos.getZ() - startPos.getZ()) > -(depth * (1.0F - (((pos.getY() - startPos.getY()) + height / 2.0F) / height))) / 2.0F &&
						(pos.getZ() - startPos.getZ()) < (depth * (1.0F - (((pos.getY() - startPos.getY()) + height / 2.0F) / height))) / 2.0F;
			};
		}
	}
	
	public static class RectangularPyramid extends Shape {
		public RectangularPyramid(float width, float height, float depth) {
			super(new Position(width / 2.0F, height, depth / 2.0F), new Position(-width / 2.0F, 0.0F, -depth / 2.0F));
			
			this.equation = (shape, pos) -> {
				return (pos.getZ() - startPos.getZ()) > -(width * (1.0F - ((pos.getY() - startPos.getY()) / height))) / 2.0F &&
						(pos.getZ() - startPos.getZ()) < (width * (1.0F - ((pos.getY() - startPos.getY()) / height))) / 2.0F &&
						(pos.getY() - startPos.getY()) > 0.0F && (pos.getY() - startPos.getY()) < height &&
						(pos.getZ() - startPos.getZ()) > -(depth * (1.0F - ((pos.getY() - startPos.getY()) / height))) / 2.0F &&
						(pos.getZ() - startPos.getZ()) < (depth * (1.0F - ((pos.getY() - startPos.getY()) / height))) / 2.0F;
			};
		}
	}
	
	public static class EllipticalPyramid extends Shape {
		public EllipticalPyramid(float a, float b, float height) {
			super(new Position(a, height, b), new Position(-a, 0.0F, -b));
			
			this.equation = (shape, pos) -> {
				return (Math.pow(pos.getZ() - startPos.getZ(), 2.0D) / Math.pow(a * (1.0F - ((pos.getY() - startPos.getY()) / height)), 2.0D)) +
						(Math.pow(pos.getZ() - startPos.getZ(), 2.0D) / Math.pow(b * (1.0F - ((pos.getY() - startPos.getY()) / height)), 2.0D)) < 1.0F &&
						(pos.getY() - startPos.getY()) > 0.0F &&
						(pos.getY() - startPos.getY()) < height;
			};
		}
	}
	
	public static class Ellipsoid extends Shape {
		public Ellipsoid(float a, float b, float c) {
			super(new Position(a, c, b), new Position(-a, -c, -b));
			
			this.equation = (shape, pos) -> {
				return (Math.pow(pos.getZ() - startPos.getZ(), 2.0D) / Math.pow(a, 2.0D)) +
						(Math.pow(pos.getY() - startPos.getY(), 2.0D) / Math.pow(b, 2.0D)) +
						(Math.pow(pos.getZ() - startPos.getZ(), 2.0D) / Math.pow(c, 2.0D)) < 1.0F;
			};
		}
	}
	
	public static class HemiEllipsoid extends Shape {
		public HemiEllipsoid(float a, float b, float c) {
			super(new Position(a, c, b), new Position(-a, 0.0F, -b));
			
			this.equation = (shape, pos) -> {
				return (Math.pow(pos.getZ() - startPos.getZ(), 2.0D) / Math.pow(a, 2.0D)) +
						(Math.pow(pos.getZ() - startPos.getZ(), 2.0D) / Math.pow(b, 2.0D)) +
						(Math.pow(pos.getY() - startPos.getY(), 2.0D) / Math.pow(c, 2.0D)) > 0.0F;
			};
		}
	}
}
