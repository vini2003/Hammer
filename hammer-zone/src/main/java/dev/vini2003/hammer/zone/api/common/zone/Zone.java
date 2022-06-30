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

package dev.vini2003.hammer.zone.api.common.zone;

import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.Objects;

public class Zone {
	private Identifier id;
	
	private Position minPos;
	private Position maxPos;
	
	private Position lerpedMinPos;
	private Position lerpedMaxPos;
	
	private Color color;
	
	private ZoneGroup group;
	
	private boolean removed = false;
	
	public Zone(@Nullable ZoneGroup group, Identifier id, Position startPos, Position endPos, Color color) {
		this.id = id;
		
		this.minPos = Position.min(startPos, endPos);
		this.maxPos = Position.max(startPos, endPos);
		
		this.color = color;
		
		this.lerpedMinPos = getCenterPos();
		this.lerpedMaxPos = getCenterPos();
	}
	
	public Identifier getId() {
		return id;
	}
	
	public Position getMinPos() {
		return minPos;
	}
	
	public void setMinPos(Position minPos) {
		this.minPos = Position.min(maxPos, minPos);
		this.maxPos = Position.max(maxPos, minPos);
	}
	
	public Position getMaxPos() {
		return maxPos;
	}
	
	public void setMaxPos(Position maxPos) {
		this.minPos = Position.min(maxPos, minPos);
		this.maxPos = Position.max(maxPos, minPos);
	}
	
	public Position getCenterPos() {
		return new Position(
				(minPos.getX() + maxPos.getX()) / 2,
				(minPos.getY() + maxPos.getY()) / 2,
				(minPos.getZ() + maxPos.getZ()) / 2
		);
	}
	
	public Position getLerpedMinPos(float tickDelta) {
		lerpedMinPos = new Position(
				MathHelper.lerp(tickDelta, lerpedMinPos.getX(), minPos.getX()),
				MathHelper.lerp(tickDelta, lerpedMinPos.getY(), minPos.getY()),
				MathHelper.lerp(tickDelta, lerpedMinPos.getZ(), minPos.getZ())
		);
		
		return lerpedMinPos;
	}
	
	public Position getLerpedMaxPos(float tickDelta) {
		lerpedMaxPos = new Position(
				MathHelper.lerp(tickDelta, lerpedMaxPos.getX(), maxPos.getX()),
				MathHelper.lerp(tickDelta, lerpedMaxPos.getY(), maxPos.getY()),
				MathHelper.lerp(tickDelta, lerpedMaxPos.getZ(), maxPos.getZ())
		);
		
		return lerpedMaxPos;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public ZoneGroup getGroup() {
		return group;
	}
	
	public void markRemoved() {
		this.removed = true;
		
		var centerPos = getCenterPos();
		
		this.setMinPos(centerPos);
		this.setMaxPos(centerPos);
	}
	
	public boolean isRemoved() {
		return removed;
	}
	
	public void setGroup(ZoneGroup group) {
		this.group = group;
	}
	
	public float getWidth() {
		return maxPos.getX() - minPos.getX();
	}
	
	public float getHeight() {
		return maxPos.getY() - minPos.getY();
	}
	
	public float getDepth() {
		return maxPos.getZ() - minPos.getZ();
	}
	
	/**
	 * Scales this zone by the given amount in all axis.
	 *
	 * @param amount the amount.
	 */
	public void scale(int amount) {
		minPos = minPos.offset(-amount, -amount, -amount);
		maxPos = maxPos.offset(amount, amount, amount);
	}
	
	/**
	 * Stretches this zone by the given amount in the specified axis.
	 *
	 * @param axis the axis.
	 * @param amount the amount.
	 */
	public void stretch(Direction axis, int amount) {
		switch (axis) {
			case EAST -> maxPos = maxPos.offset(amount, 0, 0);
			case WEST -> minPos = minPos.offset(-amount, 0, 0);
			case SOUTH -> maxPos = maxPos.offset(0, 0, amount);
			case NORTH -> minPos = minPos.offset(0, 0, -amount);
			case UP -> maxPos = maxPos.offset(0, amount, 0);
			case DOWN -> maxPos = maxPos.offset(0, -amount, 0);
			
		}
	}
	
	/**
	 * Moves this zone by the given offset in the specified axis.
	 *
	 * @param axis the axis.
	 * @param amount the amount.
	 */
	public void move(Direction axis, int amount) {
		switch (axis) {
			case EAST -> {
				minPos = minPos.offset(amount, 0, 0);
				maxPos = maxPos.offset(amount, 0, 0);
			}
			
			case WEST -> {
				minPos = minPos.offset(-amount, 0, 0);
				maxPos = maxPos.offset(-amount, 0, 0);
			}
			
			case SOUTH -> {
				minPos = minPos.offset(0, 0, amount);
				maxPos = maxPos.offset(0, 0, amount);
			}
			
			case NORTH -> {
				minPos = minPos.offset(0, 0, -amount);
				maxPos = maxPos.offset(0, 0, -amount);
			}
			
			case UP -> {
				minPos = minPos.offset(0, amount, 0);
				maxPos = maxPos.offset(0, amount, 0);
			}
			
			case DOWN -> {
				minPos = minPos.offset(0, -amount, 0);
				maxPos = maxPos.offset(0, -amount, 0);
			}
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (!(o instanceof Zone)) {
			return false;
		}
		
		var zone = (Zone) o;
		
		return Objects.equals(minPos, zone.minPos) && Objects.equals(maxPos, zone.maxPos);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(minPos, maxPos);
	}
}
