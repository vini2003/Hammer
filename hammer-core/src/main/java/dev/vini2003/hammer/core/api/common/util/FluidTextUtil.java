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
import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.registry.common.HCConfig;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FluidTextUtil {
	public static final Map<FluidVariant, Color> COLOR_OVERRIDES = new HashMap<>();
	
	public static List<Text> getVariantTooltips(FluidVariant fluidVariant) {
		if (fluidVariant.isBlank()) {
			return ImmutableList.of(TextUtil.getEmpty());
		}
		
		var overrideColor = COLOR_OVERRIDES.get(fluidVariant);
		
		var color = TextColor.fromRgb(overrideColor != null ? overrideColor.toRgb() : FluidVariantRendering.getColor(fluidVariant));
		
		return FluidVariantRendering.getTooltip(fluidVariant).stream().map(text -> (MutableText) text).map(text -> text.styled(style -> style.withColor(color))).collect(Collectors.toList());
	}
	
	public static List<Text> getShortenedStorageTooltips(StorageView<FluidVariant> fluidView) {
		var fluidId = Registry.FLUID.getId(fluidView.getResource().getFluid());
		
		var tooltips = new ArrayList<Text>();
		
		if (HCConfig.USE_DROPLETS) {
			tooltips.addAll(getVariantTooltips(fluidView.getResource()));
			tooltips.add(TextUtil.toLiteralText(NumberUtil.getPrettyShortenedString(fluidView.getAmount(), "d") + " / " + NumberUtil.getPrettyShortenedString(fluidView.getCapacity(), "d")).formatted(Formatting.GRAY));
			tooltips.add(TextUtil.getModId(fluidId));
		} else {
			tooltips.addAll(getVariantTooltips(fluidView.getResource()));
			tooltips.add(TextUtil.toLiteralText(NumberUtil.getPrettyShortenedString(fluidView.getAmount() / ((double) FluidConstants.BUCKET), "b") + " / " + NumberUtil.getPrettyShortenedString(fluidView.getCapacity() / ((double) FluidConstants.BUCKET), "b")).formatted(Formatting.GRAY));
			tooltips.add(TextUtil.getModId(fluidId));
		}
		
		return tooltips;
	}
	
	public static List<Text> getDetailedStorageTooltips(StorageView<FluidVariant> fluidView) {
		var fluidId = Registry.FLUID.getId(fluidView.getResource().getFluid());
		
		var tooltips = new ArrayList<Text>();
		
		if (HCConfig.USE_DROPLETS) {
			tooltips.addAll(getVariantTooltips(fluidView.getResource()));
			tooltips.add(TextUtil.toLiteralText(NumberUtil.getPrettyString(fluidView.getAmount(), "d") + " / " + NumberUtil.getPrettyString(fluidView.getCapacity(), "d")).formatted(Formatting.GRAY));
			tooltips.add(TextUtil.getModId(fluidId));
		} else {
			tooltips.addAll(getVariantTooltips(fluidView.getResource()));
			tooltips.add(TextUtil.toLiteralText(NumberUtil.getPrettyString(fluidView.getAmount() / ((double) FluidConstants.BUCKET), "b") + " / " + NumberUtil.getPrettyString(fluidView.getCapacity() / ((double) FluidConstants.BUCKET), "b")).formatted(Formatting.GRAY));
			tooltips.add(TextUtil.getModId(fluidId));
		}
		
		return tooltips;
	}
}
