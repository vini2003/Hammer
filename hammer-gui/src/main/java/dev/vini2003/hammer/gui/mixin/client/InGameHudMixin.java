/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.gui.mixin.client;

import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.client.event.InGameHudEvents;
import dev.vini2003.hammer.gui.api.common.event.LayoutChangedEvent;
import dev.vini2003.hammer.gui.api.common.util.InGameHudUtil;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import dev.vini2003.hammer.gui.api.common.widget.bar.HudBarWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;
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
public abstract class InGameHudMixin implements WidgetCollection, WidgetCollection.Root {
	private final List<Widget> hammer$children = new ArrayList<>();
	@Shadow
	@Final
	private MinecraftClient client;
	
	private InGameHud hammer$self() {
		return (InGameHud) (Object) this;
	}
	
	@Inject(at = @At("RETURN"), method = "<init>")
	private void hammer$init(MinecraftClient client, ItemRenderer itemRenderer, CallbackInfo ci) {
		hammer$children.clear();
		
		InGameHudEvents.INIT.invoker().onInit(hammer$self(), this);
		
		onLayoutChanged();
		
		for (var child : getAllChildren()) {
			child.dispatchEvent(new LayoutChangedEvent());
		}
	}
	
	
	@Inject(at = @At("RETURN"), method = "render")
	private void hammer$render(MatrixStack matrices, float delta, CallbackInfo ci) {
		if (client == null) {
			return;
		}
		
		var provider = client.getBufferBuilders().getEffectVertexConsumers();
		
		var bars = getChildren()
				.stream()
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
				}).toList();
		
		var leftPos = InGameHudUtil.getLeftBarPos(client.player);
		var rightPos = InGameHudUtil.getRightBarPos(client.player);
		
		for (var bar : bars) {
			switch (bar.getSide()) {
				case LEFT -> {
					leftPos = leftPos.plus(new Position(0.0F, -10.0F));
					
					bar.setPosition(leftPos);
				}
				
				case RIGHT -> {
					rightPos = rightPos.plus(new Position(0.0F, -10.0F));
					
					bar.setPosition(rightPos);
				}
			}
			
			switch (bar.getType()) {
				case CONTINUOS -> {
					bar.setSize(new Size(10.0F * 9.0F - 9.0F, 9.0F));
				}
				
				case TILED -> {
					bar.setSize(new Size(10.0F * 9.0F, 9.0F));
				}
			}
		}
		
		getChildren().stream()
					 .filter(widget -> !widget.isHidden())
					 .forEach(widget -> widget.draw(matrices, provider, delta));
		
		provider.draw();
		
		InGameHudEvents.RENDER.invoker().onRender(matrices, provider, hammer$self(), this);
	}
	
	@NotNull
	@Override
	public List<Widget> getChildren() {
		return hammer$children;
	}
	
	@Override
	public void onLayoutChanged() {
	
	}
}
