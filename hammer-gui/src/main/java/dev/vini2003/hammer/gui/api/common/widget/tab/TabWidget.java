package dev.vini2003.hammer.gui.api.common.widget.tab;

import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.ImageTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.PositionUtil;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.shape.Shape;
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
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class TabWidget extends Widget implements WidgetCollection {
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
	
	protected Supplier<Texture> panelTexture = () -> PanelWidget.STANDARD_TEXTURE;
	
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
	
	public TabCollection addTab(Supplier<ItemStack> symbol) {
		return addTab(symbol, () -> null);
	}
	
	public TabCollection addTab(Supplier<ItemStack> symbol, Supplier<List<Text>> tooltip) {
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
		
		children.clear();
		
		for (var tabCollection : tabCollections) {
			children.addAll(tabCollection.getChildren());
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
		return children;
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
		protected Collection<Widget> children;
		
		protected int number;
		
		public TabCollection(int number) {
			this.number = number;
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
		public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {}
	}
	
	public void setActiveLeftTexture(Supplier<Texture> activeLeftTexture) {
		this.activeLeftTexture = activeLeftTexture;
	}
	
	public void setActiveLeftTexture(Texture activeLeftTexture) {
		setActiveLeftTexture(() -> activeLeftTexture);
	}
	
	public void setActiveMiddleTexture(Supplier<Texture> activeMiddleTexture) {
		this.activeMiddleTexture = activeMiddleTexture;
	}
	
	public void setActiveMiddleTexture(Texture activeMiddleTexture) {
		setActiveMiddleTexture(() -> activeMiddleTexture);
	}
	
	public void setActiveRightTexture(Supplier<Texture> activeRightTexture) {
		this.activeRightTexture = activeRightTexture;
	}
	
	public void setActiveRightTexture(Texture activeRightTexture) {
		setActiveRightTexture(() -> activeRightTexture);
	}
	
	public void setInactiveLeftTexture(Supplier<Texture> inactiveLeftTexture) {
		this.inactiveLeftTexture = inactiveLeftTexture;
	}
	
	public void setInactiveLeftTexture(Texture inactiveLeftTexture) {
		setInactiveLeftTexture(() -> inactiveLeftTexture);
	}
	
	public void setInactiveMiddleTexture(Supplier<Texture> inactiveMiddleTexture) {
		this.inactiveMiddleTexture = inactiveMiddleTexture;
	}
	
	public void setInactiveMiddleTexture(Texture inactiveMiddleTexture) {
		setInactiveMiddleTexture(() -> inactiveMiddleTexture);
	}
	
	public void setInactiveRightTexture(Supplier<Texture> inactiveRightTexture) {
		this.inactiveRightTexture = inactiveRightTexture;
	}
	
	public void setInactiveRightTexture(Texture inactiveRightTexture) {
		setInactiveRightTexture(() -> inactiveRightTexture);
	}
}
