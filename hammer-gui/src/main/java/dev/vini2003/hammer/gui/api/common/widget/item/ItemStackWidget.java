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
				return ImmutableList.of(TextUtil.EMPTY);
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
