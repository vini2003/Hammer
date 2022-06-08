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
import dev.vini2003.hammer.gui.api.common.event.*;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class ListWidget extends Widget implements WidgetCollection {
	public static final Texture STANDARD_SCROLLBAR_TEXTURE = new PartitionedTexture(HC.id("textures/widget/scrollbar.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.16F);
	public static final Texture STANDARD_SCROLLER_TEXTURE = new PartitionedTexture(HC.id("textures/widget/scroller.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.11F);
	public static final Texture STANDARD_FOCUSED_SCROLLER_TEXTURE = new PartitionedTexture(HC.id("textures/widget/scroller_focus.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.11F);
	
	protected Supplier<Texture> scrollbarTexture = () -> STANDARD_SCROLLBAR_TEXTURE;
	protected Supplier<Texture> scrollerTexture = () -> STANDARD_SCROLLER_TEXTURE;
	protected Supplier<Texture> focusedScrollerTexture = () -> STANDARD_FOCUSED_SCROLLER_TEXTURE;
	
	protected Collection<Widget> children = new ArrayList<>();
	
	protected boolean scrollerHeld = false;
	
	protected Cached<Float> maxY = new Cached<>(() -> {
		var maxY = 0.0F;
		
		for (var child : getChildren()) {
			if (child.getY() + child.getHeight() > maxY) {
				maxY = child.getY() + child.getHeight();
			}
		}
		
		return maxY;
	});
	
	protected Cached<Float> minY = new Cached<>(() -> {
		var minY = 0.0F;
		
		for (var child : getChildren()) {
			if (child.getY() < minY) {
				minY = child.getY();
			}
		}
		
		return minY;
	});
	
	protected Cached<Float> scrollerHeight = new Cached<>(() -> {
		var height = 0.0F;
		
		for (var child : getChildren()) {
			height += child.getHeight();
		}
		
		return Math.min(getHeight() - 2.0F, getHeight() / height * getHeight());
	});
	
	protected Cached<Float> scrollerY = new Cached<>(() -> {
		return Math.max(getY() + 2.0F, Math.min(getY() + getHeight() - scrollerHeight.get(), (Math.abs(getY() - minY.get()) / (maxY.get() - minY.get()) * (getHeight() + scrollerHeight.get()) + getY() + 1.0F)));
	});
	
	protected Cached<Shape> scrollerRectangle = new Cached<>(() -> {
		return new Shape.Rectangle2D(16.0F, scrollerHeight.get()).translate(getX() + getWidth() - 1.0F - 16.0F, scrollerY.get() - 1.0F, 0.0F);
	});
	
	protected Cached<Shape> scrollbarRectangle = new Cached<>(() -> {
		return new Shape.Rectangle2D(16.0F, getHeight() - 2.0F).translate(getX() + getWidth() - 1.0F - 16.0F, getY() + 1.0F, 0.0F);
	});
	
	@Override
	public Collection<Widget> getChildren() {
		return children;
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
					for (var child : getAllChildren()) {
						child.setY(child.getY() + (float) event.deltaY() * 2.5F);
						child.dispatchEvent(new LayoutChangedEvent());
						
						if (child.getY() >= getY() + getHeight()) {
							child.setHidden(true);
						} else if (child.getY() >= getY()) {
							child.setHidden(false);
						}
					}
					
					// This was inside the loop!
					dispatchEvent(new LayoutChangedEvent());
				} else if (event.deltaY() <= 0.0D && maxY.get() >= getY() + getHeight() - 2.0F) {
					for (var child : getAllChildren()) {
						child.setY(child.getY() + (float) event.deltaY() * 2.5F);
						child.dispatchEvent(new LayoutChangedEvent());
						
						if (child.getY() >= getY() + getHeight()) {
							child.setHidden(true);
						} else if (child.getY() >= getY()) {
							child.setHidden(false);
						}
					}
					
					// This was inside the loop!
					dispatchEvent(new LayoutChangedEvent());
				}
			}
		}
		
		maxY.refresh();
		minY.refresh();
		
		scrollerHeight.refresh();
		scrollerY.refresh();
		
		scrollerRectangle.refresh();
		scrollbarRectangle.refresh();
	}
	
	@Override
	protected void onLayoutChanged(LayoutChangedEvent event) {
		super.onLayoutChanged(event);
		
		maxY.refresh();
		minY.refresh();
		
		scrollerHeight.refresh();
		scrollerY.refresh();
		
		scrollerRectangle.refresh();
		scrollbarRectangle.refresh();
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		scrollbarTexture.get().draw(matrices, provider, getX() + getWidth() - 18.0F, getY(), 18.0F, getHeight());
		
		var scrollerFocused = scrollerRectangle.get().isPositionWithin(PositionUtil.getMousePosition());
		
		if (scrollerFocused || scrollerHeld) {
			focusedScrollerTexture.get().draw(matrices, provider, getX() + getWidth() - 18.0F + 1.0F, scrollerY.get() - 1.0F, 16.0F, scrollerHeight.get());
		} else {
			scrollerTexture.get().draw(matrices, provider, getX() + getWidth() - 18.0F + 1.0F, scrollerY.get() - 1.0F, 16.0F, scrollerHeight.get());
		}
		
		var scissors = new Scissors(getX(), getY(), getWidth(), getHeight(), provider);
		
		for (var child : getChildren()) {
			if (!child.isHidden()) {
				child.draw(matrices, provider, tickDelta);
			}
		}
		
		scissors.destroy();
	}
}
