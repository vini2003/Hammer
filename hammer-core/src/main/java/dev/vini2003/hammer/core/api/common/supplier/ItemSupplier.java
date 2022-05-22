package dev.vini2003.hammer.core.api.common.supplier;

import net.minecraft.item.Item;

import java.util.function.Supplier;

@FunctionalInterface
public interface ItemSupplier extends Supplier<Item> {
}
