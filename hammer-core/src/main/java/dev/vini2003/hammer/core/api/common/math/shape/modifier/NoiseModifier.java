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
import dev.vini2003.hammer.core.api.common.math.position.StaticPosition;
import dev.vini2003.hammer.core.api.common.math.shape.Shape;

import java.util.Random;

/**
 * A {@link NoiseModifier} is a {@link Shape} modifier that applies
 * a random noise-based distortion to blocks inside the shape.
 */
public class NoiseModifier implements Modifier {
	private final Random random;
	
	private final float magnitude;
	
	/**
	 * Constructs a noise modifier.
	 * @param random the random to use.
	 * @param magnitude the magnitude of noise to use.
	 * @return the modifier.
	 */
	public NoiseModifier(Random random, float magnitude) {
		this.random = random;
		this.magnitude = magnitude;
	}
	
	/**
	 * Modifiers the position to apply noise-based distortion.
	 * @param pos the position to modify.
	 * @return the modified position.
	 */
	@Override
	public StaticPosition modifyEquation(StaticPosition pos) {
		return Position.of(
				pos.getX() + (random.nextFloat() * magnitude) * (random.nextFloat() * magnitude),
				pos.getY() + (random.nextFloat() * magnitude) * (random.nextFloat() * magnitude),
				pos.getZ() + (random.nextFloat() * magnitude) * (random.nextFloat() * magnitude)
		);
	}
}
