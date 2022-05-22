package dev.vini2003.hammer.gui.api.common.widget.list;

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
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.Collection;

public class ListWidget extends Widget implements WidgetCollection {
	public static final Texture STANDARD_SCROLLBAR_TEXTURE = new PartitionedTexture(HC.id("textures/widget/scrollbar.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.16F);
	public static final Texture STANDARD_SCROLLER_TEXTURE = new PartitionedTexture(HC.id("textures/widget/scroller.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.11F);
	public static final Texture STANDARD_FOCUSED_SCROLLER_TEXTURE = new PartitionedTexture(HC.id("textures/widget/scroller_focus.png"), 18.0F, 18.0F, 0.11F, 0.11F, 0.11F, 0.11F);
	
	protected TextureSupplier scrollbarTexture = () -> STANDARD_SCROLLBAR_TEXTURE;
	protected TextureSupplier scrollerTexture = () -> STANDARD_SCROLLER_TEXTURE;
	protected TextureSupplier focusedScrollerTexture = () -> STANDARD_FOCUSED_SCROLLER_TEXTURE;
	
	protected Collection<Widget> widgets = new ArrayList<>();
	
	protected boolean scrollerHeld = false;
	
	protected Cached<Float> maxY = new Cached<>(() -> {
		var maxY = 0.0F;
		
		for (var widget : widgets) {
			if (widget.getY() + widget.getHeight() > maxY) {
				maxY = widget.getY() + widget.getHeight();
			}
		}
		
		return maxY;
	});
	
	protected Cached<Float> minY = new Cached<>(() -> {
		var minY = 0.0F;
		
		for (var widget : widgets) {
			if (widget.getY() < minY) {
				minY = widget.getY();
			}
		}
		
		return minY;
	});
	
	protected Cached<Float> scrollerHeight = new Cached<>(() -> {
		var height = 0.0F;
		
		for (var widget : widgets) {
			height += widget.getHeight();
		}
		
		return Math.min(getHeight() - 2.0F, getHeight() / height * getHeight());
	});
	
	protected Cached<Float> scrollerY = new Cached<>(() -> {
		return Math.max(getY() + 2.0F, Math.min(getY() + getHeight() - scrollerHeight.get(), (Math.abs(getY() - minY.get()) / (maxY.get() - minY.get()) * (getHeight() + scrollerHeight.get()) + getY() + 1.0F)));
	});
	
	protected Cached<Shape> scrollerRectangle = new Cached<>(() -> {
		return new Shape.ScreenRectangle(16.0F, scrollerHeight.get()).translate(getX() + getWidth() - 1.0F - 16.0F, scrollerY.get() - 1.0F, 0.0F);
	});
	
	protected Cached<Shape> scrollbarRectangle = new Cached<>(() -> {
		return new Shape.ScreenRectangle(16.0F, getHeight() - 2.0F).translate(getX() + getWidth() - 1.0F - 16.0F, getY() + 1.0F, 0.0F);
	});
	
	@Override
	public Collection<Widget> getChildren() {
		return widgets;
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
			if (!widgets.isEmpty()) {
				if (event.deltaY() > 0.0D && minY.get() < getY() + 2.0F) {
					for (var widget : getAllChildren()) {
						widget.setY(widget.getY() + (float) event.deltaY() * 2.5F);
						widget.dispatchEvent(new LayoutChangedEvent());
						
						if (widget.getY() >= getY() + getHeight()) {
							widget.setHidden(true);
						} else if (widget.getY() >= getY()) {
							widget.setHidden(false);
						}
					}
					
					// This was inside the loop!
					dispatchEvent(new LayoutChangedEvent());
				} else if (event.deltaY() <= 0.0D && maxY.get() >= getY() + getHeight() - 2.0F) {
					for (var widget : getAllChildren()) {
						widget.setY(widget.getY() + (float) event.deltaY() * 2.5F);
						widget.dispatchEvent(new LayoutChangedEvent());
						
						if (widget.getY() >= getY() + getHeight()) {
							widget.setHidden(true);
						} else if (widget.getY() >= getY()) {
							widget.setHidden(false);
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
		
		for (var widget : getChildren()) {
			if (!widget.isHidden()) {
				widget.draw(matrices, provider, tickDelta);
			}
		}
		
		scissors.destroy();
	}
}
