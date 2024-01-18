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

package dev.vini2003.hammer.core.api.common.util;

import com.google.common.collect.ImmutableList;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.color.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FluidTextUtil {
	public static final Map<Fluid, Color> COLOR_OVERRIDES = new HashMap<>();
	
	public static List<Text> getTooltip(Fluid fluid) {
		if (fluid == Fluids.EMPTY) {
			return ImmutableList.of(TextUtil.getEmpty());
		}
		
		var overrideColor = COLOR_OVERRIDES.get(fluid);
		
		var color = TextColor.fromRgb(overrideColor != null ? overrideColor.toRgb() : FluidStackHooks.getColor(fluid));
		
		// Re-implementation of FluidVariantRendering in Fabric.
		var tooltip = new ArrayList<Text>();
		
		tooltip.add(FluidStackHooks.getName(FluidStack.create(fluid, 1_000)).copy().styled(style -> style.withColor(color)));
		
		var tooltipContext = MinecraftClient.getInstance().options.advancedItemTooltips ? TooltipContext.Default.ADVANCED : TooltipContext.Default.NORMAL;
		
		if (tooltipContext.isAdvanced()) {
			tooltip.add(Text.literal(Registry.FLUID.getId(fluid).toString()).formatted(Formatting.DARK_GRAY));
		}
		
		return tooltip;
		
//		return FluidVariantRendering.getTooltip(fluidVariant).stream().map(text -> (MutableText) text).map(text -> text.styled(style -> style.withColor(color))).collect(Collectors.toList());
	}
	
	public static List<Text> getTooltip(Fluid fluid, long amount, long capacity) {
		var fluidId = Registry.FLUID.getId(fluid);
		
		var tooltips = new ArrayList<Text>();
		
		if (HC.CONFIG.useDroplets) {
			tooltips.addAll(getTooltip(fluid));
			tooltips.add(Text.literal(NumberUtil.getPrettyShortenedString(amount, "d") + " / " + NumberUtil.getPrettyShortenedString(capacity, "d")).formatted(Formatting.GRAY));
			tooltips.add(TextUtil.getModId(fluidId));
		} else {
			tooltips.addAll(getTooltip(fluid));
			tooltips.add(Text.literal(NumberUtil.getPrettyShortenedString(amount / ((double) FluidUtil.getBucketCapacity()), "b") + " / " + NumberUtil.getPrettyShortenedString(capacity / ((double) FluidUtil.getBucketCapacity()), "b")).formatted(Formatting.GRAY));
			tooltips.add(TextUtil.getModId(fluidId));
		}
		
		return tooltips;
	}
	
	public static List<Text> getDetailedStorageTooltips(Fluid fluid, long amount, long capacity) {
		var fluidId = Registry.FLUID.getId(fluid);
		
		var tooltips = new ArrayList<Text>();
		
		if (HC.CONFIG.useDroplets) {
			tooltips.addAll(getTooltip(fluid));
			tooltips.add(Text.literal(NumberUtil.getPrettyString(amount, "d") + " / " + NumberUtil.getPrettyString(capacity, "d")).formatted(Formatting.GRAY));
			tooltips.add(TextUtil.getModId(fluidId));
		} else {
			tooltips.addAll(getTooltip(fluid));
			tooltips.add(Text.literal(NumberUtil.getPrettyString(amount / ((double) FluidUtil.getBucketCapacity()), "b") + " / " + NumberUtil.getPrettyString(capacity / ((double) FluidUtil.getBucketCapacity()), "b")).formatted(Formatting.GRAY));
			tooltips.add(TextUtil.getModId(fluidId));
		}
		
		return tooltips;
	}
}
