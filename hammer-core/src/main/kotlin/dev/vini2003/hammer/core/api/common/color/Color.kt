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

package dev.vini2003.hammer.core.api.common.color

/**
 * A [Color] is an RGBA color, which contains red ([r]), green ([g]), blue ([b]) and alpha ([a]) components.
 */
class Color(
	/**
	 * Constructs a color.
	 *
	 * @param r the red percentage value.
	 * @param g the green percentage value.
	 * @param b the blue percentage value.
	 * @param a the alpha percentage value.
	 */
	val r: Float,
	val g: Float,
	val b: Float,
	val a: Float
) {
	/**
	 * Constructs a color.
	 *
	 * @param rgbaHex an RGBA hexadecimal string.
	 * @return the color.
	 */
	constructor(rgbaHex: String) : this(rgbaHex.toInt(16))
	
	/**
	 * Constructs a color.
	 *
	 * @param rgba an RGBA long.
	 * @return the color.
	 */
	constructor(rgba: Long) : this(((rgba shr 24) and 0xFF) / 255.0F, ((rgba shr 16) and 0xFF) / 255.0F, ((rgba shr 8) and 0xFF) / 255.0F, (rgba and 0xFF) / 255.0F)
	
	/**
	 * Constructs a color.
	 *
	 * @param rgb an RGB integer.
	 * @return the color.
	 */
	constructor(rgb: Int) : this(((rgb shr 16) and 0xFF) / 255.0F, ((rgb shr 8) and 0xFF) / 255.0F, ((rgb and 0xFF) / 255.0F), 1.0F)
	
	
	/**
	 * Returns this color as an RGB integer.
	 *
	 * @return the integer.
	 */
	fun toRGB(): Int {
		val r = (this.r * 255).toInt()
		val g = (this.g * 255).toInt()
		val b = (this.b * 255).toInt()
		
		var rgb = r
		
		rgb = (rgb shl 8) + g
		rgb = (rgb shl 8) + b
		
		return rgb
	}
	
	/**
	 * Returns this color as an RGBA long.
	 *
	 * @return the long.
	 */
	fun toRGBA(): Long {
		val r = (this.r * 255).toLong()
		val g = (this.g * 255).toLong()
		val b = (this.b * 255).toLong()
		val a = (this.b * 255).toLong()
		
		var rgba = r
		
		rgba = (rgba shl 8) + g
		rgba = (rgba shl 8) + b
		rgba = (rgba shl 8) + a
		
		return rgba
	}
	
	companion object {
		@JvmField
		val DARK_RED = Color(0.67F, 0.0F, 0.0F, 1.0F)
		
		@JvmField
		val RED = Color(1.0F, 0.33F, 0.33F, 1.0F)
		
		@JvmField
		val GOLD = Color(1.0F, 0.67F, 0.0F, 1.0F)
		
		@JvmField
		val YELLOW = Color(1.0F, 1.0F, 0.33F, 1.0F)
		
		@JvmField
		val DARK_GREEN = Color(0.0F, 0.67F, 0.0F, 1.0F)
		
		@JvmField
		val GREEN = Color(0.33F, 1.0F, 0.33F, 1.0F)
		
		@JvmField
		val AQUA = Color(0.33F, 1.0F, 1.0F, 1.0F)
		
		@JvmField
		val DARK_AQUA = Color(0.0F, 0.67F, 0.67F, 1.0F)
		
		@JvmField
		val DARK_BLUE = Color(0.0F, 0.0F, 0.67F, 1.0F)
		
		@JvmField
		val BLUE = Color(0.33F, 0.33F, 1.0F, 1.0F)
		
		@JvmField
		val LIGHT_PURPLE = Color(1.0F, 0.33F, 1.0F, 1.0F)
		
		@JvmField
		val DARK_PURPLE = Color(0.67F, 0.0F, 0.67F, 1.0F)
		
		@JvmField
		val WHITE = Color(1.0F, 1.0F, 1.0F, 1.0F)
		
		@JvmField
		val GRAY = Color(0.67F, 0.67F, 0.67F, 1.0F)
		
		@JvmField
		val DARK_GRAY = Color(0.33F, 0.33F, 0.33F, 1.0F)
		
		@JvmField
		val BLACK = Color(0.0F, 0.0F, 0.0F, 1.0F)
	}
}