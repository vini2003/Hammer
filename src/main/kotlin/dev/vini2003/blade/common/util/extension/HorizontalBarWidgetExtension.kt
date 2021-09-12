package dev.vini2003.blade.common.util.extension

import dev.vini2003.blade.client.texture.Texture
import dev.vini2003.blade.common.widget.bar.HorizontalBarWidget

fun HorizontalBarWidget.maximum(maximum: () -> Float) {
	this.maximum = maximum
}

fun HorizontalBarWidget.current(current: () -> Float) {
	this.current = current
}

fun HorizontalBarWidget.backgroundTexture(backgroundTexture: Texture) {
	this.backgroundTexture = backgroundTexture
}

fun HorizontalBarWidget.foregroundTexture(foregroundTexture: Texture) {
	this.foregroundTexture = foregroundTexture
}