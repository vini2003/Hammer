package dev.vini2003.blade.common.util.extension

import dev.vini2003.blade.client.texture.PartitionedTexture
import dev.vini2003.blade.client.texture.Texture
import dev.vini2003.blade.common.widget.bar.VerticalFluidBarWidget
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.fluid.Fluid

fun VerticalFluidBarWidget.maximum(maximum: () -> Float) {
	this.maximum = maximum
}

fun VerticalFluidBarWidget.current(current: () -> Float) {
	this.current = current
}

fun VerticalFluidBarWidget.backgroundTexture(backgroundTexture: Texture) {
	this.backgroundTexture = backgroundTexture
}

fun VerticalFluidBarWidget.foregroundTexture(foregroundTexture: Texture) {
	this.foregroundTexture = foregroundTexture
}

fun VerticalFluidBarWidget.fluidVariant(fluidVariant: FluidVariant) {
	this.fluidVariant = fluidVariant
}

fun VerticalFluidBarWidget.fluid(fluid: Fluid) {
	fluidVariant(FluidVariant.of(fluid))
}