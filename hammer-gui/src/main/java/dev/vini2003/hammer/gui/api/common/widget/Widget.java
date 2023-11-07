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

package dev.vini2003.hammer.gui.api.common.widget;

import com.google.common.collect.ImmutableList;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.util.PositionUtil;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.position.StaticPosition;
import dev.vini2003.hammer.core.api.common.math.position.Positioned;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.core.api.common.math.size.Sized;
import dev.vini2003.hammer.core.api.common.tick.Ticks;
import dev.vini2003.hammer.gui.api.common.event.*;
import dev.vini2003.hammer.gui.api.common.event.base.Event;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import dev.vini2003.hammer.gui.api.common.listener.EventListener;
import dev.vini2003.hammer.gui.registry.common.HGUINetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public abstract class Widget implements Positioned, Sized, EventListener, Ticks {
	protected Position position = Position.of(0.0F, 0.0F);
	protected Size size = Size.of(0.0F, 0.0F);
	
	protected WidgetCollection collection;
	protected WidgetCollection.Root rootCollection;
	
	protected Widget parent;
	
	protected Supplier<List<OrderedText>> tooltip = ImmutableList::of;
	
	protected boolean hidden = false;
	protected boolean focused = false;
	protected boolean held = false;
	protected boolean locking = false;
	
	protected Map<EventType, Collection<EventListener<?>>> listeners = new HashMap<>();
	
	/**
	 * The widget's ID is used to identify it when
	 * synchronizing events between the client and the server.
	 */
	@Nullable
	protected String id;
	
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
	
	}
	
	protected void onMouseDragged(MouseDraggedEvent event) {
	
	}
	
	protected void onMouseMoved(MouseMovedEvent event) {
		updateFocus(event.x(), event.y());
	}
	
	protected void onMouseReleased(MouseReleasedEvent event) {
	
	}
	
	protected void onMouseScrolled(MouseScrolledEvent event) {
	
	}
	
	protected void onLayoutChanged(LayoutChangedEvent event) {
		if (rootCollection != null && rootCollection.isClient()) {
			updateFocus(PositionUtil.getMouseX(), PositionUtil.getMouseY());
		}
	}
	
	/**
	 * <p>Returns whether the given event type should be synced.</p>
	 *
	 * @param type the type.
	 *
	 * @return the valid.
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
		if (!allowsEvents()) {
			return;
		}
		
		var rootCollection = getRootCollection();
		
		// Always propagate Layout Changed, as some widgets may
		// handle special logic in there that will not run on the server
		// if it is not propagated correctly.
		if ((shouldSync(event.type()) || event.type() == EventType.LAYOUT_CHANGED) && (isFocused() || rootCollection != null && rootCollection.isScreenHandler() && rootCollection.isClient())) {
			if (id == null) {
				HC.LOGGER.debug("A widget was not given an ID, but tried to synchronize an event!");
				
				return;
			}
			
			var buf = PacketByteBufs.create();
			buf.writeString(id);
			
			event.writeToBuf(buf);
			
			ClientPlayNetworking.send(HGUINetworking.SYNC_WIDGET_EVENT, buf);
		}
		
		for (var listener : getListeners(event.type())) {
			listener.dispatchEvent(event);
		}
		
		if (this instanceof WidgetCollection collection && (rootCollection == null || rootCollection.isClient())) {
			if (event.type() != EventType.ADDED && event.type() != EventType.REMOVED) {
				for (var child : collection.getChildren()) {
					child.dispatchEvent(event);
				}
			}
		}
	}
	
	/**
	 * Updates whether the widget is focused or not.
	 */
	protected void updateFocus(float x, float y) {
		var wasFocused = isFocused();
		var isFocused = isPointWithin(x, y);
		
		var isBelowOtherWidget = false;
		
		for (var child : getRootCollection().getAllChildren()) {
			if (child == this) {
				continue;
			}
			
			if (child.isPointWithin(x, y) && child.getZ() > getZ()) {
				isBelowOtherWidget = true;
				
				break;
			}
		}
		
		if (isBelowOtherWidget) {
			isFocused = false;
		}
		
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
	 * @return the valid.
	 */
	public boolean isPointWithin(float x, float y) {
		return x > getX() && x < getX() + getWidth() && y > getY() && y < getY() + getHeight();
	}
	
	/**
	 * Configures transforms before drawing.
	 * Useful for translating to the correct Z layer.
	 *
	 * @param context	the draw context.
	 * @param tickDelta the tick delta.
	 */
	@Environment(EnvType.CLIENT)
	public void onBeginDraw(DrawContext context, float tickDelta) {
		context.getMatrices().push();
		context.getMatrices().translate(0.0F, 0.0F, getZ());
	}
	
	/**
	 * Reverts transforms after drawing.
	 *
	 * @param context	the draw context.
	 * @param tickDelta the tick delta.
	 */
	@Environment(EnvType.CLIENT)
	public void onEndDraw(DrawContext context, float tickDelta) {
		context.getMatrices().pop();
	}
	
	/**
	 * Draws this widget.
	 *
	 * @param context	the draw context.
	 * @param tickDelta	the tick delta.
	 */
	@Environment(EnvType.CLIENT)
	public void draw(DrawContext context, float tickDelta) {
		onBeginDraw(context, tickDelta);
		
		if (this instanceof WidgetCollection collection) {
			for (var child : collection.getChildren()) {
				if (!child.isHidden()) {
					child.draw(context, tickDelta);
				}
			}
		}
		
		onEndDraw(context, tickDelta);
	}
	
	@Deprecated
	public List<Text> getTooltips() {
		return Collections.emptyList();
	}
	
	/**
	 * Returns this widget's tooltips.
	 *
	 * @return the tooltips.
	 */
	public List<OrderedText> getTooltip() {
		if (tooltip != null) {
			return tooltip.get();
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
		if (size.getWidth() == 0.0F && size.getHeight() == 0.0F) {
			size = getStandardSize();
		}
		
		return size;
	}
	
	@Override
	public void setSize(Size size) {
		this.size = size;
		
		dispatchEvent(new LayoutChangedEvent());
	}
	
	@Nullable
	public String getId() {
		return this.id;
	}
	
	public void setId(@Nullable String id) {
		this.id = id;
	}
	
	public boolean allowsEvents() {
		return !(this instanceof WidgetCollection.Root) || collection != null || rootCollection != null;
	}
	
	public WidgetCollection getCollection() {
		return collection;
	}
	
	public void setCollection(WidgetCollection collection) {
		this.collection = collection;
	}
	
	public WidgetCollection.Root getRootCollection() {
		if (this instanceof WidgetCollection.Root rootCollection) {
			return rootCollection;
		}
		
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
	
	public void setTooltip(Supplier<List<OrderedText>> tooltip) {
		this.tooltip = tooltip;
	}
	
	@Deprecated
	public void setTooltipSupplier(Supplier<List<Text>> tooltipSupplier) {
		if (tooltipSupplier.get() != null) {
			this.tooltip = () -> tooltipSupplier.get().stream().map(Text::asOrderedText).toList();
		}
	}
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	public boolean isHidden() {
		if (parent != null) {
			return parent.isHidden() || hidden;
		} else {
			return hidden;
		}
	}
	
	public void setFocused(boolean focused) {
		this.focused = focused;
	}
	
	public boolean isFocused() {
		if (parent != null) {
			return !parent.isHidden() && !isHidden() && focused;
		} else {
			return !isHidden() && focused;
		}
	}
	
	public void setHeld(boolean held) {
		this.held = held;
	}
	
	public boolean isHeld() {
		if (parent != null) {
			return !parent.isHidden() && !isHidden() && held;
		} else {
			return !isHidden() && held;
		}
	}
	
	public void setLocking(boolean locking) {
		this.locking = locking;
	}
	
	public boolean isLocking() {
		return locking;
	}
}
