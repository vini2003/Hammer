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

package dev.vini2003.hammer.core.api.common.math.position;

import dev.vini2003.hammer.core.api.common.supplier.FloatSupplier;
import net.minecraft.util.math.Quaternion;

import java.util.function.Function;

/**
 * <p>A {@link DynamicPosition} represents a three-dimensional position
 * whose coordinates are dynamically calculated at every call.
 */
public class DynamicPosition extends Position {
	private final FloatSupplier xSupplier;
	private final FloatSupplier ySupplier;
	private final FloatSupplier zSupplier;
	
	/**
	 * Constructs a dynamic position.
	 *
	 * @param x the X position component supplier.
	 * @param y the Y position component supplier.
	 * @param z the Z position component supplier.
	 */
	public DynamicPosition(FloatSupplier x, FloatSupplier y, FloatSupplier z) {
		this.xSupplier = x;
		this.ySupplier = y;
		this.zSupplier = z;
	}
	
	/**
	 * Constructs a dynamic position.
	 *
	 * @param x the X position component.
	 * @param y the Y position component.
	 */
	public DynamicPosition(FloatSupplier x, FloatSupplier y) {
		this.xSupplier = x;
		this.ySupplier = y;
		this.zSupplier = () -> 0.0F;
	}
	
	/**
	 * Constructs an anchored dynamic position.
	 *
	 * @param anchor           the anchor.
	 * @param relativePosition the relative position.
	 */
	public DynamicPosition(PositionHolder anchor, PositionHolder relativePosition) {
		this(() -> anchor.getX() + relativePosition.getX(), () -> anchor.getY() + relativePosition.getY(), () -> anchor.getZ() + relativePosition.getZ());
	}
	
	/**
	 * Constructs an anchored dynamic position.
	 *
	 * @param anchor    the anchor.
	 * @param relativeX the relative X component.
	 * @param relativeY the relative Y component.
	 * @param relativeZ the relative Z component.
	 */
	public DynamicPosition(PositionHolder anchor, FloatSupplier relativeX, FloatSupplier relativeY, FloatSupplier relativeZ) {
		this(() -> anchor.getX() + relativeX.get(), () -> anchor.getY() + relativeY.get(), () -> anchor.getZ() + relativeZ.get());
	}
	
	/**
	 * Constructs an anchored dynamic position.
	 *
	 * @param anchor    the anchor.
	 * @param relativeX the relative X component.
	 * @param relativeY the relative Y component.
	 */
	public DynamicPosition(PositionHolder anchor, FloatSupplier relativeX, FloatSupplier relativeY) {
		this(() -> anchor.getX() + relativeX.get(), () -> anchor.getY() + relativeY.get(), anchor::getZ);
	}
	
	/**
	 * Returns this position, adding another position to it.
	 *
	 * @param position the position.
	 *
	 * @return the resulting position.
	 */
	public DynamicPosition plus(PositionHolder position) {
		return new DynamicPosition(() -> getX() + position.getX(), () -> getY() + position.getY(), () -> getZ() + position.getZ());
	}
	
	/**
	 * Returns this position, subtracting another position from it.
	 *
	 * @param position the position.
	 *
	 * @return the resulting position.
	 */
	public DynamicPosition minus(PositionHolder position) {
		return new DynamicPosition(() -> getX() + position.getX(), () -> getY() + position.getY(), () -> getZ() + position.getZ());
	}
	
	/**
	 * Returns this position, multiplying its components by a given number.
	 *
	 * @param number the number.
	 *
	 * @return the resulting position.
	 */
	public DynamicPosition times(FloatSupplier number) {
		return new DynamicPosition(() -> getX() * number.get(), () -> getY() * number.get(), () -> getZ() * number.get());
	}
	
	/**
	 * Returns this position, dividing its components by a given number.
	 *
	 * @param number the number.
	 *
	 * @return the resulting position.
	 */
	public DynamicPosition div(FloatSupplier number) {
		return new DynamicPosition(() -> getX() / number.get(), () -> getY() / number.get(), () -> getZ() / number.get());
	}
	
	/**
	 * Returns this position, rotated by the given quaternion.
	 *
	 * @return the resulting position.
	 */
	public DynamicPosition rotate(Quaternion quaternion) {
		// TODO: Check if this works.
		return new DynamicPosition(
				() -> {
					var q1 = new Quaternion(quaternion);
					q1.hamiltonProduct(new Quaternion(this.getX(), this.getY(), this.getZ(), 0.0F));
					var q2 = new Quaternion(quaternion);
					q2.conjugate();
					q1.hamiltonProduct(q2);
					
					return q1.getX();
				},
				() -> {
					var q1 = new Quaternion(quaternion);
					q1.hamiltonProduct(new Quaternion(this.getX(), this.getY(), this.getZ(), 0.0F));
					var q2 = new Quaternion(quaternion);
					q2.conjugate();
					q1.hamiltonProduct(q2);
					
					return q1.getY();
				},
				() -> {
					var q1 = new Quaternion(quaternion);
					q1.hamiltonProduct(new Quaternion(this.getX(), this.getY(), this.getZ(), 0.0F));
					var q2 = new Quaternion(quaternion);
					q2.conjugate();
					q1.hamiltonProduct(q2);
					
					return q1.getZ();
				}
		);
	}
	
	/**
	 * Returns the maximum position by the given function.
	 *
	 * @return the maximum position.
	 */
	public static DynamicPosition maxBy(DynamicPosition a, DynamicPosition b, Function<DynamicPosition, Float> function) {
		var resA = function.apply(a);
		var resB = function.apply(b);
		
		if (resA >= resB) {
			return a;
		} else {
			return b;
		}
	}
	
	/**
	 * Returns the minimum position by the given function.
	 *
	 * @return the minimum position.
	 */
	public static DynamicPosition minBy(DynamicPosition a, DynamicPosition b, Function<DynamicPosition, Float> function) {
		var resA = function.apply(a);
		var resB = function.apply(b);
		
		if (resA <= resB) {
			return a;
		} else {
			return b;
		}
	}
	
	/**
	 * Returns the maximum position.
	 *
	 * @return the maximum position.
	 */
	public static DynamicPosition max(DynamicPosition a, DynamicPosition b) {
		return new DynamicPosition(() -> Math.max(a.getX(), b.getX()), () -> Math.max(a.getY(), b.getY()), () -> Math.max(a.getZ(), b.getZ()));
	}
	
	/**
	 * Returns the minimum position.
	 *
	 * @return the minimum position.
	 */
	public static DynamicPosition min(DynamicPosition a, DynamicPosition b) {
		return new DynamicPosition(() -> Math.min(a.getX(), b.getX()), () -> Math.min(a.getY(), b.getY()), () -> Math.min(a.getZ(), b.getZ()));
	}
	
	@Override
	public float getX() {
		return xSupplier.get();
	}
	
	@Override
	public float getY() {
		return ySupplier.get();
	}
	
	@Override
	public float getZ() {
		return zSupplier.get();
	}
}
