package dev.vini2003.hammer.common.util

object NumberUtils {
	private val UNITS = arrayOf("k", "M", "G", "T", "P", "E", "Z", "Y")
	
	@JvmStatic
	fun shorten(value: Long, unit: String): String? {
		var value = value
		
		if (value < 1000) {
			return "$value $unit"
		}
		
		var exponent = 0
		
		while (value >= 1000) {
			value /= 1000
			
			++exponent
		}
		
		return String.format("%d%s", value, if (exponent - 1 > UNITS.size - 1) "∞" else UNITS[exponent - 1] + " $unit")
	}
}