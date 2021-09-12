package dev.vini2003.blade.common.util.extension

import dev.vini2003.blade.client.util.Instances
import dev.vini2003.blade.common.geometry.position.Position
import dev.vini2003.blade.common.geometry.size.Size
import dev.vini2003.blade.common.widget.AbstractWidget

fun AbstractWidget.position(position: Position) {
	if (parent == null) {
		this.position = position
	} else {
		position(parent!!.position, position)
	}
}

fun AbstractWidget.position(x: Float, y: Float) {
	if (parent == null) {
		this.position = Position.of(x, y)
	} else {
		position(parent!!.position, x, y)
	}
}

fun AbstractWidget.position(anchor: Position, position: Position) {
	this.position = Position.of(anchor, position)
}

fun AbstractWidget.position(anchor: Position, x: Float, y: Float) {
	this.position = Position.of(anchor, x, y)
}

fun AbstractWidget.center() {
	if (parent == null) {
		this.position = Position.of(Instances.client.window.scaledWidth / 2.0F - size.width / 2.0F, Instances.client.window.scaledHeight / 2.0F - size.height / 2.0F)
	} else {
		this.position = Position.of(parent!!.position.x + parent!!.width / 2F - width / 2F, parent!!.position.y + parent!!.height / 2F - height / 2F)
	}
}

fun AbstractWidget.centerHorizontally() {
	this.position = Position.of(parent!!.position.x + parent!!.width / 2F - width / 2F, y)
}

fun AbstractWidget.centerVertically() {
	this.position = Position.of(x, parent!!.position.y + parent!!.height / 2F - height / 2F)
}

fun AbstractWidget.size(size: Size) {
	this.size = size
}

fun AbstractWidget.size(width: Float, height: Float) {
	this.size = Size.of(width, height)
}

fun AbstractWidget.size(side: Float) {
	this.size = Size.of(side, side)
}