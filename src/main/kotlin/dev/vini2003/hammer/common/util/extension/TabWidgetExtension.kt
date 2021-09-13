package dev.vini2003.hammer.common.util.extension

import dev.vini2003.hammer.client.texture.PartitionedTexture
import dev.vini2003.hammer.common.widget.tab.TabWidget
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

fun TabWidget.tab(stack: ItemStack): TabWidget.TabWidgetCollection.() -> Unit {
	return { addTab(stack) }
}

fun TabWidget.tab(stack: ItemStack, tooltipBlock: () -> List<Text>): TabWidget.TabWidgetCollection.() -> Unit {
	return { addTab(stack, tooltipBlock) }
}