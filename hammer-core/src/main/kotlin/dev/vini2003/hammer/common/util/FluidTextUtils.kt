package dev.vini2003.hammer.common.util

import dev.vini2003.hammer.common.util.extension.gray
import dev.vini2003.hammer.common.util.extension.toLiteralText
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView
import net.minecraft.fluid.Fluid

object FluidTextUtils {
	@JvmStatic
	fun tooltip(fluid: Fluid) = variantTooltip(FluidVariant.of(fluid))
	
	@JvmStatic
	fun variantTooltip(fluidVariant: FluidVariant) = FluidVariantRendering.getTooltip(fluidVariant)
	
	@JvmStatic
	fun shortenedTooltip(fluidView: StorageView<FluidVariant>) = "${NumberUtils.shorten(fluidView.amount, "d")} / ${NumberUtils.shorten(fluidView.capacity, "d")}".toLiteralText().gray()
	
	@JvmStatic
	fun detailedTooltip(fluidView: StorageView<FluidVariant>) = "${fluidView.amount} d / ${fluidView.capacity} d".toLiteralText().gray()
}