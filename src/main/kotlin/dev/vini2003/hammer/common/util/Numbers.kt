package dev.vini2003.hammer.common.util

object Numbers {
	private val Units = arrayOf("k", "M", "G", "T", "P", "E", "Z", "Y")
	
	fun shorten(value: Long, unit: String): String? {
		var value = value
		
		if (value < 1000) {
			return value.toString() + unit
		}
		
		var exponent = 0
		
		while (value >= 1000) {
			value /= 1000
			
			++exponent
		}
		
		return String.format("%d%s", value, if (exponent - 1 > Units.size - 1) "∞" else Units[exponent - 1] + " $unit")
	}
}