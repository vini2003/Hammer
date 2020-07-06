package dev.vini2003.hammer.common.util

import dev.vini2003.hammer.client.util.InstanceUtils

object PositionUtils {
	@JvmStatic
	@get:JvmName("getMouseX")
	val MOUSE_X
		get() = InstanceUtils.CLIENT.mouse.x.toFloat()
	
	@JvmStatic
	@get:JvmName("getMouseY")
	val MOUSE_Y
		get() = InstanceUtils.CLIENT.mouse.y.toFloat()
}