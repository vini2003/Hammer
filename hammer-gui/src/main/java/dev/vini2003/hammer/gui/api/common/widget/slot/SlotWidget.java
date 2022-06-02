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

package dev.vini2003.hammer.gui.api.common.widget.slot;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.PartitionedTexture;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.function.QuadFunction;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.event.AddedEvent;
import dev.vini2003.hammer.gui.api.common.event.LayoutChangedEvent;
import dev.vini2003.hammer.gui.api.common.event.RemovedEvent;
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

import java.util.function.Supplier;

public class SlotWidget extends Widget {
	public static final Texture STANDARD_TEXTURE = new PartitionedTexture(HC.id("textures/widget/slot.png"), 18.0F, 18.0F, 0.055F, 0.055F, 0.055F, 0.055F);
	
	protected Supplier<Texture> texture = () -> STANDARD_TEXTURE;
	
	protected Inventory inventory;
	
	protected QuadFunction<Inventory, Integer, Integer, Integer, Slot> slotSupplier;
	
	protected Slot slot = null;
	
	protected int index;
	
	public SlotWidget(Inventory inventory, int index, QuadFunction<Inventory, Integer, Integer, Integer, Slot> slotSupplier) {
		super();
		
		this.inventory = inventory;
		
		this.index = index;
		
		this.slotSupplier = slotSupplier;
	}
	
	@Override
	protected void onAdded(AddedEvent event) {
		super.onAdded(event);
		
		slot = slotSupplier.apply(inventory, index, getSlotX(), getSlotY());
		slot.index = index;
		
		if (rootCollection instanceof BaseScreenHandler handler) {
			handler.addSlot(slot);
		}
	}
	
	@Override
	protected void onRemoved(RemovedEvent event) {
		super.onRemoved(event);
		
		if (rootCollection instanceof BaseScreenHandler handler) {
			handler.removeSlot(slot);
		}
	}
	
	@Override
	protected void onLayoutChanged(LayoutChangedEvent event) {
		super.onLayoutChanged(event);
		
		updateSlot();
	}
	
	@Override
	public void setPosition(Position position) {
		super.setPosition(new Position((int) position.getX(), (int) position.getY(), (int) position.getZ()));
		
		updateSlot();
	}
	
	@Override
	public void setSize(Size size) {
		super.setSize(new Size((int) size.getWidth(), (int) size.getHeight(), (int) size.getLength()));
		
		updateSlot();
	}
	
	@Override
	public void setHidden(boolean hidden) {
		super.setHidden(hidden);
		
		updateSlot();
	}
	
	public int getSlotX() {
		return (int) (getX() + getWidth() <= 18.0F ? 1.0F : getWidth() / 2.0F - 9.0F);
	}
	
	public int getSlotY() {
		return (int) (getY() + getHeight() <= 18.0F ? 1.0F : getHeight() / 2.0F - 9.0F);
	}
	
	protected void updateSlot() {
		if (hidden) {
			slot.x = Integer.MAX_VALUE / 2;
			slot.y = Integer.MAX_VALUE / 2;
		} else {
			slot.x = getSlotX();
			slot.y = getSlotY();
		}
		
		if (rootCollection.isClient()) {
			updateSlotOnClient();
		}
	}
	
	protected void updateSlotOnClient() {
		var client = InstanceUtil.getClient();
		
		var screen = (HandledScreen<?>) client.currentScreen;
		
		if (screen != null) {
			slot.x = slot.x - screen.x;
			slot.y = slot.y - screen.y;
		}
	}
	
	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float tickDelta) {
		texture.get().draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
	}
	
	public Slot getSlot() {
		return slot;
	}
	
	public void setTexture(Supplier<Texture> texture) {
		this.texture = texture;
	}
	
	public void setTexture(Texture texture) {
		setTexture(() -> texture);
	}
}
