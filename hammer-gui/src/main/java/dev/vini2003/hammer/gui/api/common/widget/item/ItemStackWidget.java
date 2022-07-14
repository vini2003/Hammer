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

package dev.vini2003.hammer.gui.api.common.widget.item;

import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class ItemStackWidget extends Widget {
	protected Supplier<ItemStack> stack = () -> ItemStack.EMPTY;
	
	public ItemStackWidget() {
		super();
		
		setTooltipSupplier(() -> {
			var stack = this.stack.get();
			
			if (stack.isEmpty()) {
				return ImmutableList.of(TextUtil.getEmpty());
			} else {
				var client = InstanceUtil.getClient();
				
				return stack.getTooltip(client.player, () -> client.options.advancedItemTooltips);
			}
		});
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		var itemRenderer = DrawingUtil.getItemRenderer();
		
		itemRenderer.renderInGui(stack.get(), (int) getX(), (int) getY());
	}
	
	public void setStack(Supplier<ItemStack> stackSupplier) {
		this.stack = stackSupplier;
	}
	
	public void setStack(ItemStack stack) {
		setStack(() -> stack);
	}
}