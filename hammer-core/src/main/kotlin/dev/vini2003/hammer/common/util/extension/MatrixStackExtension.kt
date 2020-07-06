package dev.vini2003.hammer.common.util.extension

import net.minecraft.client.util.math.MatrixStack

val MatrixStack.positionMatrix
	get() = peek().positionMatrix

val MatrixStack.normalMatrix
	get() = peek().normalMatrix