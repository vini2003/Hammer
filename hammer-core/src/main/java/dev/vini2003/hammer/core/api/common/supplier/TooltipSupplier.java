package dev.vini2003.hammer.core.api.common.supplier;

import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Supplier;

@FunctionalInterface
public interface TooltipSupplier extends Supplier<List<Text>> {
}
