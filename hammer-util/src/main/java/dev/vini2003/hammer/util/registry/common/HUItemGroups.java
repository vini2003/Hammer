package dev.vini2003.hammer.util.registry.common;

import com.google.common.base.Suppliers;
import dev.vini2003.hammer.core.HC;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;

public class HUItemGroups {
	public static final ItemGroup HAMMER = FabricItemGroupBuilder.build(HC.id("hammer"), Suppliers.memoize(() -> new ItemStack(HUItems.FREEZE)));
	
	public static void init() {
	
	}
}
