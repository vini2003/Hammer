package dev.vini2003.hammer.core.api.common.util;

import net.minecraft.item.ItemStack;

import java.util.function.BiConsumer;

public class StackUtil {
	public static void merge(ItemStack source, ItemStack target, BiConsumer<ItemStack, ItemStack> consumer) {
		var targetMax = target.getMaxCount();
		
		if (ItemStack.areItemsEqual(source, target) && ItemStack.areNbtEqual(source, target)) {
			var sourceCount = source.getCount();
			var targetCount = target.getCount();
			
			var targetAvailable = Math.max(0, targetMax - targetCount);
			
			target.increment(Math.min(sourceCount, targetAvailable));
			
			source.setCount(Math.max(sourceCount - targetAvailable, 0));
		} else {
			if (target.isEmpty() && !source.isEmpty()) {
				var sourceCount = source.getCount();
				var targetCount = target.getCount();
				
				var targetAvailable = targetMax - targetCount;
				
				target = source.copy();
				
				target.setCount(Math.min(sourceCount, targetAvailable));
				
				source.decrement(Math.min(sourceCount, targetAvailable));
			}
		}
		
		consumer.accept(source, target);
	}
}
