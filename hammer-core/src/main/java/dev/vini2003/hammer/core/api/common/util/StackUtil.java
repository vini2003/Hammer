/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.core.api.common.util;

import net.minecraft.item.ItemStack;

import java.util.function.BiConsumer;

public class StackUtil {
	public static void merge(ItemStack source, ItemStack target, BiConsumer<ItemStack, ItemStack> consumer) {
		var targetMax = target.getMaxCount();
		
		// TODO: Check if this is equivalent to the 1.19.2 code.
		if (ItemStack.areItemsEqual(source, target) && ItemStack.areEqual(source, target)) {
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
