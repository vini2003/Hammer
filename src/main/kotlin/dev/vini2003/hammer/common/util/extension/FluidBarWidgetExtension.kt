@file:Suppress("DEPRECATION", "UnstableApiUsage")

package dev.vini2003.hammer.common.util.extension

import dev.vini2003.hammer.common.widget.bar.FluidBarWidget
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.fluid.Fluid

var FluidBarWidget.fluid: Fluid
	get() = fluidVariant.fluid
	set(value) {
		fluidVariant = FluidVariant.of(value)
	}