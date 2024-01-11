package dev.vini2003.hammer.gui.api.common.widget.provider;

import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public interface ItemStackProvider {
	Supplier<ItemStack> getItemStack();
	
	void setItemStack(Supplier<ItemStack> itemStack);
	
	default void setItemStack(ItemStack itemStack) {
		setItemStack(() -> itemStack);
	}
}
