@file:Suppress("DEPRECATION", "UnstableApiUsage")

package dev.vini2003.hammer.common.util

import dev.vini2003.hammer.common.util.extension.gray
import dev.vini2003.hammer.common.util.extension.toLiteralText
import dev.vini2003.hammer.common.util.extension.toTranslatableText

object TextUtils {
	@JvmField
	val EMPTY = "text.hammer.empty".toTranslatableText()
	
	@JvmStatic
	fun percentage(a: Number, b: Number) = "${(a.toFloat() / b.toFloat() * 100.0F).toInt()}%".toLiteralText().gray()
}