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

package dev.vini2003.hammer.gui.api.common.widget.tab;

import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import dev.vini2003.hammer.core.api.client.util.PositionUtil;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.shape.Shape;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.event.AddedEvent;
import dev.vini2003.hammer.gui.api.common.event.LayoutChangedEvent;
import dev.vini2003.hammer.gui.api.common.event.MouseClickedEvent;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import dev.vini2003.hammer.gui.api.common.widget.panel.PanelWidget;
import dev.vini2003.hammer.gui.api.common.widget.provider.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class TabWidget extends Widget implements WidgetCollection, ActiveLeftTextureProvider, ActiveMiddleTextureProvider, ActiveRightTextureProvider, InactiveLeftTextureProvider, InactiveMiddleTextureProvider, InactiveRightTextureProvider, TextureProvider {
	public static final Texture STANDARD_ACTIVE_LEFT_TEXTURE = new ImageTexture(HC.id("textures/widget/tab_left_active.png"));
	public static final Texture STANDARD_ACTIVE_MIDDLE_TEXTURE = new ImageTexture(HC.id("textures/widget/tab_middle_active.png"));
	public static final Texture STANDARD_ACTIVE_RIGHT_TEXTURE = new ImageTexture(HC.id("textures/widget/tab_right_active.png"));
	public static final Texture STANDARD_INACTIVE_LEFT_TEXTURE = new ImageTexture(HC.id("textures/widget/tab_left_inactive.png"));
	public static final Texture STANDARD_INACTIVE_MIDDLE_TEXTURE = new ImageTexture(HC.id("textures/widget/tab_middle_inactive.png"));
	public static final Texture STANDARD_INACTIVE_RIGHT_TEXTURE = new ImageTexture(HC.id("textures/widget/tab_right_inactive.png"));
	
	protected Supplier<Texture> activeLeftTexture = () -> STANDARD_ACTIVE_LEFT_TEXTURE;
	protected Supplier<Texture> activeMiddleTexture = () -> STANDARD_ACTIVE_MIDDLE_TEXTURE;
	protected Supplier<Texture> activeRightTexture = () -> STANDARD_ACTIVE_RIGHT_TEXTURE;
	
	protected Supplier<Texture> inactiveLeftTexture = () -> STANDARD_INACTIVE_LEFT_TEXTURE;
	protected Supplier<Texture> inactiveMiddleTexture = () -> STANDARD_INACTIVE_MIDDLE_TEXTURE;
	protected Supplier<Texture> inactiveRightTexture = () -> STANDARD_INACTIVE_RIGHT_TEXTURE;
	
	protected Supplier<Texture> texture = () -> PanelWidget.STANDARD_TEXTURE;
	
	protected List<Shape> tabRectangles = new ArrayList<>();
	protected List<TabCollection> tabCollections = new ArrayList<>();
	protected List<Supplier<ItemStack>> tabSymbols = new ArrayList<>();
	protected List<Supplier<List<Text>>> tabTooltips = new ArrayList<>();
	
	protected Collection<Widget> children = new ArrayList<>();
	
	protected int selected = 0;
	
	public TabWidget() {
		setTooltipSupplier(() -> {
			var tabIndex = 0;
			
			for (var tabRectangle : tabRectangles) {
				if (tabRectangle.isPositionWithin(PositionUtil.getMousePosition())) {
					if (tabTooltips.size() >= tabIndex) {
						return tabTooltips.get(tabIndex).get();
					}
				}
				
				tabIndex += 1;
			}
			
			return ImmutableList.of();
		});
	}
	
	@Override
	public Size getStandardSize() {
		return new Size(Math.max(128.0F, tabCollections.size() * 26.0F), 96.0F);
	}
	
	public TabCollection addTab(Supplier<ItemStack> symbol) {
		return addTab(symbol, () -> null);
	}
	
	public TabCollection addTab(Supplier<ItemStack> symbol, Supplier<List<Text>> tooltip) {
		var collection = new TabCollection(tabCollections.size());
		collection.setRootCollection(rootCollection);
		collection.setCollection(this);
		collection.setParent(this);
		
		tabCollections.add(collection);
		tabSymbols.add(symbol);
		tabTooltips.add(tooltip);
		
		tabRectangles.clear();
		
		var tabIndex = 0;
		
		for (var tabCollection : tabCollections) {
			tabRectangles.add(new Shape.Rectangle2D(26.0F, 25.0F).translate(getX() + tabIndex * 26.0F, getY()));
			
			tabIndex += 1;
		}
		
		return collection;
	}
	
	@Override
	protected void onMouseClicked(MouseClickedEvent event) {
		super.onMouseClicked(event);
		
		var pos = new Position(event.x(), event.y());
		
		Shape focusedTabRectangle = null;
		
		for (var tabRectangle : tabRectangles) {
			if (tabRectangle.isPositionWithin(pos)) {
				focusedTabRectangle = tabRectangle;
				
				break;
			}
		}
		
		if (focusedTabRectangle != null) {
			var tabIndex = 0;
			
			for (var tabRectangle : tabRectangles) {
				var hidden = tabRectangle != focusedTabRectangle;
				
				for (var widget : tabCollections.get(tabIndex).getChildren()) {
					widget.setHidden(hidden);
				}
				
				if (!hidden) {
					selected = tabIndex;
				}
				
				tabIndex += 1;
			}
		}
	}
	
	@Override
	protected void onLayoutChanged(LayoutChangedEvent event) {
		super.onLayoutChanged(event);
		
		children.clear();

		tabRectangles.clear();
		
		var tabIndex = 0;
		
		for (var tabCollection : tabCollections) {
			tabRectangles.add(new Shape.Rectangle2D(26.0F, 25.0F).translate(getX() + tabIndex * 26.0F, getY()));

			children.addAll(tabCollection.getChildren());
			
			tabCollection.setY(getY());
			
			tabIndex += 1;
		}
	}
	
	@Override
	protected void onAdded(AddedEvent event) {
		super.onAdded(event);
		
		var tabIndex = 0;
		
		for (var tabCollection : tabCollections) {
			tabCollection.setRootCollection(rootCollection);
			tabCollection.setCollection(this);
			tabCollection.setParent(this);
			
			if (tabIndex != 0) {
				for (var widget : tabCollection.getChildren()) {
					widget.setHidden(true);
				}
			}
		}
	}
	
	@Override
	public boolean shouldSync(EventType type) {
		return type == EventType.MOUSE_CLICKED;
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
		
		var itemRenderer = DrawingUtil.getItemRenderer();
		
		texture.get().draw(matrices, provider, getX(), getY() + 25.0F, getWidth(), getHeight() - 25.0F);
		
		// In 1.20.1, it is an Immediate by default.
		// if (provider instanceof VertexConsumerProvider.Immediate immediate) {
			provider.draw();
		// }
		
		var tabIndex = 0;
		
		for (var tabRectangle : tabRectangles) {
			if (selected == tabIndex) {
				if (tabRectangle.getStartPos().getX() == getX()) {
					activeLeftTexture.get().draw(matrices, provider, getX() + (26.0F * tabIndex), getY(), 25.0F, 29.0F);
				} else if (tabRectangle.getStartPos().getX() == getX() + getWidth() - 25.0F) {
					activeRightTexture.get().draw(matrices, provider, getX() + (26.0F * tabIndex), getY(), 25.0F, 28.0F);
				} else {
					activeMiddleTexture.get().draw(matrices, provider, getX() + (26.0F * tabIndex), getY(), 25.0F, 28.0F);
				}
			} else {
				if (tabRectangle.getStartPos().getX() == getX()) {
					inactiveLeftTexture.get().draw(matrices, provider, getX() + (26.0F * tabIndex), getY() + 3.0F, 25.0F, 24.0F);
				} else if (tabRectangle.getStartPos().getX() == getX() + getWidth() - 25.0F) {
					inactiveRightTexture.get().draw(matrices, provider, getX() + (26.0F * tabIndex), getY() + 2.0F, 25.0F, 26.0F);
				} else {
					inactiveMiddleTexture.get().draw(matrices, provider, getX() + (26.0F * tabIndex), getY() + 2.0F, 25.0F, 25.0F);
				}
			}
			
			
			context.drawItem(tabSymbols.get(tabIndex).get(), (int) (getX() + (26.0F * tabIndex) + 4.5F), (int) (getY() + 7));
			
			tabIndex += 1;
		}
		
		for (var child : getChildren()) {
			if (!child.isHidden()) {
				child.draw(context, tickDelta);
			}
		}
	}
	
	public static class TabCollection extends Widget implements WidgetCollection {
		protected Collection<Widget> children = new ArrayList<>();
		
		protected int number;
		
		public TabCollection(int number) {
			this.number = number;
		}
		
		@Override
		public Size getStandardSize() {
			return new Size(18.0F, 18.0F);
		}
		
		@Override
		public Collection<Widget> getChildren() {
			return children;
		}
		
		@Override
		public void add(Widget child) {
			WidgetCollection.super.add(child);
			
			child.setHidden(child.isHidden() || (parent != null && (parent.isHidden() || ((TabWidget) parent).selected != number)));
		}
		
		@Override
		public void draw(DrawContext context, float tickDelta) {
			// draw lines around the border using context, remember to cast to int
			context.drawHorizontalLine((int) getX(), (int) (getX() + getWidth()), (int) getY(), 0xFF000000);
			context.drawHorizontalLine((int) getX(), (int) (getX() + getWidth()), (int) (getY() + getHeight()), 0xFF000000);
			context.drawVerticalLine((int) getX(), (int) getY(), (int) (getY() + getHeight()), 0xFF000000);
			context.drawVerticalLine((int) (getX() + getWidth()), (int) getY(), (int) (getY() + getHeight()), 0xFF000000);
			
			for (var child : getChildren()) {
				if (!child.isHidden()) {
					child.draw(context, tickDelta);
				}
			}
		}
	}
	
	@Override
	public Supplier<Texture> getActiveLeftTexture() {
		return activeLeftTexture;
	}
	
	@Override
	public void setActiveLeftTexture(Supplier<Texture> activeLeftTexture) {
		this.activeLeftTexture = activeLeftTexture;
	}
	
	@Override
	public Supplier<Texture> getActiveMiddleTexture() {
		return activeMiddleTexture;
	}
	
	@Override
	public void setActiveMiddleTexture(Supplier<Texture> activeMiddleTexture) {
		this.activeMiddleTexture = activeMiddleTexture;
	}
	
	@Override
	public Supplier<Texture> getActiveRightTexture() {
		return activeRightTexture;
	}
	
	@Override
	public void setActiveRightTexture(Supplier<Texture> activeRightTexture) {
		this.activeRightTexture = activeRightTexture;
	}
	
	@Override
	public Supplier<Texture> getInactiveLeftTexture() {
		return inactiveLeftTexture;
	}
	
	@Override
	public void setInactiveLeftTexture(Supplier<Texture> inactiveLeftTexture) {
		this.inactiveLeftTexture = inactiveLeftTexture;
	}
	
	@Override
	public Supplier<Texture> getInactiveMiddleTexture() {
		return inactiveMiddleTexture;
	}
	
	@Override
	public void setInactiveMiddleTexture(Supplier<Texture> inactiveMiddleTexture) {
		this.inactiveMiddleTexture = inactiveMiddleTexture;
	}
	
	@Override
	public Supplier<Texture> getInactiveRightTexture() {
		return inactiveRightTexture;
	}
	
	@Override
	public void setInactiveRightTexture(Supplier<Texture> inactiveRightTexture) {
		this.inactiveRightTexture = inactiveRightTexture;
	}
	
	@Override
	public Supplier<Texture> getTexture() {
		return texture;
	}
	
	@Override
	public void setTexture(Supplier<Texture> texture) {
		this.texture = texture;
	}
}
