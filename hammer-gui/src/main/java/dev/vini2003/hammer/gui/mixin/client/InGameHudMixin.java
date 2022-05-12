package dev.vini2003.hammer.gui.mixin.client;

import dev.vini2003.hammer.core.api.client.util.DrawingUtils;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.client.event.InGameHudEvents;
import dev.vini2003.hammer.gui.api.client.util.extension.InGameHudExtensionKt;
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import dev.vini2003.hammer.gui.api.common.util.InGameHudUtils;
import dev.vini2003.hammer.gui.api.common.widget.BaseWidget;
import dev.vini2003.hammer.gui.api.common.widget.BaseWidgetCollection;
import dev.vini2003.hammer.gui.api.common.widget.bar.HudBarWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin implements BaseWidgetCollection, BaseWidgetCollection.Handled {
	private final List<BaseWidget> hammer$widgets = new ArrayList<>();
	@Shadow
	@Final
	private MinecraftClient client;
	
	@Inject(at = @At("RETURN"), method = "<init>")
	private void hammer$init(MinecraftClient client, CallbackInfo ci) {
		hammer$widgets.clear();
		
		InGameHudEvents.INIT.invoker().onInit((InGameHud) (Object) this, this);
		
		onLayoutChanged();
		
		getAllWidgets().forEach(widget -> {
			widget.onLayoutChanged();
		});
	}
	
	@Inject(at = @At("RETURN"), method = "render")
	private void hammer$render(MatrixStack matrices, float delta, CallbackInfo ci) {
		if (client == null) {
			return;
		}
		
		var provider = client.getBufferBuilders().getEffectVertexConsumers();
		
		var bars = getWidgets().stream()
					.filter(widget -> widget instanceof HudBarWidget)
					.map(widget -> (HudBarWidget) widget)
					.filter(widget -> {
						if (!widget.shouldShow()) {
							widget.setHidden(true);
							
							return false;
						} else {
							widget.setHidden(false);
							
							return true;
						}
					})
					.collect(Collectors.toList());
		
		var leftPos = InGameHudUtils.getArmorBarPos((InGameHud) (Object) this, client.player);
		var rightPos = InGameHudUtils.getHungerBarPos((InGameHud) (Object) this, client.player);
		
		for (var bar : bars) {
			switch (bar.getSide()) {
				case LEFT -> {
					leftPos = leftPos.plus(new Position(0.0F, -10.0F));
					
					bar.setPosition(leftPos);
					bar.setSize(new Size(10.0F * 9.0F, 9.0F));
				}
				
				case RIGHT -> {
					rightPos = rightPos.plus(new Position(0.0F, -10.0F));
					
					bar.setPosition(rightPos);
					bar.setSize(new Size(10.0F * 9.0F, 9.0F));
				}
			}
		}
		
		getWidgets().stream()
					.filter(widget -> !widget.isHidden())
					.forEach(widget -> widget.drawWidget(matrices, provider, delta));
		
		provider.draw();
		
		InGameHudEvents.RENDER.invoker().onRender(matrices, provider, (InGameHud) (Object) this, this);
	}
	
	@NotNull
	@Override
	public List<BaseWidget> getWidgets() {
		return hammer$widgets;
	}
	
	@Override
	public void onLayoutChanged() {
	
	}
	
	@Override
	public boolean isClient() {
		return true;
	}
	
	@Nullable
	@Override
	public Integer getId() {
		return null;
	}
	
	@Nullable
	@Override
	public BaseScreenHandler getHandler() {
		return null;
	}
}
