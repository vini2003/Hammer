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
import dev.vini2003.hammer.core.api.common.math.shape.Shape;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.Random;

public class RotateModifier implements Modifier {
	private final Quaternion rotation;
	
	public RotateModifier(Quaternion rotation) {
		this.rotation = rotation;
	}
	
	@Override
	public Position modifyStartPos(Shape shape) {
		var minPos = new Position(Float.MAX_VALUE, Float.MAX_VALUE);
		
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
	
	@Override
	public Position modifyEndPos(Shape shape) {
		var maxPos = new Position(Float.MIN_VALUE, Float.MIN_VALUE);
		
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
	
	@Override
	public Position modifyEquation(Position pos) {
		return pos.rotate(rotation);
	}
}
