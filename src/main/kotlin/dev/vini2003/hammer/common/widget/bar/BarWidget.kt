package dev.vini2003.hammer.common.widget.bar

import dev.vini2003.hammer.H
import dev.vini2003.hammer.client.texture.Texture
import dev.vini2003.hammer.common.widget.Widget
import net.minecraft.util.Identifier

abstract class BarWidget(
	var maximum: () -> Float,
	var current: () -> Float
) : Widget() {
	companion object {
		val StandardForegroundTexture: Texture = Texture.of(
			H.id("textures/widget/bar_foreground.png"),
			18F,
			18F,
			0.05555555555555555556F,
			0.05555555555555555556F,
			0.05555555555555555556F,
			0.05555555555555555556F
		)
		
		val StandardBackgroundTexture: Texture = Texture.of(
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
	
	var orientation: Orientation = Orientation.Vertical
	
	enum class Orientation {
		Horizontal,
		Vertical
	}
}