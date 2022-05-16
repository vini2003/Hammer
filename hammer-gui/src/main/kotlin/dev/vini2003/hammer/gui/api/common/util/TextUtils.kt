package dev.vini2003.hammer.gui.api.common.util

import net.minecraft.text.LiteralText
import net.minecraft.text.Text

object TextUtils {
	fun getRatio(progress: Int, limit: Int): Text {
		return LiteralText((progress.toFloat() / limit.toFloat() * 100.0F).toInt().toString() + "%")
	}
}