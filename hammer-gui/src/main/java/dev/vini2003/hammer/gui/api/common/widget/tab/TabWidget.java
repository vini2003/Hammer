package dev.vini2003.hammer.gui.api.common.widget.tab;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.PositionUtil;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.shape.Shape;
import dev.vini2003.hammer.core.api.common.supplier.ItemStackSupplier;
import dev.vini2003.hammer.core.api.common.supplier.ItemSupplier;
import dev.vini2003.hammer.core.api.common.supplier.TextureSupplier;
import dev.vini2003.hammer.core.api.common.supplier.TooltipSupplier;
import dev.vini2003.hammer.gui.api.common.event.AddedEvent;
import dev.vini2003.hammer.gui.api.common.event.LayoutChangedEvent;
import dev.vini2003.hammer.gui.api.common.event.MouseClickedEvent;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import dev.vini2003.hammer.gui.api.common.widget.panel.PanelWidget;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TabWidget extends Widget implements WidgetCollection {
	public static final Texture STANDARD_ACTIVE_LEFT_TEXTURE = new ImageTexture(HC.id("textures/widget/tab_left_active.png"));
	public static final Texture STANDARD_ACTIVE_MIDDLE_TEXTURE = new ImageTexture(HC.id("textures/widget/tab_middle_active.png"));
	public static final Texture STANDARD_ACTIVE_RIGHT_TEXTURE = new ImageTexture(HC.id("textures/widget/tab_right_active.png"));
	public static final Texture STANDARD_INACTIVE_LEFT_TEXTURE = new ImageTexture(HC.id("textures/widget/tab_left_inactive.png"));
	public static final Texture STANDARD_INACTIVE_MIDDLE_TEXTURE = new ImageTexture(HC.id("textures/widget/tab_middle_inactive.png"));
	public static final Texture STANDARD_INACTIVE_RIGHT_TEXTURE = new ImageTexture(HC.id("textures/widget/tab_right_inactive.png"));
	
	protected TextureSupplier activeLeftTexture = () -> STANDARD_ACTIVE_LEFT_TEXTURE;
	protected TextureSupplier activeMiddleTexture = () -> STANDARD_ACTIVE_MIDDLE_TEXTURE;
	protected TextureSupplier activeRightTexture = () -> STANDARD_ACTIVE_RIGHT_TEXTURE;
	
	protected TextureSupplier inactiveLeftTexture = () -> STANDARD_INACTIVE_LEFT_TEXTURE;
	protected TextureSupplier inactiveMiddleTexture = () -> STANDARD_INACTIVE_MIDDLE_TEXTURE;
	protected TextureSupplier inactiveRightTexture = () -> STANDARD_INACTIVE_RIGHT_TEXTURE;
	
	protected TextureSupplier panelTexture = () -> PanelWidget.STANDARD_TEXTURE;
	
	protected List<Shape> tabRectangles = new ArrayList<>();
	protected List<TabCollection> tabCollections = new ArrayList<>();
	protected List<ItemStackSupplier> tabSymbols = new ArrayList<>();
	protected List<TooltipSupplier> tabTooltips = new ArrayList<>();
	
	protected Collection<Widget> widgets = new ArrayList<>();
	
	protected int selected = 0;
	
	public TabWidget() {
		setTooltipSupplier(() -> {
			var tabIndex = 0;
			
			for (var tabRectangle : tabRectangles) {
				if (tabRectangle.isPositionWithin(PositionUtil.getMousePosition())) {
					if (tabTooltips.size() >= tabIndex) {
						return tabTooltips.get(tabIndex);
					}
				}
				
				tabIndex += 1;
			}
		});
	}
	
	public TabCollection addTab(ItemSupplier symbol) {
		return addTab(symbol, () -> null);
	}
	
	public TabCollection addTab(ItemSupplier symbol, TooltipSupplier tooltip) {
		return addTab(() -> symbol.get().getDefaultStack(), tooltip);
	}
	
	public TabCollection addTab(ItemStackSupplier symbol) {
		return addTab(symbol, () -> null);
	}
	
	public TabCollection addTab(ItemStackSupplier symbol, TooltipSupplier tooltip) {
		var collection = new TabCollection(tabCollections.size());
		collection.setRootCollection(rootCollection);
		collection.setCollection(this);
		
		tabCollections.add(collection);
		tabSymbols.add(symbol);
		tabTooltips.add(tooltip);
		
		tabRectangles.clear();
		
		var tabIndex = 0;
		
		for (var tabCollection : tabCollections) {
			tabRectangles.add(new Shape.ScreenRectangle(26.0F, 25.0F).translate(tabIndex * 26.0F, 0.0F, 0.0F));
			
			tabIndex += 1;
		}
		
		return collection;
	}
	
	@Override
	protected void onMouseClicked(MouseClickedEvent event) {
		super.onMouseClicked(event);
		
		var pos = new Position(event.x(), event.y());
		
		var focusedTabRectangle = (Shape) null;
		
		for (var tabRectangle : tabRectangles) {
			if (tabRectangle.isPositionWithin(pos)) {
				focusedTabRectangle = tabRectangle;
				
				break;
			}
		}
		
		if (focusedTabRectangle != null) {
			var tabIndex = 0;
			
			for (var tabRectangle : tabRectangles) {
				var hidden = !tabRectangle.isPositionWithin(pos);
				
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
		
		widgets.clear();
		
		for (var tabCollection : tabCollections) {
			widgets.addAll(tabCollection.getChildren());
		}
	}
	
	@Override
	protected void onAdded(AddedEvent event) {
		super.onAdded(event);
		
		var tabIndex = 0;
		
		for (var tabCollection : tabCollections) {
			tabCollection.setRootCollection(rootCollection);
			tabCollection.setCollection(this);
			
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
		return widgets;
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		panelTexture.get().draw(matrices, provider, getX(), getY() + 25.0F, getWidth(), getHeight() - 25.0F);
		
		if (provider instanceof VertexConsumerProvider.Immediate immediate) {
			immediate.draw();
		}
		
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
			
			tabIndex += 1;
		}
	}
	
	class TabCollection extends Widget implements WidgetCollection {
		protected Collection<Widget> widgets;
		
		protected int number;
		
		public TabCollection(int number) {
			this.number = number;
		}
		
		@Override
		public Collection<Widget> getChildren() {
			return widgets;
		}
		
		@Override
		public void add(Widget child) {
			WidgetCollection.super.add(child);
			
			child.setHidden(child.isHidden() || (parent != null && (parent.isHidden() || ((TabWidget) parent).selected != number)));
		}
		
		@Override
		public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {}
	}
}
