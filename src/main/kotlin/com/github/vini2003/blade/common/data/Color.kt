package com.github.vini2003.blade.common.data

class Color(val r: Float, val g: Float, val b: Float, val a: Float) {
	companion object {
		@JvmStatic
		fun of(string: String): Color {
			val a = Integer.decode("0x" + string.substring(2, 4)) / 255F
			val r = Integer.decode("0x" + string.substring(4, 6)) / 255F
			val g = Integer.decode("0x" + string.substring(6, 8)) / 255F
			val b = Integer.decode("0x" + string.substring(8, 10)) / 255F

			return Color(r, g, b, a)
		}

		@JvmStatic
		fun of(color: Int): Color {
			return Color((color shr 24 and 0xFF) / 255F, (color shr 16 and 0xFF) / 255F, (color shr 8 and 0xFF) / 255F, (color and 0xFF) / 255F)
		}

		@JvmStatic
		fun standard(): Color {
			return Color(1F, 1F, 1F, 1F)
		}

		@JvmStatic
		fun redGreenBlue(r: Int, g: Int, b: Int, a: Int): Int {
			var i = (r shl 8) + g
			i = (i shl 8) + b
			return i
		}
	}

	fun toInt(): Int {
		return redGreenBlue((r * 255F).toInt(), (g * 255F).toInt(), (b * 255F).toInt(), (a * 255F).toInt())
	}
}