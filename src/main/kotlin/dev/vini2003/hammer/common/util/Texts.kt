@file:Suppress("DEPRECATION", "UnstableApiUsage")

package dev.vini2003.hammer.common.util

import dev.vini2003.hammer.common.util.extension.gray
import dev.vini2003.hammer.common.util.extension.toLiteralText
import dev.vini2003.hammer.common.util.extension.toTranslatableText
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView
import net.minecraft.fluid.Fluid

object Texts {
	val Empty = "text.hammer.empty".toTranslatableText()
	
	fun percentage(a: Number, b: Number) = "${(a.toFloat() / b.toFloat() * 100.0F).toInt()}%".toLiteralText().gray()
	
	fun tooltip(fluid: Fluid) = tooltip(FluidVariant.of(fluid))
	
	fun tooltip(fluidVariant: FluidVariant) = FluidVariantRendering.getTooltip(fluidVariant)
	
	fun fluidView(fluidView: StorageView<FluidVariant>) = "${Numbers.shorten(fluidView.amount, "d")} / ${Numbers.shorten(fluidView.amount, "d")}".toLiteralText().gray()
}