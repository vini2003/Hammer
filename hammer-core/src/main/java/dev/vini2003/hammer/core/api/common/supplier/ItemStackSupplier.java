package dev.vini2003.hammer.core.api.common.supplier;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

@FunctionalInterface
public interface ItemStackSupplier extends Supplier<ItemStack> {
}
