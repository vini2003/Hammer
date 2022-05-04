package dev.vini2003.hammer.gui.energy.common.util

import dev.vini2003.hammer.common.util.NumberUtils
import dev.vini2003.hammer.common.util.extension.gray
import dev.vini2003.hammer.common.util.extension.toLiteralText
import dev.vini2003.hammer.common.util.extension.toTranslatableText

object EnergyTextUtils {
	@JvmField
	val ENERGY = "text.hammer.energy".toTranslatableText()
	
	@JvmStatic
	fun detailedTooltip(a: Number, b: Number) = listOf(ENERGY, "$a E / $b E".toLiteralText())
	
	@JvmStatic
	fun shortenedTooltip(a: Number, b: Number) = listOf(ENERGY, "${NumberUtils.shorten(a.toLong(), "E")} / ${NumberUtils.shorten(b.toLong(), "E")}".toLiteralText().gray())
}