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

package dev.vini2003.hammer.core.api.common.util

import net.minecraft.item.ItemStack
import kotlin.math.max
import kotlin.math.min

object StackUtils {
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
		
		return consumer(source, target)
	}
}