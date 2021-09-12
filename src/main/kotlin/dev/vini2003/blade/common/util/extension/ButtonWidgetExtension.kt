package dev.vini2003.blade.common.util.extension

import dev.vini2003.blade.client.texture.PartitionedTexture
import dev.vini2003.blade.common.widget.button.ButtonWidget
import net.minecraft.text.LiteralText
import net.minecraft.text.Text

fun ButtonWidget.click(clickAction: () -> Unit) {
	this.clickAction = clickAction
}

fun ButtonWidget.label(label: Text) {
	this.label = label
}

fun ButtonWidget.label(label: String) {
	this.label = LiteralText(label)
}

fun ButtonWidget.disabled(block: () -> Boolean) {
	this.disabled = block
}

fun ButtonWidget.textureOn(textureOn: PartitionedTexture) {
	this.textureOn = textureOn
}

fun ButtonWidget.textureOff(textureOff: PartitionedTexture) {
	this.textureOff = textureOff
}

fun ButtonWidget.textureFocus(textureFocus: PartitionedTexture) {
	this.textureFocus = textureFocus
}