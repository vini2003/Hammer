package dev.vini2003.hammer.client.util

import dev.vini2003.hammer.client.util.extension.height
import dev.vini2003.hammer.client.util.extension.width
import net.minecraft.text.Text

object TextUtils {
	@JvmStatic
	fun width(text: Text) = text.width
	
	@JvmStatic
	fun height(text: Text) = text.height
}