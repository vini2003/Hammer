package dev.vini2003.blade.client.util.extension

import dev.vini2003.blade.client.util.Drawings
import dev.vini2003.blade.client.util.Instances
import net.minecraft.text.Text

val Text.width
	get() = Drawings.textRenderer.getWidth(this)

val Text.height
	get() = Drawings.textRenderer.fontHeight