package dev.vini2003.blade.common.util.extension

import dev.vini2003.blade.client.texture.PartitionedTexture
import dev.vini2003.blade.common.widget.tab.TabWidget
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

fun TabWidget.tab(stack: ItemStack): TabWidget.WidgetCollection.() -> Unit {
	return { addTab(stack) }
}

fun TabWidget.tab(stack: ItemStack, tooltipBlock: () -> List<Text>): TabWidget.WidgetCollection.() -> Unit {
	return { addTab(stack, tooltipBlock) }
}

fun TabWidget.leftActiveTexture(leftActiveTexturelock: Identifier) {
	this.leftActiveTexture = leftActiveTexturelock
}

fun TabWidget.middleActiveTexture(middleActiveTexture: Identifier) {
	this.middleActiveTexture = middleActiveTexture
}

fun TabWidget.rightActiveTexture(rightActiveTexture: Identifier) {
	this.rightActiveTexture = rightActiveTexture
}

fun TabWidget.leftInactiveTexture(leftInactiveTexturelock: Identifier) {
	this.leftInactiveTexture = leftInactiveTexturelock
}

fun TabWidget.middleInactiveTexture(middleInactiveTexture: Identifier) {
	this.middleInactiveTexture = middleInactiveTexture
}

fun TabWidget.rightInactiveTexture(rightInactiveTexture: Identifier) {
	this.rightInactiveTexture = rightInactiveTexture
}

fun TabWidget.panelTexture(panelTexture: PartitionedTexture) {
	this.panelTexture = panelTexture
}