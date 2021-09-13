package dev.vini2003.hammer.client.util.extension

import dev.vini2003.hammer.client.util.Drawings
import net.minecraft.text.Text

val Text.width
	get() = Drawings.textRenderer.getWidth(this)

val Text.height
	get() = Drawings.textRenderer.fontHeight