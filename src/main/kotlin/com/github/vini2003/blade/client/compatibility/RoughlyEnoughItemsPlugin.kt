package com.github.vini2003.blade.client.compatibility

import com.github.vini2003.blade.Blade
import com.github.vini2003.blade.client.handler.BaseHandledScreen
import com.github.vini2003.blade.common.handler.BaseScreenHandler
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.DisplayHelper
import me.shedaniel.rei.api.DisplayHelper.DisplayBoundsProvider
import me.shedaniel.rei.api.plugins.REIPluginV0
import net.minecraft.util.Identifier

class RoughlyEnoughItemsPlugin : REIPluginV0 {
	companion object {
		val IDENTIFIER = Blade.identifier("roughly_enough_items_plugin")
	}

	override fun getPluginIdentifier(): Identifier {
		return IDENTIFIER
	}

	override fun registerBounds(displayHelper: DisplayHelper?) {
		displayHelper!!.registerHandler(object : DisplayBoundsProvider<BaseHandledScreen<BaseScreenHandler>> {
			override fun getBaseSupportedClass(): Class<*>? {
				return BaseHandledScreen::class.java
			}

			override fun getScreenBounds(screen: BaseHandledScreen<BaseScreenHandler>): Rectangle {
				return screen.screenHandler.rectangle
			}

			override fun getPriority(): Float {
				return 16F
			}
		})
	}
}