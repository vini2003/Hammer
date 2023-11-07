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
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.provider.ItemStackProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Supplier;

public class ItemStackWidget extends Widget implements ItemStackProvider {
	public static final Size STANDARD_SIZE = Size.of(18.0F, 18.0F);
	
	protected Supplier<ItemStack> stack = () -> ItemStack.EMPTY;
	
	protected boolean initializedTooltip = false;
	
	public ItemStackWidget() {
		super();
	}
	
	@Environment(EnvType.CLIENT)
	protected List<OrderedText> getTooltipOnClient() {
		var stack = this.stack.get();
		
		if (stack.isEmpty()) {
			return ImmutableList.of(TextUtil.getEmpty().asOrderedText());
		} else {
			var client = InstanceUtil.getClient();
			
			return stack.getTooltip(client.player, client.options.advancedItemTooltips ? TooltipContext.ADVANCED : TooltipContext.BASIC).stream().map(Text::asOrderedText).toList();
		}
	}
	
	@Override
	public Size getStandardSize() {
		return STANDARD_SIZE;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void draw(DrawContext context, float tickDelta) {
		onBeginDraw(context, tickDelta);
		
		if (!initializedTooltip) {
			initializedTooltip = true;
			
			setTooltip(this::getTooltipOnClient);
		}
		
		context.drawItem(stack.get(), (int) getX(), (int) getY());
		
		onEndDraw(context, tickDelta);
	}
	
	@Override
	public Supplier<ItemStack> getItemStack() {
		return stack;
	}
	
	@Override
	public void setItemStack(Supplier<ItemStack> itemStack) {
		this.stack = itemStack;
	}
}
