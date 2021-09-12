package dev.vini2003.blade.common.util.extension

import dev.vini2003.blade.client.texture.PartitionedTexture
import dev.vini2003.blade.common.widget.list.ListWidget

fun ListWidget.scrollbarTexture(scrollbarTexture: PartitionedTexture) {
	this.scrollbarTexture = scrollbarTexture
}

fun ListWidget.scrollerTexture(scrollerTexture: PartitionedTexture) {
	this.scrollerTexture = scrollerTexture
}

fun ListWidget.scrollerFocusTexture(scrollerFocusTexture: PartitionedTexture) {
	this.scrollerFocusTexture = scrollerFocusTexture
}