package dev.vini2003.blade.common.util

import net.minecraft.item.ItemStack
import kotlin.math.max
import kotlin.math.min

object Stacks {
	@JvmStatic
	fun merge(source: ItemStack, target: ItemStack, consumer: (ItemStack, ItemStack) -> Unit) {
		var target = target
		
		val targetMax = target.maxCount
		
		if (ItemStack.areItemsEqual(source, target) && ItemStack.areNbtEqual(source, target)) {
			val sourceCount = source.count
			val targetCount = target.count
			
			val targetAvailable = max(0, targetMax - targetCount)
			
			target.increment(min(sourceCount, targetAvailable))
			source.count = max(sourceCount - targetAvailable, 0)
		} else {
			if (target.isEmpty && !source.isEmpty) {
				val sourceCount = source.count
				val targetCount = target.count
				
				val targetAvailable = targetMax - targetCount
				
				target = source.copy()
				
				target.count = min(sourceCount, targetAvailable)
				source.decrement(min(sourceCount, targetAvailable))
			}
		}
		return consumer.invoke(source, target)
	}
	
	@JvmStatic
	fun equal(stackA: ItemStack, stackB: ItemStack): Boolean {
		return ItemStack.areEqual(stackA, stackB)
	}
}