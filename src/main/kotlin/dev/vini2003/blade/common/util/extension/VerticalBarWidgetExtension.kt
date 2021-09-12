package dev.vini2003.blade.common.util.extension

import dev.vini2003.blade.client.texture.PartitionedTexture
import dev.vini2003.blade.client.texture.Texture
import dev.vini2003.blade.common.widget.bar.VerticalBarWidget
import dev.vini2003.blade.common.widget.bar.VerticalFluidBarWidget

fun VerticalBarWidget.maximum(maximum: () -> Float) {
	this.maximum = maximum
}

fun VerticalBarWidget.current(current: () -> Float) {
	this.current = current
}

fun VerticalBarWidget.backgroundTexture(backgroundTexture: Texture) {
	this.backgroundTexture = backgroundTexture
}

fun VerticalBarWidget.foregroundTexture(foregroundTexture: Texture) {
	this.foregroundTexture = foregroundTexture
}