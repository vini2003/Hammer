package dev.vini2003.hammer.gui.api.common.widget.provider;

import net.minecraft.item.Item;

import java.util.function.Supplier;

public interface ItemProvider {
	Supplier<Item> getItem();
	
	void setItem(Supplier<Item> item);
	
	default void setItem(Item item) {
		setItem(() -> item);
	}
}
