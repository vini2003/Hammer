package dev.vini2003.blade.common.util.extension

import dev.vini2003.blade.client.texture.PartitionedTexture
import dev.vini2003.blade.common.widget.panel.PanelWidget

fun PanelWidget.texture(texture: PartitionedTexture) {
	this.texture = texture
}