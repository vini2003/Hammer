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

import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.event.AddedEvent;
import dev.vini2003.hammer.gui.api.common.event.MouseScrolledEvent;
import dev.vini2003.hammer.gui.api.common.event.type.EventType;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import dev.vini2003.hammer.gui.api.common.widget.slot.SlotWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.Collection;

public class SlotListWidget extends Widget implements WidgetCollection {
	protected final Inventory inventory;
	
	protected int widthInSlots;
	protected int heightInSlots;
	
	protected int maxSlots;
	
	protected Collection<Widget> children = new ArrayList<>();
	
	protected int row = 0;
	
	public SlotListWidget(Inventory inventory, int widthInSlots, int heightInSlots, int maxSlots) {
		super();
		
		this.inventory = inventory;
		
		this.widthInSlots = widthInSlots;
		this.heightInSlots = heightInSlots;
		
		this.maxSlots = maxSlots;
	}
	
	@Override
	public Size getStandardSize() {
		return Size.of(widthInSlots * 18.0F, heightInSlots * 18.0F);
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
	protected void onMouseScrolled(MouseScrolledEvent event) {
		if (rootCollection.isClient()) return;
		
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
		} else if (event.deltaY() <= 0.0D && row < (inventory.size() / widthInSlots) - heightInSlots) {
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
	
	@Override
	public boolean shouldSync(EventType type) {
		return type == EventType.MOUSE_SCROLLED || type == EventType.MOUSE_CLICKED || type == EventType.FOCUS_GAINED || type == EventType.FOCUS_RELEASED;
	}
	
	@Override
	public Collection<Widget> getChildren() {
		return children;
	}
	
	protected int getBottomRow() {
		return maxSlots - heightInSlots;
	}
	
	protected int getTotalRows() {
		return inventory.size() / widthInSlots;
	}
}
