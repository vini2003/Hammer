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
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.util.math.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * An {@link Position} represents a three-dimensional position
 * whose coordinates do not have a fixed storage medium.
 *
 * Implementations include {@link StaticPosition}, whose coordinates
 * are fixed, and {@link DynamicPosition}, whose coordinates are not.
 */
public abstract class Position implements PositionHolder {
	/**
	 * A factory for creating a StaticPosition with specified X, Y, and Z components.
	 *
	 * @param x the X coordinate of the position.
	 * @param y the Y coordinate of the position.
	 * @param z the Z coordinate of the position.
	 * @return a new StaticPosition instance with the specified coordinates.
	 */
	public static StaticPosition of(float x, float y, float z) {
		return new StaticPosition(x, y, z);
	}
	
	/**
	 * A factory for creating a StaticPosition with specified X and Y components, assuming Z as zero.
	 *
	 * @param x the X coordinate of the position.
	 * @param y the Y coordinate of the position.
	 * @return a new StaticPosition instance with the specified coordinates and Z set to zero.
	 */
	public static StaticPosition of(float x, float y) {
		return new StaticPosition(x, y);
	}
	
	/**
	 * A factory for creating a StaticPosition relative to an anchor with additional relative X, Y, and Z components.
	 *
	 * @param anchor      the position holder to serve as an anchor.
	 * @param relativeX   the relative X component to add to the anchor's X position.
	 * @param relativeY   the relative Y component to add to the anchor's Y position.
	 * @param relativeZ   the relative Z component to add to the anchor's Z position.
	 * @return a new StaticPosition instance offset from the anchor by the relative components.
	 */
	public static StaticPosition of(PositionHolder anchor, float relativeX, float relativeY, float relativeZ) {
		return new StaticPosition(anchor.getX() + relativeX, anchor.getY() + relativeY, anchor.getZ() + relativeZ);
	}
	
	/**
	 * A factory for creating a StaticPosition relative to an anchor with additional relative X and Y components.
	 *
	 * @param anchor      the position holder to serve as an anchor.
	 * @param relativeX   the relative X component to add to the anchor's X position.
	 * @param relativeY   the relative Y component to add to the anchor's Y position.
	 * @return a new StaticPosition instance offset from the anchor by the relative components with the same Z position as the anchor.
	 */
	public static StaticPosition of(PositionHolder anchor, float relativeX, float relativeY) {
		return new StaticPosition(anchor.getX() + relativeX, anchor.getY() + relativeY, anchor.getZ());
	}
	
	/**
	 * A factory for creating a StaticPosition with the same coordinates as the given position holder.
	 *
	 * @param anchor the position holder whose coordinates should be copied.
	 * @return a new StaticPosition instance with the same coordinates as the anchor.
	 */
	public static StaticPosition of(PositionHolder anchor) {
		return new StaticPosition(anchor.getX(), anchor.getY(), anchor.getZ());
	}
	
	/**
	 * A factory for creating a StaticPosition from a BlockPos.
	 *
	 * @param blockPos the BlockPos to convert.
	 * @return a new StaticPosition instance with coordinates corresponding to the BlockPos.
	 */
	public static StaticPosition of(BlockPos blockPos) {
		return new StaticPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
	
	/**
	 * A factory for creating a StaticPosition from a ChunkPos with Y set to zero.
	 *
	 * @param chunkPos the ChunkPos to convert.
	 * @return a new StaticPosition instance with coordinates corresponding to the ChunkPos and Y set to zero.
	 */
	public static StaticPosition of(ChunkPos chunkPos) {
		return new StaticPosition(chunkPos.x, 0.0F, chunkPos.z);
	}
	
	/**
	 * A factory for creating a StaticPosition from a Vec2f with Z set to zero.
	 *
	 * @param vec2f the Vec2f to convert.
	 * @return a new StaticPosition instance with coordinates corresponding to the Vec2f and Z set to zero.
	 */
	public static StaticPosition of(Vec2f vec2f) {
		return new StaticPosition(vec2f.x, vec2f.y);
	}
	
	/**
	 * A factory for creating a StaticPosition from a Vec3d.
	 *
	 * @param vec3d the Vec3d to convert.
	 * @return a new StaticPosition instance with coordinates corresponding to the Vec3d.
	 */
	public static StaticPosition of(Vec3d vec3d) {
		return new StaticPosition((float) vec3d.x, (float) vec3d.y, (float) vec3d.z);
	}
	
	/**
	 * A factory for creating a StaticPosition from a Vector3f.
	 *
	 * @param vec3f the Vector3f to convert.
	 * @return a new StaticPosition instance with coordinates corresponding to the Vector3f.
	 */
	public static StaticPosition of(Vec3f vec3f) {
		return new StaticPosition(vec3f.getX(), vec3f.getY(), vec3f.getZ());
	}
	
	/**
	 * A factory for creating a StaticPosition from a Vec3i.
	 *
	 * @param vec3i the Vec3i to convert.
	 * @return a new StaticPosition instance with coordinates corresponding to the Vec3i.
	 */
	public static StaticPosition of(Vec3i vec3i) {
		return new StaticPosition(vec3i.getX(), vec3i.getY(), vec3i.getZ());
	}
	
	/**
	 * A factory for creating a StaticPosition from a Vector3d.
	 *
	 * @param vector3d the Vector3d to convert.
	 * @return a new StaticPosition instance with coordinates corresponding to the Vector3d.
	 */
	public static StaticPosition of(Vector3d vector3d) {
		return new StaticPosition((float) vector3d.x, (float) vector3d.y, (float) vector3d.z);
	}
	
	/**
	 * A factory for creating a DynamicPosition with supplied functions for X, Y, and Z components.
	 *
	 * @param x the supplier for the X dynamic position component.
	 * @param y the supplier for the Y dynamic position component.
	 * @param z the supplier for the Z dynamic position component.
	 * @return a new DynamicPosition instance with supplied dynamic components.
	 */
	public static DynamicPosition of(FloatSupplier x, FloatSupplier y, FloatSupplier z) {
		return new DynamicPosition(x, y, z);
	}
	
	/**
	 * A factory for creating a DynamicPosition with supplied functions for X and Y components, assuming Z as zero.
	 *
	 * @param x the supplier for the X dynamic position component.
	 * @param y the supplier for the Y dynamic position component.
	 * @return a new DynamicPosition instance with supplied dynamic components and Z set to zero.
	 */
	public static DynamicPosition of(FloatSupplier x, FloatSupplier y) {
		return new DynamicPosition(x, y, () -> 0.0F);
	}
	
	/**
	 * Converts this position to a {@link BlockPos}.
	 *
	 * @return the block pos.
	 */
	public BlockPos toBlockPos() {
		return new BlockPos((int) getX(), (int) getY(), (int) getZ());
	}
	
	/**
	 * Converts this position to a {@link ChunkPos}
	 *
	 * @return the chunk pos.
	 */
	public ChunkPos toChunkPos() {
		return new ChunkPos((int) getX(), (int) getZ());
	}
	
	/**
	 * Converts this position to a {@link Vec2f}.
	 * @return The vector.
	 */
	public Vec2f toVec2f() {
		return new Vec2f(getX(), getY());
	}
	
	/**
	 * Converts this position to a {@link Vec3d}.
	 * @return The vector.
	 */
	public Vec3d toVec3d() {
		return new Vec3d(getX(), getY(), getZ());
	}
	
	/**
	 * Converts this position to a {@link Vec3f}.
	 * @return The vector.
	 */
	public Vec3f toVector3f() {
		return new Vec3f(getX(), getY(), getZ());
	}
	
	/**
	 * Converts this position to a {@link Vec3i}.
	 * @return The vector.
	 */
	public Vec3i toVec3i() {
		return new Vec3i((int) getX(), (int) getY(), (int) getZ());
	}
	
	/**
	 * Converts this position to a {@link Vector3d}.
	 * @return The vector.
	 */
	public Vector3d toVector3d() {
		return new Vector3d(getX(), getY(), getZ());
	}
	
	
	/**
	 * Returns this static position offset by X, Y and Z components.
	 *
	 * @param x the X component to offset this position by.
	 * @param y the Y component to offset this position by.
	 * @param z the Z component to offset this position by.
	 *
	 * @return the resulting position.
	 */
	public StaticPosition offset(float x, float y, float z) {
		return new StaticPosition(this.getX() + x, this.getY() + y, this.getZ() + z);
	}
	
	/**
	 * Returns this static position offset by X and Y components.
	 *
	 * @param x the X component to offset this position by.
	 * @param y the Y component to offset this position by.
	 *
	 * @return the resulting position.
	 */
	public StaticPosition offset(float x, float y) {
		return new StaticPosition(this.getX() + x, this.getY() + y, this.getZ());
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
	public DynamicPosition offset(FloatSupplier x, FloatSupplier y, FloatSupplier z) {
		return new DynamicPosition(() -> this.getX() + x.get(), () -> this.getY() + y.get(), () -> this.getZ() + z.get());
	}
	
	/**
	 * Returns this position offset by X and Y components.
	 *
	 * @param x the X component to offset this position by.
	 * @param y the Y component to offset this position by.
	 *
	 * @return the resulting position.
	 */
	public DynamicPosition offset(FloatSupplier x, FloatSupplier y) {
		return new DynamicPosition(() -> this.getX() + x.get(), () -> this.getY() + y.get(), this::getZ);
	}
	
	/**
	 * Returns the distance between this and the distant position.
	 *
	 * @param position the distant position.
	 *
	 * @return the distance.
	 */
	public float distanceTo(PositionHolder position) {
		return (float) Math.sqrt(Math.pow((getX() - position.getX()), 2.0D) + Math.pow((getY() - position.getY()), 2.0D) + Math.pow((getZ() - position.getZ()), 2.0D));
	}
	
	/**
	 * Returns the squared distance between this and the distant position.
	 *
	 * @param position the distant position.
	 *
	 * @return the distance.
	 */
	public float squaredDistanceTo(PositionHolder position) {
		return (float) (Math.pow((getX() - position.getX()), 2.0D) + Math.pow((getY() - position.getY()), 2.0D) + Math.pow((getZ() - position.getZ()), 2.0D));
	}
	
	/**
	 * Returns a collection of all positions within the two given positions.
	 *
	 * @param startPosition the start position.
	 * @param endPosition   the end position.
	 *
	 * @return the collection.
	 */
	public static Collection<StaticPosition> collect(PositionHolder startPosition, PositionHolder endPosition) {
		var minPosition = new StaticPosition(Math.min(startPosition.getX(), endPosition.getX()), Math.min(startPosition.getY(), endPosition.getY()), Math.min(startPosition.getZ(), endPosition.getZ()));
		var maxPosition = new StaticPosition(Math.max(startPosition.getX(), endPosition.getY()), Math.max(startPosition.getY(), endPosition.getY()), Math.max(startPosition.getZ(), endPosition.getZ()));
		
		var x = (int) minPosition.getX();
		var y = (int) minPosition.getY();
		var z = (int) minPosition.getZ();
		
		var positions = new ArrayList<StaticPosition>();
		
		while (x < maxPosition.getX()) {
			while (y < maxPosition.getY()) {
				while (z < maxPosition.getZ()) {
					positions.add(new StaticPosition(x, y, z));
					
					++z;
				}
				
				++y;
			}
			
			++x;
		}
		
		return positions;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (!(o instanceof StaticPosition)) {
			return false;
		}
		
		var position = (StaticPosition) o;
		
		return Float.compare(position.getX(), getX()) == 0 && Float.compare(position.getY(), getY()) == 0 && Float.compare(position.getZ(), getZ()) == 0;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getX(), getY(), getZ());
	}
}
