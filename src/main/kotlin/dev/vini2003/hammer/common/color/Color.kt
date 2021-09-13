package dev.vini2003.hammer.common.color

data class Color(val r: Float, val g: Float, val b: Float, val a: Float) {
	companion object {
		@JvmStatic
		fun of(string: String): Color {
			val chunked = string.chunked(2)
			
			val a = chunked[1].toInt(16) / 255F
			val r = chunked[2].toInt(16) / 255F
			val g = chunked[3].toInt(16) / 255F
			val b = chunked[4].toInt(16) / 255F
			
			return Color(r, g, b, a)
		}

		@JvmStatic
		fun of(color: Long): Color {
			val r = (color and -0x1000000 shr 24) / 255F
			val g = (color and 0x00FF0000 shr 16) / 255F
			val b = (color and 0x0000FF00 shr 8) / 255F
			val a = (color and 0x000000FF) / 255F

			return Color(r, g, b, a)
		}
		
		@JvmStatic
		fun of(color: Int): Color {
			val r = (color and 0x00FF0000 shr 16) / 255F
			val g = (color and 0x0000FF00 shr 8) / 255F
			val b = (color and 0x000000FF) / 255F
			
			return Color(r, g, b, 1.0F)
		}
		
		@JvmStatic
		val Standard = Color(1.0F, 1.0F, 1.0F, 1.0F)
	}

	fun toInt(): Int {
		val r = (this.r * 255).toInt() and 0xFF
		val g = (this.g * 255).toInt() and 0xFF
		val b = (this.b * 255).toInt() and 0xFF
		val a = (this.a * 255).toInt() and 0xFF

		return (r shl 24) + (g shl 16) + (b shl 8) + a
	}
}