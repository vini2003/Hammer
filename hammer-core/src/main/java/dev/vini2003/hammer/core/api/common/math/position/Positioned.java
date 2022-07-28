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

import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.core.api.common.math.size.SizeHolder;

/**
 * A {@link Positioned} represents an object which has
 * a three-dimensional position, delegated to a {@link Positioned}.
 */
public interface Positioned extends PositionHolder {
	/**
	 * Returns this object's position.
	 * @return this object's position.
	 */
	Position getPosition();
	
	/**
	 * Sets this object's position.
	 * @param position the new position.
	 */
	void setPosition(Position position);
	
	/**
	 * Sets this object's position.
	 * @param x the new position's X.
	 * @param y the new position's Y.
	 * @param z the new position's Z.
	 */
	default void setPosition(float x, float y, float z) {
		setPosition(new Position(x, y, z));
	}
	
	/**
	 * Sets this object's position.
	 * @param x the new position's X.
	 * @param y the new position's Y.
	 */
	default void setPosition(float x, float y) {
		setPosition(new Position(x, y));
	}
	
	/**
	 * Sets this object's X.
	 * @param x the new X.
	 */
	default void setX(float x) {
		setPosition(new Position(x, getX(), getZ()));
	}
	
	/**
	 * Sets this object's Y.
	 * @param y the new Y.
	 */
	default void setY(float y) {
		setPosition(new Position(getX(), y, getZ()));
	}
	
	/**
	 * Sets this object's Z.
	 * @param z the new Z.
	 */
	default void setZ(float z) {
		setPosition(new Position(getX(), getY(), z));
	}
	
	@Override
	default float getX() {
		return getPosition().getX();
	}
	
	@Override
	default float getY() {
		return getPosition().getY();
	}
	
	@Override
	default float getZ() {
		return getPosition().getZ();
	}
}
