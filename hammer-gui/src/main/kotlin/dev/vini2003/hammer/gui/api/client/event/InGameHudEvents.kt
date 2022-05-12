package dev.vini2003.hammer.gui.api.client.event

import dev.vini2003.hammer.gui.api.common.widget.BaseWidgetCollection
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack

interface InGameHudEvents {
	companion object {
		@JvmField
		val INIT = EventFactory.createArrayBacked(Init::class.java) { events ->
			object : Init {
				override fun onInit(hud: InGameHud, collection: BaseWidgetCollection) {
					events.forEach { event ->
						event.onInit(hud, collection)
					}
				}
			}
		}
		
		@JvmField
		val RENDER = EventFactory.createArrayBacked(Render::class.java) { events ->
			object : Render {
				override fun onRender(matrices: MatrixStack, provider: VertexConsumerProvider.Immediate, hud: InGameHud, collection: BaseWidgetCollection) {
					events.forEach { event ->
						event.onRender(matrices, provider, hud, collection)
					}
				}
			}
		}
	}
	
	@FunctionalInterface
	fun interface Init {
		fun onInit(hud: InGameHud, collection: BaseWidgetCollection)
	}
	
	@FunctionalInterface
	fun interface Render {
		fun onRender(matrices: MatrixStack, provider: VertexConsumerProvider.Immediate, hud: InGameHud, collection: BaseWidgetCollection)
	}
}