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

package dev.vini2003.hammer.core.api.common.util

import dev.vini2003.hammer.core.api.client.util.TextUtils.getIdMod
import dev.vini2003.hammer.core.api.common.color.Color
import dev.vini2003.hammer.core.api.common.util.extension.*
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.util.registry.Registry

object FluidTextUtils {
	@JvmField
	val COLOR_OVERRIDES: MutableMap<FluidVariant, Color> = mutableMapOf()
	
	@JvmStatic
	fun getVariantTooltips(fluidVariant: FluidVariant): List<Text> {
		if (fluidVariant.isBlank) return listOf(TextUtils.EMPTY.gray())
		
		val color = TextColor.fromRgb(COLOR_OVERRIDES.get(fluidVariant)?.toRGB() ?: FluidVariantRendering.getColor(fluidVariant))
		
		return FluidVariantRendering.getTooltip(fluidVariant).map { text -> text as MutableText }.onEach { text -> text.styled { style -> style.withColor(color) } }
	}

	@JvmStatic
	fun getShortenedStorageTooltips(fluidView: StorageView<FluidVariant>): List<Text> {
		if (fluidView.isResourceBlank) return listOf(TextUtils.EMPTY.gray())
		
		val fluidId = Registry.FLUID.getId(fluidView.resource.fluid)
		
		return getVariantTooltips(fluidView.resource) + "${fluidView.amount.toPrettyShortenedString("d")} / ${fluidView.capacity.toPrettyShortenedString("d")}".toLiteralText().gray() + getIdMod(fluidId)
	}
	
	@JvmStatic
	fun getDetailedStorageTooltips(fluidView: StorageView<FluidVariant>): List<Text> {
		if (fluidView.isResourceBlank) return listOf(TextUtils.EMPTY.gray())

		val fluidId = Registry.FLUID.getId(fluidView.resource.fluid)
		
		return getVariantTooltips(fluidView.resource) + "${fluidView.amount.toPrettyString("d")} / ${fluidView.capacity.toPrettyString("d")}".toLiteralText().gray() + getIdMod(fluidId)
	}
}