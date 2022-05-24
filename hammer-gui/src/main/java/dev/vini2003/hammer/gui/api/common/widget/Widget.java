package dev.vini2003.hammer.gui.api.common.widget;

import dev.vini2003.hammer.core.api.client.util.PositionUtil;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.position.Positioned;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.core.api.common.math.size.Sized;
import dev.vini2003.hammer.core.api.common.tick.Tickable;
import dev.vini2003.hammer.gui.api.common.event.*;
import dev.vini2003.hammer.gui.api.common.event.base.Event;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import dev.vini2003.hammer.gui.api.common.listener.EventListener;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.*;
import java.util.function.Supplier;

public abstract class Widget implements Positioned, Sized, EventListener, Tickable {
	protected Position position;
	protected Size size;
	
	protected WidgetCollection collection;
	protected WidgetCollection.Root rootCollection;
	
	protected Widget parent;
	
	protected Supplier<List<Text>> tooltipSupplier;
	
	protected boolean hidden = false;
	protected boolean focused = false;
	protected boolean held = false;
	protected boolean locking = false;
	
	protected Map<EventType, Collection<EventListener<?>>> listeners = new HashMap<>();
	
	protected Widget() {
		onEvent(EventType.ADDED, this::onAdded);
		onEvent(EventType.REMOVED, this::onRemoved);
		onEvent(EventType.FOCUS_GAINED, this::onFocusGained);
		onEvent(EventType.FOCUS_RELEASED, this::onFocusReleased);
		onEvent(EventType.KEY_PRESSED, this::onKeyPressed);
		onEvent(EventType.KEY_RELEASED, this::onKeyReleased);
		onEvent(EventType.CHARACTER_TYPED, this::onCharacterTyped);
		onEvent(EventType.MOUSE_CLICKED, this::onMouseClicked);
		onEvent(EventType.MOUSE_DRAGGED, this::onMouseDragged);
		onEvent(EventType.MOUSE_MOVED, this::onMouseMoved);
		onEvent(EventType.MOUSE_RELEASED, this::onMouseReleased);
		onEvent(EventType.MOUSE_SCROLLED, this::onMouseScrolled);
		onEvent(EventType.LAYOUT_CHANGED, this::onLayoutChanged);
	}
	
	protected void onAdded(AddedEvent event) {
		rootCollection = event.rootCollection();
		collection = event.collection();
	}
	
	protected void onRemoved(RemovedEvent event) {
		rootCollection = null;
		collection = null;
	}
	
	protected void onFocusGained(FocusGainedEvent event) {
	
	}
	
	protected void onFocusReleased(FocusReleasedEvent event) {
	
	}
	
	protected void onKeyPressed(KeyPressedEvent event) {
	
	}
	
	protected void onKeyReleased(KeyReleasedEvent event) {
	
	}
	
	protected void onCharacterTyped(CharacterTypedEvent event) {
	
	}
	
	protected void onMouseClicked(MouseClickedEvent event) {
		setHeld(focused);
	}
	
	protected void onMouseDragged(MouseDraggedEvent event) {
	
	}
	
	protected void onMouseMoved(MouseMovedEvent event) {
		updateFocus(event.x(), event.y());
	}
	
	protected void onMouseReleased(MouseReleasedEvent event) {
		setHeld(false);
	}
	
	protected void onMouseScrolled(MouseScrolledEvent event) {
	
	}
	
	protected void onLayoutChanged(LayoutChangedEvent event) {
		updateFocus(PositionUtil.getMouseX(), PositionUtil.getMouseY());
	}
	
	/**
	 * <p>Returns whether the given event type should be synced.</p>
	 *
	 * @param type the type.
	 *
	 * @return the result.
	 */
	public boolean shouldSync(EventType type) {
		return false;
	}
	
	/**
	 * <p>Adds a listener for a given event type to this widget.</p>
	 *
	 * @param type     the type.
	 * @param listener the listener.
	 */
	public <T extends Event> void onEvent(EventType type, EventListener<T> listener) {
		listeners.computeIfAbsent(type, ($) -> new ArrayList<>());
		listeners.get(type).add(listener);
	}
	
	/**
	 * <p>Returns all listeners of for given event type in this widget.</p>
	 *
	 * @param type the type.
	 *
	 * @return the listeners.
	 */
	public <T extends Event> Collection<EventListener<T>> getListeners(EventType type) {
		listeners.computeIfAbsent(type, ($) -> new ArrayList<>());
		
		return (Collection) listeners.get(type);
	}
	
	/**
	 * <p>Called when an event is dispatched.</p>
	 *
	 * <p>Syncs it if this widget is attached to a screen or screen handler,
	 * and is running on the client.</p>
	 *
	 * @param event the event.
	 */
	@Override
	public void dispatchEvent(Event event) {
		if (shouldSync(event.type()) && isFocused() && rootCollection != null && rootCollection.isScreenHandler()) {
		
		}
		
		for (var listener : getListeners(event.type())) {
			listener.dispatchEvent(event);
		}
		
		if (this instanceof WidgetCollection collection && (rootCollection == null || rootCollection.isClient())) {
			for (var child : collection.getChildren()) {
				child.dispatchEvent(event);
			}
		}
	}
	
	/**
	 * Updates whether the widget is focused or not.
	 */
	protected void updateFocus(float x, float y) {
		var wasFocused = isFocused();
		var isFocused = isPointWithin(x, y);
		
		setFocused(isFocused);
		
		if (wasFocused && !isFocused) {
			dispatchEvent(new FocusReleasedEvent());
		}
		
		if (!wasFocused && isFocused) {
			dispatchEvent(new FocusGainedEvent());
		}
	}
	
	/**
	 * Returns whether a point is within this widget's boundaries or not.
	 *
	 * @param x the point's X component.
	 * @param y the point's Y component.
	 *
	 * @return the result.
	 */
	public boolean isPointWithin(float x, float y) {
		return x > getX() && x < getX() + getWidth() && y > getY() && y < getY() + getHeight();
	}
	
	/**
	 * Draws this widget.
	 *
	 * @param matrices  the position and normal matrices.
	 * @param provider  the buffer provider.
	 * @param tickDelta the time elapsed since the last tick.
	 */
	public abstract void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta);
	
	/**
	 * Returns this widget's tooltips.
	 *
	 * @return the tooltips.
	 */
	public Collection<Text> getTooltips() {
		if (tooltipSupplier != null) {
			return tooltipSupplier.get();
		}
		
		return new ArrayList<>();
	}
	
	@Override
	public Position getPosition() {
		return position;
	}
	
	@Override
	public void setPosition(Position position) {
		this.position = position;
		
		dispatchEvent(new LayoutChangedEvent());
	}
	
	@Override
	public Size getSize() {
		return size;
	}
	
	@Override
	public void setSize(Size size) {
		this.size = size;
		
		dispatchEvent(new LayoutChangedEvent());
	}
	
	public WidgetCollection getCollection() {
		return collection;
	}
	
	public void setCollection(WidgetCollection collection) {
		this.collection = collection;
	}
	
	public WidgetCollection.Root getRootCollection() {
		return rootCollection;
	}
	
	public void setRootCollection(WidgetCollection.Root rootCollection) {
		this.rootCollection = rootCollection;
	}
	
	public Widget getParent() {
		return parent;
	}
	
	public void setParent(Widget parent) {
		this.parent = parent;
	}
	
	public Supplier<List<Text>> getTooltipSupplier() {
		return tooltipSupplier;
	}
	
	public void setTooltipSupplier(Supplier<List<Text>> tooltipSupplier) {
		this.tooltipSupplier = tooltipSupplier;
	}
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public void setFocused(boolean focused) {
		this.focused = focused;
	}
	
	public boolean isFocused() {
		return focused;
	}
	
	public void setHeld(boolean held) {
		this.held = held;
	}
	
	public boolean isHeld() {
		return held;
	}
	
	public void setLocking(boolean locking) {
		this.locking = locking;
	}
	
	public boolean isLocking() {
		return locking;
	}
}
