package com.github.vini2003.blade.common.widget

import com.github.vini2003.blade.common.handler.BaseScreenHandler
import net.minecraft.entity.player.PlayerEntity

interface HandledWidgetCollection : WidgetCollection {
	val handler: BaseScreenHandler

	fun onLayoutChanged()
}