package dev.vini2003.hammer.gui.api.common.widget.item;

import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.util.TextUtil;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.function.Supplier;

public class ItemWidget extends Widget {
	protected Supplier<Item> item = () -> Items.AIR;
	
	public ItemWidget() {
		super();
		
		setTooltipSupplier(() -> {
			var item = this.item.get();
			
			if (item == Items.AIR) {
				return ImmutableList.of(TextUtil.getEmpty());
			} else {
				var client = InstanceUtil.getClient();
				
				return new ItemStack(item).getTooltip(client.player, () -> client.options.advancedItemTooltips);
			}
		});
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		var itemRenderer = DrawingUtil.getItemRenderer();
		
		itemRenderer.renderInGui(new ItemStack(item.get()), (int) getX(), (int) getY());
	}
	
	public void setItem(Supplier<Item> itemSupplier) {
		this.item = itemSupplier;
	}
	
	public void setItem(Item item) {
		setItem(() -> item);
	}
}
