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

package dev.vini2003.hammer.gui.api.common.widget.list.slot;

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
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import dev.vini2003.hammer.gui.api.common.widget.provider.FocusedScrollerTextureProvider;
import dev.vini2003.hammer.gui.api.common.widget.provider.ScrollbarTextureProvider;
import dev.vini2003.hammer.gui.api.common.widget.provider.ScrollerTextureProvider;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class SlotListWidget extends Widget implements WidgetCollection, ScrollbarTextureProvider, ScrollerTextureProvider, FocusedScrollerTextureProvider {
	public static final Texture STANDARD_SCROLLBAR_TEXTURE = new PartitionedTexture(HC.id("textures/widget/scrollbar.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.16F);
	public static final Texture STANDARD_SCROLLER_TEXTURE = new PartitionedTexture(HC.id("textures/widget/scroller.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.11F);
	public static final Texture STANDARD_FOCUSED_SCROLLER_TEXTURE = new PartitionedTexture(HC.id("textures/widget/scroller_focus.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.11F);
	
	protected Supplier<Texture> scrollbarTexture = () -> STANDARD_SCROLLBAR_TEXTURE;
	protected Supplier<Texture> scrollerTexture = () -> STANDARD_SCROLLER_TEXTURE;
	protected Supplier<Texture> focusedScrollerTexture = () -> STANDARD_FOCUSED_SCROLLER_TEXTURE;
	
	protected final Inventory inventory;
	
	protected int widthInSlots = 0;
	protected int heightInSlots = 0;
	
	protected int maxSlots = 0;
	
	protected Collection<Widget> children = new ArrayList<>();
	
	protected int row = 0;
	
	protected boolean scrollerHeld = false;
	
	protected Cached<Float> scrollerHeight = new Cached<>(() -> Math.min(getHeight() - 2.0F, (float) heightInSlots / (float) getTotalRows() * (getHeight() - 2.0F)));
	
	protected Cached<Float> scrollerY = new Cached<>(() -> Math.max(getY() + 2.0F, Math.min(getY() + getHeight() - scrollerHeight.get(), (float) row / (float) getTotalRows() * getHeight() + getY() + 1.0F)));
	
	protected Cached<Shape> scrollerRectangle = new Cached<>(() -> {
		return new Shape.Rectangle2D(16.0F, scrollerHeight.get()).translate(getX() + getWidth() - 1.0F - 16.0F, scrollerY.get() - 1.0F, 0.0F);
	});
	
	protected Cached<Shape> scrollbarRectangle = new Cached<>(() -> {
		return new Shape.Rectangle2D(16.0F, getHeight() - 2.0F).translate(getX() + getWidth() - 1.0F - 16.0F, getY() + 1.0F, 0.0F);
	});
	
	public SlotListWidget(Inventory inventory, int widthInSlots, int heightInSlots, int maxSlots) {
		super();
		
		this.inventory = inventory;
		
		this.widthInSlots = widthInSlots;
		this.heightInSlots = heightInSlots;
		
		this.maxSlots = maxSlots;
	}
	
	@Override
	public Size getStandardSize() {
		return new Size(widthInSlots * 18.0F, heightInSlots * 18.0F);
	}
	
	@Override
	protected void onAdded(AddedEvent event) {
		super.onAdded(event);
		
		for (var h = 0; h < heightInSlots; ++h) {
			for (var w = 0; w < widthInSlots; ++w) {
				if (inventory.size() > h + w) {
					var slot = new SlotWidget(inventory, h * widthInSlots + w, Slot::new);
					
					slot.setPosition(position.offset(w * 18.0F, h * 18.0F));
					slot.setSize(18.0F, 18.0F);
					
					add(slot);
				}
			}
		}
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
		
		if (rootCollection.isClient()) return;
		
		if (focused || scrollerHeld) {
			if (event.deltaY() > 0.0D && row > 0) {
				--row;
				
				for (var child : getChildren()) {
					var slotWidget = (SlotWidget) child;
					
					var slot = slotWidget.getSlot();
					
					if (slot.index - widthInSlots >= 0) {
						slot.index = slot.index - widthInSlots;
					}
					
					rootCollection.getScreenHandler().sendContentUpdates();
				}
			} else if (event.deltaY() <= 0.0D && row > getBottomRow()) {
				++row;
				
				for (var child : getChildren()) {
					var slotWidget = (SlotWidget) child;
					
					var slot = slotWidget.getSlot();
					
					if (slot.index + widthInSlots <= inventory.size()) {
						slot.index = slot.index + widthInSlots;
					}
					
					rootCollection.getScreenHandler().sendContentUpdates();
				}
			}
		}
		
		scrollerHeight.refresh();
		scrollerY.refresh();
		
		scrollerRectangle.refresh();
		scrollbarRectangle.refresh();
	}
	
	@Override
	protected void onLayoutChanged(LayoutChangedEvent event) {
		super.onLayoutChanged(event);
		
		scrollerHeight.refresh();
		scrollerY.refresh();
		
		scrollerRectangle.refresh();
		scrollbarRectangle.refresh();
	}
	
	@Override
	public boolean shouldSync(EventType type) {
		return type == EventType.MOUSE_SCROLLED || type == EventType.MOUSE_CLICKED;
	}
	
	@Override
	public Collection<Widget> getChildren() {
		return children;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
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
			if (!child.isHidden()) {
				child.draw(context, tickDelta);
			}
		}
		
		scissors.destroy();
	}
	
	protected int getBottomRow() {
		return maxSlots - heightInSlots;
	}
	
	protected int getTotalRows() {
		return inventory.size() / widthInSlots;
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
