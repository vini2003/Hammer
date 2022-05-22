package dev.vini2003.hammer.core.api.common.supplier;

import net.minecraft.text.Text;

import java.util.function.Supplier;

@FunctionalInterface
public interface LabelSupplier extends Supplier<Text> {
}
