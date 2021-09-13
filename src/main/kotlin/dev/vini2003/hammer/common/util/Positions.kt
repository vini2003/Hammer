package dev.vini2003.hammer.common.util

import dev.vini2003.hammer.client.util.Instances

object Positions {
	@JvmStatic
	val MouseX
		get() = Instances.client.mouse.x.toFloat()
	
	@JvmStatic
	val MouseY
		get() = Instances.client.mouse.y.toFloat()
}