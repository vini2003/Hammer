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
		setPosition(Position.of(x, y, z));
	}
	
	
	
	/**
	 * Sets this object's position.
	 * @param anchor the new position's anchor.
	 * @param relativeX the new position's relative X.
	 * @param relativeY the new position's relative Y.
	 * @param relativeZ the new position's relative Z.
	 */
	default void setPosition(PositionHolder anchor, float relativeX, float relativeY, float relativeZ) {
		setPosition(new StaticPosition(anchor, relativeX, relativeY, relativeZ));
	}
	
	/**
	 * Sets this object's position.
	 * @param x the new position's X.
	 * @param y the new position's Y.
	 */
	default void setPosition(float x, float y) {
		setPosition(new StaticPosition(x, y));
	}
	
	/**
	 * Sets this object's position.
	 * @param anchor the new position's anchor.
	 * @param relativeX the new position's relative X.
	 * @param relativeY the new position's relative Y.
	 */
	default void setPosition(PositionHolder anchor, float relativeX, float relativeY) {
		setPosition(new StaticPosition(anchor, relativeX, relativeY));
	}
	
	/**
	 * Sets this object's position.
	 * @param anchor the new position's anchor.
	 * @param relativePosition the new position's relative position.
	 */
	default void setPosition(PositionHolder anchor, StaticPosition relativePosition) {
		setPosition(new StaticPosition(anchor, relativePosition));
	}
	
	/**
	 * Sets this object's position using static or dynamic coordinates.
	 * @param xSupplier the supplier for the new position's X coordinate.
	 * @param ySupplier the supplier for the new position's Y coordinate.
	 * @param zSupplier the supplier for the new position's Z coordinate.
	 */
	default void setPosition(FloatSupplier xSupplier, FloatSupplier ySupplier, FloatSupplier zSupplier) {
		setPosition(new DynamicPosition(xSupplier, ySupplier, zSupplier));
	}
	
	/**
	 * Sets this object's position relative to an anchor with dynamic or static offsets.
	 * @param anchor the new position's anchor.
	 * @param xSupplier the supplier for the new position's relative X coordinate.
	 * @param ySupplier the supplier for the new position's relative Y coordinate.
	 * @param zSupplier the supplier for the new position's relative Z coordinate.
	 */
	default void setPosition(PositionHolder anchor, FloatSupplier xSupplier, FloatSupplier ySupplier, FloatSupplier zSupplier) {
		setPosition(new DynamicPosition(anchor, xSupplier, ySupplier, zSupplier));
	}
	
	/**
	 * Sets this object's position using static or dynamic coordinates, assuming Z as 0.
	 * @param xSupplier the supplier for the new position's X coordinate.
	 * @param ySupplier the supplier for the new position's Y coordinate.
	 */
	default void setPosition(FloatSupplier xSupplier, FloatSupplier ySupplier) {
		setPosition(new DynamicPosition(xSupplier, ySupplier, () -> 0.0F));
	}
	
	/**
	 * Sets this object's position relative to an anchor with dynamic or static offsets,
	 * assuming Z as the anchor's Z.
	 * @param anchor the new position's anchor.
	 * @param xSupplier the supplier for the new position's relative X coordinate.
	 * @param ySupplier the supplier for the new position's relative Y coordinate.
	 */
	default void setPosition(PositionHolder anchor, FloatSupplier xSupplier, FloatSupplier ySupplier) {
		setPosition(new DynamicPosition(anchor, xSupplier, ySupplier));
	}
	
	/**
	 * Sets this object's X.
	 * @param x the new X.
	 */
	default void setX(float x) {
		setPosition(new StaticPosition(x, getX(), getZ()));
	}
	
	/**
	 * Sets this object's Y.
	 * @param y the new Y.
	 */
	default void setY(float y) {
		setPosition(new StaticPosition(getX(), y, getZ()));
	}
	
	/**
	 * Sets this object's Z.
	 * @param z the new Z.
	 */
	default void setZ(float z) {
		setPosition(new StaticPosition(getX(), getY(), z));
	}
	
	/**
	 * Sets this object's X using a supplier.
	 * @param xSupplier the supplier for the new X coordinate.
	 */
	default void setX(FloatSupplier xSupplier) {
		setPosition(xSupplier, getPosition()::getY, getPosition()::getZ);
	}
	
	/**
	 * Sets this object's Y using a supplier.
	 * @param ySupplier the supplier for the new Y coordinate.
	 */
	default void setY(FloatSupplier ySupplier) {
		setPosition(getPosition()::getX, ySupplier, getPosition()::getZ);
	}
	
	/**
	 * Sets this object's Z using a supplier.
	 * @param zSupplier the supplier for the new Z coordinate.
	 */
	default void setZ(FloatSupplier zSupplier) {
		setPosition(getPosition()::getX, getPosition()::getY, zSupplier);
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
