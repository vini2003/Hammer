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

package dev.vini2003.hammer.gui.api.common.widget.list;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.scissor.Scissors;
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.PositionUtil;
import dev.vini2003.hammer.core.api.common.cache.Cached;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.shape.Shape;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.event.*;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import dev.vini2003.hammer.gui.api.common.widget.provider.FocusedScrollerTextureProvider;
import dev.vini2003.hammer.gui.api.common.widget.provider.ScrollbarTextureProvider;
import dev.vini2003.hammer.gui.api.common.widget.provider.ScrollerTextureProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class ListWidget extends Widget implements WidgetCollection, ScrollbarTextureProvider, ScrollerTextureProvider, FocusedScrollerTextureProvider {
	public static final Texture STANDARD_SCROLLBAR_TEXTURE = new PartitionedTexture(HC.id("textures/widget/scrollbar.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.16F);
	public static final Texture STANDARD_SCROLLER_TEXTURE = new PartitionedTexture(HC.id("textures/widget/scroller.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.11F);
	public static final Texture STANDARD_FOCUSED_SCROLLER_TEXTURE = new PartitionedTexture(HC.id("textures/widget/scroller_focus.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.11F);
	
	protected Supplier<Texture> scrollbarTexture = () -> STANDARD_SCROLLBAR_TEXTURE;
	protected Supplier<Texture> scrollerTexture = () -> STANDARD_SCROLLER_TEXTURE;
	protected Supplier<Texture> focusedScrollerTexture = () -> STANDARD_FOCUSED_SCROLLER_TEXTURE;
	
	protected Collection<Widget> children = new ArrayList<>();
	
	protected boolean scrollerHeld = false;
	
	protected float scrolledY = 0.0F;
	
	protected Cached<Float> maxY = new Cached<>(() -> {
		var maxY = Float.MIN_VALUE;
		
		for (var child : getChildren()) {
			if (child.getY() + child.getHeight() > maxY) {
				maxY = child.getY() + child.getHeight();
			}
		}
		
		return maxY;
	});
	
	protected Cached<Float> minY = new Cached<>(() -> {
		var minY = Float.MAX_VALUE;
		
		for (var child : getChildren()) {
			if (child.getY() < minY) {
				minY = child.getY();
			}
		}
		
		return minY;
	});
	
	protected Cached<Float> scrollerHeight = new Cached<>(() -> {
		return Math.min(getHeight() - 2.0F, Math.max((getHeight() / (maxY.get() - minY.get())) * getHeight(), 10.0F));
	});
	
	protected Cached<Float> scrollerY = new Cached<>(() -> {
		return MathHelper.clamp((maxY.get() - minY.get() <= getHeight()) ? getY() : getY() + ((scrolledY * -1) / (maxY.get() - minY.get() - getHeight() - 5.0F)) * (getHeight() - 5.0F - scrollerHeight.get()) + 2.0F, getY() + 2.0F, getY() + getHeight() - scrollerHeight.get());
	});
	
	protected Cached<Shape> scrollerRectangle = new Cached<>(() -> {
		return new Shape.Rectangle2D(16.0F, scrollerHeight.get()).translate(getX() + getWidth() - 1.0F - 16.0F, scrollerY.get() - 1.0F, 0.0F);
	});
	
	protected Cached<Shape> scrollbarRectangle = new Cached<>(() -> {
		return new Shape.Rectangle2D(16.0F, getHeight() - 2.0F).translate(getX() + getWidth() - 1.0F - 16.0F, getY() + 1.0F, 0.0F);
	});
	
	@Override
	public Size getStandardSize() {
		return new Size(
				Math.max(64.0F, getChildren().stream().map(Widget::getWidth).max(Float::compareTo).orElse(0.0F)),
				Math.min(96.0F, getChildren().stream().map(Widget::getHeight).reduce(Float::sum).orElse(0.0F))
		);
	}
	
	@Override
	public Collection<Widget> getChildren() {
		return children;
	}
	
	@Override
	public void add(Widget child) {
		WidgetCollection.super.add(child);
		
		size = getStandardSize();
	}
	
	@Override
	protected void onMouseClicked(MouseClickedEvent event) {
		super.onMouseClicked(event);
		
		var pos = new Position(event.x(), event.y());
		
		if (scrollerRectangle.get().isPositionWithin(pos)) {
			scrollerHeld = true;
		} else {
			if (scrollbarRectangle.get().isPositionWithin(pos)) {
				if (event.y() > scrollerY.get()) {
					dispatchEvent(new MouseScrolledEvent(event.x(), event.y(), -1.0D));
				} else if (event.y() < scrollerY.get()) {
					dispatchEvent(new MouseScrolledEvent(event.x(), event.y(), 1.0D));
				}
			}
		}
	}
	
	@Override
	protected void onMouseReleased(MouseReleasedEvent event) {
		super.onMouseReleased(event);
		
		scrollerHeld = false;
	}
	
	@Override
	protected void onMouseDragged(MouseDraggedEvent event) {
		super.onMouseDragged(event);
		
		if (scrollerHeld) {
			if (event.deltaY() > 0.0D) {
				dispatchEvent(new MouseScrolledEvent(event.x(), event.y(), -event.deltaY()));
			} else if (event.deltaY() < 0.0D) {
				dispatchEvent(new MouseScrolledEvent(event.x(), event.y(), -event.deltaY()));
			}
		}
	}
	
	@Override
	protected void onMouseScrolled(MouseScrolledEvent event) {
		super.onMouseScrolled(event);
		
		if (focused || scrollerHeld) {
			if (!children.isEmpty()) {
				if (event.deltaY() > 0.0D && minY.get() < getY() + 2.0F) {
					scrolledY += event.deltaY() * 5.0F;
					
					for (var child : getAllChildren()) {
						child.setY(child.getY() + (float) event.deltaY() * 2.5F);
						child.dispatchEvent(new LayoutChangedEvent());
						
						if (child.getY() >= getY() + getHeight()) {
							child.setHidden(true);
						} else if (child.getY() >= getY()) {
							child.setHidden(false);
						}
					}
					
					dispatchEvent(new LayoutChangedEvent());
				} else if (event.deltaY() <= 0.0D && maxY.get() >= getY() + getHeight() - 2.0F) {
					scrolledY += event.deltaY() * 5.0F;
					
					for (var child : getAllChildren()) {
						child.setY(child.getY() + (float) event.deltaY() * 2.5F);
						child.dispatchEvent(new LayoutChangedEvent());
						
						if (child.getY() >= getY() + getHeight()) {
							child.setHidden(true);
						} else if (child.getY() >= getY()) {
							child.setHidden(false);
						}
					}
					
					dispatchEvent(new LayoutChangedEvent());
				}
			}
		}
	}
	
	@Override
	protected void onLayoutChanged(LayoutChangedEvent event) {
		super.onLayoutChanged(event);
		
		var y = getY() + 2.0F + scrolledY;
		
		for (var child : getChildren()) {
			child.setX(getX());
			child.setY(y);
			
			y += child.getHeight() + 2.0F;
		}
		
		maxY.refresh();
		minY.refresh();
		
		scrollerHeight.refresh();
		scrollerY.refresh();
		
		scrollerRectangle.refresh();
		scrollbarRectangle.refresh();
	}
	
	@Override
	public void draw(DrawContext context, float tickDelta) {
		var matrices = context.getMatrices();
		var provider = context.getVertexConsumers();
		
		scrollbarTexture.get().draw(matrices, provider, getX() + getWidth() - 18.0F, getY(), 18.0F, getHeight());
		
		var scrollerFocused = scrollerRectangle.get().isPositionWithin(PositionUtil.getMousePosition());
		
		if (scrollerFocused || scrollerHeld) {
			focusedScrollerTexture.get().draw(matrices, provider, getX() + getWidth() - 18.0F + 1.0F, scrollerY.get() - 1.0F, 16.0F, scrollerHeight.get());
		} else {
			scrollerTexture.get().draw(matrices, provider, getX() + getWidth() - 18.0F + 1.0F, scrollerY.get() - 1.0F, 16.0F, scrollerHeight.get());
		}
		
		var scissors = new Scissors(getX(), getY(), getWidth(), getHeight(), provider);
		
		for (var child : getChildren()) {
//			if (!child.isHidden()) {
				child.draw(context, tickDelta);
//			}
		}
		
		scissors.destroy();
	}
	
	@Override
	public Supplier<Texture> getFocusedScrollerTexture() {
		return focusedScrollerTexture;
	}
	
	@Override
	public void setFocusedScrollerTexture(Supplier<Texture> focusedScrollerTexture) {
		this.focusedScrollerTexture = focusedScrollerTexture;
	}
	
	@Override
	public Supplier<Texture> getScrollbarTexture() {
		return scrollbarTexture;
	}
	
	@Override
	public void setScrollbarTexture(Supplier<Texture> scrollbarTexture) {
		this.scrollbarTexture = scrollbarTexture;
	}
	
	@Override
	public Supplier<Texture> getScrollerTexture() {
		return scrollerTexture;
	}
	
	@Override
	public void setScrollerTexture(Supplier<Texture> scrollerTexture) {
		this.scrollerTexture = scrollerTexture;
	}
}
