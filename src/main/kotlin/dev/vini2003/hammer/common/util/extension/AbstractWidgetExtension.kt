package dev.vini2003.hammer.common.util.extension

import dev.vini2003.hammer.client.util.Instances
import dev.vini2003.hammer.common.geometry.position.Position
import dev.vini2003.hammer.common.geometry.size.Size
import dev.vini2003.hammer.common.widget.Widget

fun Widget.center() {
	if (parent == null) {
		this.position = Position.of(Instances.client.window.scaledWidth / 2.0F - size.width / 2.0F, Instances.client.window.scaledHeight / 2.0F - size.height / 2.0F)
	} else {
		this.position = Position.of(parent!!.position.x + parent!!.width / 2F - width / 2F, parent!!.position.y + parent!!.height / 2F - height / 2F)
	}
}

fun Widget.centerHorizontally() {
	this.position = Position.of(parent!!.position.x + parent!!.width / 2F - width / 2F, y)
}

fun Widget.centerVertically() {
	this.position = Position.of(x, parent!!.position.y + parent!!.height / 2F - height / 2F)
}