package dev.vini2003.hammer.gui.api.common.widget.list.slot;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.scissor.Scissors;
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.PositionUtil;
import dev.vini2003.hammer.core.api.common.cache.Cached;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.shape.Shape;
import dev.vini2003.hammer.core.api.common.math.shape.modifier.TranslateModifier;
import dev.vini2003.hammer.core.api.common.supplier.TextureSupplier;
import dev.vini2003.hammer.gui.api.common.event.*;
import dev.vini2003.hammer.gui.api.common.event.annotation.EventSubscriber;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.Collection;

public class SlotListWidget extends Widget implements WidgetCollection {
	public static final Texture STANDARD_SCROLLBAR_TEXTURE = new PartitionedTexture(HC.id("textures/widget/scrollbar.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.16F);
	public static final Texture STANDARD_SCROLLER_TEXTURE = new PartitionedTexture(HC.id("textures/widget/scroller.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.11F);
	public static final Texture STANDARD_FOCUSED_SCROLLER_TEXTURE = new PartitionedTexture(HC.id("textures/widget/scroller_focus.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.11F);
	
	protected TextureSupplier scrollbarTexture = () -> STANDARD_SCROLLBAR_TEXTURE;
	protected TextureSupplier scrollerTexture = () -> STANDARD_SCROLLER_TEXTURE;
	protected TextureSupplier focusedScrollerTexture = () -> STANDARD_FOCUSED_SCROLLER_TEXTURE;
	
	protected final Inventory inventory;
	
	protected int widthInSlots = 0;
	protected int heightInSlots = 0;
	
	protected int maxSlots = 0;
	
	protected Collection<Widget> widgets = new ArrayList<>();
	
	protected int row = 0;
	
	protected boolean scrollerHeld = false;
	
	protected Cached<Float> scrollerHeight = new Cached<>(() -> Math.min(getHeight() - 2.0F, (float) heightInSlots / (float) getTotalRows() * (getHeight() - 2.0F)));
	
	protected Cached<Float> scrollerY = new Cached<>(() -> Math.max(getY() + 2.0F, Math.min(getY() + getHeight() - scrollerHeight.get(), (float) row / (float) getTotalRows() * getHeight() + getY() + 1.0F)));
	
	protected Cached<Shape> scrollerRectangle = new Cached<>(() -> {
		return new Shape.ScreenRectangle(16.0F, scrollerHeight.get()).translate(getX() + getWidth() - 1.0F - 16.0F, scrollerY.get() - 1.0F, 0.0F);
	});
	
	protected Cached<Shape> scrollbarRectangle = new Cached<>(() -> {
		return new Shape.ScreenRectangle(16.0F, getHeight() - 2.0F).translate(getX() + getWidth() - 1.0F - 16.0F, getY() + 1.0F, 0.0F);
	});
	
	protected int getBottomRow() {
		return maxSlots - heightInSlots;
	}
	
	protected int getTotalRows() {
		return inventory.size() / widthInSlots;
	}
	
	public SlotListWidget(Inventory inventory, int widthInSlots, int heightInSlots, int maxSlots) {
		super();
		
		this.inventory = inventory;
		
		this.widthInSlots = widthInSlots;
		this.heightInSlots = heightInSlots;
		
		this.maxSlots = maxSlots;
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
					dispatchEvent(new MouseScrolledEvent(event.x(), event.y(), - 1.0D));
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
			} else if (event.deltaY() < 0.0D){
				dispatchEvent(new MouseScrolledEvent(event.x(), event.y(), -event.deltaY()));
			}
		}
	}
	
	@Override
	protected void onMouseScrolled(MouseScrolledEvent event) {
		super.onMouseScrolled(event);
		
		if (focused || scrollerHeld) {
			if (event.deltaY() > 0.0D && row > 0) {
				--row;
				
				for (var widget : getChildren()) {
					var slotWidget = (SlotWidget) widget;
					
					var slot = slotWidget.getSlot();
					
					if (slot.index - widthInSlots >= 0) {
						slot.index = slot.index - widthInSlots;
					}
					
					rootCollection.getScreenHandler().sendContentUpdates();
				}
			} else if (event.deltaY() <= 0.0D && row < getBottomRow()) {
				++row;
				
				for (var widget : getChildren()) {
					var slotWidget = (SlotWidget) widget;
					
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
		return widgets;
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
		
		for (var widget : getChildren()) {
			if (!widget.isHidden()) {
				widget.draw(matrices, provider, tickDelta);
			}
		}
		
		scissors.destroy();
	}
}
