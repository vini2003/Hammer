package dev.vini2003.hammer.gui.api.client.event;

import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public interface InGameHudEvents {
	Event<Init> INIT = EventFactory.createArrayBacked(Init.class, (events) -> (hud, collection) -> {
		for (var event : events) {
			event.onInit(hud, collection);
		}
	});
	
	Event<Render> RENDER = EventFactory.createArrayBacked(Render.class, (events) -> (matrices, provider, hud, collection) -> {
		for (var event : events) {
			event.onRender(matrices, provider, hud, collection);
		}
	});
	
	@FunctionalInterface
	interface Init {
		void onInit(InGameHud hud, WidgetCollection collection);
	}
	
	@FunctionalInterface
	interface Render {
		void onRender(MatrixStack matrices, VertexConsumerProvider.Immediate provider, InGameHud hud, WidgetCollection collection);
	}
}
