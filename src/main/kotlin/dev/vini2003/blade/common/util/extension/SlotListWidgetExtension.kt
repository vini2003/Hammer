package dev.vini2003.blade.common.util.extension

import dev.vini2003.blade.client.texture.PartitionedTexture
import dev.vini2003.blade.common.widget.list.slot.SlotListWidget

fun SlotListWidget.scrollbarTexture(scrollbarTexture: PartitionedTexture) {
	this.scrollbarTexture = scrollbarTexture
}

fun SlotListWidget.scrollerTexture(scrollerTexture: PartitionedTexture) {
	this.scrollerTexture = scrollerTexture
}

fun SlotListWidget.scrollerFocusTexture(scrollerFocusTexture: PartitionedTexture) {
	this.scrollerFocusTexture = scrollerFocusTexture
}