package dev.vini2003.hammer.gui.common.widget.bar

import dev.vini2003.hammer.H
import dev.vini2003.hammer.gui.common.widget.Widget
import dev.vini2003.hammer.client.texture.Texture

abstract class BarWidget(
	var maximum: () -> Float,
	var current: () -> Float
) : Widget() {
	companion object {
		@JvmField
		val STANDARD_FOREGROUND_TEXTURE: Texture = Texture.of(
			H.id("textures/widget/bar_foreground.png"),
			18F,
			18F,
			0.05555555555555555556F,
			0.05555555555555555556F,
			0.05555555555555555556F,
			0.05555555555555555556F
		)
		
		@JvmField
		val STANDARD_BACKGROUND_TEXTURE: Texture = Texture.of(
			H.id("textures/widget/bar_background.png"),
			18F,
			18F,
			0.05555555555555555556F,
			0.05555555555555555556F,
			0.05555555555555555556F,
			0.05555555555555555556F
		)
	}
	
	abstract var foregroundTexture: Texture
	
	abstract var backgroundTexture: Texture
	
	var horizontal: Boolean = false
		set(value) {
			field = value
			if (vertical && value) {
				vertical = false
			}
		}
	
	var vertical: Boolean = true
		set(value) {
			field = value
			if (horizontal && value) {
				horizontal = false
			}
		}
}