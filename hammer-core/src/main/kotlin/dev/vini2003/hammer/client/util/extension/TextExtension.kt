package dev.vini2003.hammer.client.util.extension

import dev.vini2003.hammer.client.util.DrawingUtils
import net.minecraft.text.Text

val Text.width
	get() = DrawingUtils.TEXT_RENDERER.getWidth(this)

val Text.height
	get() = DrawingUtils.TEXT_RENDERER.fontHeight