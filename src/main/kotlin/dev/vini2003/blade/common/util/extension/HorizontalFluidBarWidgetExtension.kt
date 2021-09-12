package dev.vini2003.blade.common.util.extension

import dev.vini2003.blade.client.texture.PartitionedTexture
import dev.vini2003.blade.client.texture.Texture
import dev.vini2003.blade.common.widget.bar.HorizontalFluidBarWidget
import dev.vini2003.blade.common.widget.bar.VerticalFluidBarWidget
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.fluid.Fluid

fun HorizontalFluidBarWidget.maximum(maximum: () -> Float) {
	this.maximum = maximum
}

fun HorizontalFluidBarWidget.current(current: () -> Float) {
	this.current = current
}

fun HorizontalFluidBarWidget.backgroundTexture(backgroundTexture: Texture) {
	this.backgroundTexture = backgroundTexture
}

fun HorizontalFluidBarWidget.foregroundTexture(foregroundTexture: Texture) {
	this.foregroundTexture = foregroundTexture
}

fun HorizontalFluidBarWidget.fluidVariant(fluidVariant: FluidVariant) {
	this.fluidVariant = fluidVariant
}

fun HorizontalFluidBarWidget.fluid(fluid: Fluid) {
	fluidVariant(FluidVariant.of(fluid))
}