package dev.vini2003.blade.common.util.extension

import net.minecraft.client.util.math.MatrixStack

val MatrixStack.model
	get() = peek().model

val MatrixStack.normal
	get() = peek().normal