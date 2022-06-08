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

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Quaternion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A {@link Position} is 3-dimensional coordinate, which contains X, Y and Z components.
 */
public class Position implements PositionHolder {
	private final float x;
	private final float y;
	private final float z;
	
	/**
	 * Returns a collection of all positions within the two given positions.
	 *
	 * @param startPosition the start position.
	 * @param endPosition   the end position.
	 *
	 * @return the collection.
	 */
	public static Collection<Position> collect(Position startPosition, Position endPosition) {
		var minPosition = new Position(Math.min(startPosition.x, endPosition.x), Math.min(startPosition.y, endPosition.y), Math.min(startPosition.z, endPosition.z));
		var maxPosition = new Position(Math.max(startPosition.x, endPosition.y), Math.max(startPosition.y, endPosition.y), Math.max(startPosition.z, endPosition.z));
		
		var x = (int) minPosition.x;
		var y = (int) minPosition.y;
		var z = (int) minPosition.z;
		
		var positions = new ArrayList<Position>();
		
		while (x < maxPosition.x) {
			while (y < maxPosition.y) {
				while (z < maxPosition.z) {
					positions.add(new Position(x, y, z));
					
					++z;
				}
				
				++y;
			}
			
			++x;
		}
		
		return positions;
	}
	
	/**
	 * Constructs a position.
	 *
	 * @param x the X position component.
	 * @param y the Y position component.
	 * @param z the Z position component.
	 *
	 * @return the position.
	 */
	public Position(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Constructs a position.
	 *
	 * @param x the X position component.
	 * @param y the Y position component.
	 *
	 * @return the position.
	 */
	public Position(float x, float y) {
		this.x = x;
		this.y = y;
		this.z = 0.0F;
	}
	
	/**
	 * Constructs an anchored position.
	 *
	 * @param anchor           the anchor.
	 * @param relativePosition the relative position.
	 *
	 * @return the position.
	 */
	public Position(PositionHolder anchor, Position relativePosition) {
		this(anchor.getX() + relativePosition.getX(), anchor.getY() + relativePosition.getY(), anchor.getZ() + relativePosition.getZ());
	}
	
	/**
	 * Constructs an anchored position.
	 *
	 * @param anchor    the anchor.
	 * @param relativeX the relative X component.
	 * @param relativeY the relative Y component.
	 * @param relativeZ the relative Z component.
	 *
	 * @return the position.
	 */
	public Position(PositionHolder anchor, float relativeX, float relativeY, float relativeZ) {
		this(anchor.getX() + relativeX, anchor.getY() + relativeY, anchor.getZ() + relativeZ);
	}
	
	/**
	 * Constructs an anchored position.
	 *
	 * @param anchor    the anchor.
	 * @param relativeX the relative X component.
	 * @param relativeY the relative Y component.
	 *
	 * @return the position.
	 */
	public Position(PositionHolder anchor, float relativeX, float relativeY) {
		this(anchor.getX() + relativeX, anchor.getY() + relativeY, anchor.getZ());
	}
	
	/**
	 * Constructs an anchor's position.
	 *
	 * @param anchor the anchor.
	 *
	 * @return the position.
	 */
	public Position(PositionHolder anchor) {
		this(anchor.getX(), anchor.getY(), anchor.getZ());
	}
	
	/**
	 * Converts a {@link BlockPos} to a position.
	 *
	 * @param blockPos the block pos.
	 *
	 * @return the position.
	 */
	public Position(BlockPos blockPos) {
		this(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
	
	/**
	 * Converts a [ChunkPos] to a position.
	 *
	 * @param chunkPos the chunk pos.
	 *
	 * @return the position.
	 */
	public Position(ChunkPos chunkPos) {
		this(chunkPos.x, 0.0F, chunkPos.z);
	}
	
	/**
	 * Converts this position to a [BlockPos].
	 *
	 * @return the block pos.
	 */
	public BlockPos toBlockPos() {
		return new BlockPos(x, y, z);
	}
	
	/**
	 * Converts this position to a [ChunkPos].
	 *
	 * @return the chunk pos.
	 */
	public ChunkPos toChunkPos() {
		return new ChunkPos((int) x, (int) z);
	}
	
	/**
	 * Returns the distance between this and the distant position.
	 *
	 * @param position the distant position.
	 *
	 * @return the distance.
	 */
	public float distanceTo(Position position) {
		return (float) Math.sqrt(Math.pow((x - position.x), 2.0D) + Math.pow((y - position.y), 2.0D) + Math.pow((z - position.z), 2.0D));
	}
	
	/**
	 * Returns the squared distance between this and the distant position.
	 *
	 * @param position the distant position.
	 *
	 * @return the distance.
	 */
	public float squaredDistanceTo(Position position) {
		return (float) (Math.pow((x - position.x), 2.0D) + Math.pow((y - position.y), 2.0D) + Math.pow((z - position.z), 2.0D));
	}
	
	/**
	 * Returns this position offset by X, Y and Z components.
	 *
	 * @param x the X component to offset this position by.
	 * @param y the Y component to offset this position by.
	 * @param z the Z component to offset this position by.
	 *
	 * @return the resulting position.
	 */
	public Position offset(float x, float y, float z) {
		return new Position(this.x + x, this.y + y, this.z + z);
	}
	
	/**
	 * Returns this position offset by X and Y components.
	 *
	 * @param x the X component to offset this position by.
	 * @param y the Y component to offset this position by.
	 *
	 * @return the resulting position.
	 */
	public Position offset(float x, float y) {
		return new Position(this.x + x, this.y + y, this.z);
	}
	
	/**
	 * Returns this position, adding another position to it.
	 *
	 * @param position the position.
	 *
	 * @return the resulting position.
	 */
	public Position plus(Position position) {
		return new Position(x + position.x, y + position.y, z + position.z);
	}
	
	/**
	 * Returns this position, subtracting another position from it.
	 *
	 * @param position the position.
	 *
	 * @return the resulting position.
	 */
	public Position minus(Position position) {
		return new Position(x + position.x, y + position.y, z + position.z);
	}
	
	/**
	 * Returns this position, multiplying its components by a given number.
	 *
	 * @param number the number.
	 *
	 * @return the resulting position.
	 */
	public Position times(float number) {
		return new Position(x * number, y * number, z * number);
	}
	
	/**
	 * Returns this position, dividing its components by a given number.
	 *
	 * @param number the number.
	 *
	 * @return the resulting position.
	 */
	public Position div(float number) {
		return new Position(x / number, y / number, z / number);
	}
	
	/**
	 * Returns this position, rotated by the given quaternion.
	 *
	 * @return the resulting position.
	 */
	public Position rotate(Quaternion quaternion) {
		var q1 = new Quaternion(quaternion);
		q1.hamiltonProduct(new Quaternion(this.getX(), this.getY(), this.getZ(), 0.0F));
		var q2 = new Quaternion(quaternion);
		q2.conjugate();
		q1.hamiltonProduct(q2);
		
		return new Position(q1.getX(), q1.getY(), q1.getZ());
	}
	
	@Override
	public float getX() {
		return x;
	}
	
	@Override
	public float getY() {
		return y;
	}
	
	@Override
	public float getZ() {
		return z;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (!(o instanceof Position)) {
			return false;
		}
		
		var position = (Position) o;
		
		return Float.compare(position.x, x) == 0 && Float.compare(position.y, y) == 0 && Float.compare(position.z, z) == 0;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}
}
