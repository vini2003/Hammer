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

package dev.vini2003.hammer.border.api.util.extension

import dev.vini2003.hammer.border.impl.common.border.CubicWorldBorder
import net.minecraft.world.border.WorldBorder

val WorldBorder.centerY: Double
	get() = (this as CubicWorldBorder).centerY

val WorldBorder.boundUp: Double
	get() = (this as CubicWorldBorder).boundUp

val WorldBorder.boundDown: Double
	get() = (this as CubicWorldBorder).boundDown

fun WorldBorder.contains(x: Double, y: Double, z: Double): Boolean = (this as CubicWorldBorder).contains(x, y, z)

fun WorldBorder.contains(x: Double, y: Double, z: Double, margin: Double): Boolean = (this as CubicWorldBorder).contains(x, y, z, margin)

fun WorldBorder.setCenter(x: Double, y: Double, z: Double) = (this as CubicWorldBorder).setCenter(x, y, z)

fun WorldBorder.getDistanceInsideBorder(x: Double, y: Double, z: Double): Double = (this as CubicWorldBorder).getDistanceInsideBorder(x, y, z)