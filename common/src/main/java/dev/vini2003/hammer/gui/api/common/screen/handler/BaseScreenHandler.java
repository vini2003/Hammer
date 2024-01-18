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

package dev.vini2003.hammer.gui.api.common.screen.handler;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.math.shape.Shape;
import dev.vini2003.hammer.core.api.common.tick.Ticks;
import dev.vini2003.hammer.core.api.common.util.StackUtil;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collection;

public abstract class BaseScreenHandler extends ScreenHandler implements WidgetCollection.Root, Ticks {
	protected final PlayerEntity player;
	
	protected Collection<Widget> children = new ArrayList<>();
	
	protected Shape shape = new Shape.Rectangle2D(0.0F, 0.0F);
	
	public BaseScreenHandler(@Nullable ScreenHandlerType<?> screenHandlerType, int syncId, PlayerEntity player) {
		super(screenHandlerType, syncId);
		
		this.player = player;
	}
	
	public abstract void init(int width, int height);
	
	@Override
	public void onLayoutChanged() {
		try {
			var minimumX = Float.MAX_VALUE;
			var minimumY = Float.MIN_VALUE;
			
			var maximumX = 0.0F;
			var maximumY = 0.0F;
			
			for (var child : getChildren()) {
				if (child.getX() < minimumX) {
					minimumX = child.getX();
				}
				
				if (child.getY() < minimumY) {
					minimumY = child.getY();
				}
				
				if (child.getX() > maximumX) {
					maximumX = child.getX();
				}
				
				if (child.getY() > maximumY) {
					maximumY = child.getY();
				}
			}
			
			shape = new Shape.Rectangle2D(maximumX - minimumX, maximumY - minimumY).translate(minimumX, minimumY, 0.0F);
			
			if (isClient()) {
				onLayoutChangedClient();
			}
		} catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(Text.translatable("warning.hammer.screen_generic_error").formatted(Formatting.RED, Formatting.BOLD));
			close(player);
		}
	}
	
	public void onLayoutChangedClient() {
		try {
			var client = InstanceUtil.getClient();
			
			var screen = (HandledScreen<?>) client.currentScreen;
			
			screen.x = (int) shape.getStartPos().getX();
			screen.y = (int) shape.getStartPos().getY();
			
			screen.backgroundWidth = (int) shape.getWidth();
			screen.backgroundHeight = (int) shape.getHeight();
		} catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(Text.translatable("warning.hammer.screen_generic_error").formatted(Formatting.RED, Formatting.BOLD));
			close(player);
		}
	}
	
	@Override
	public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {
		try {
			if (actionType == SlotActionType.QUICK_MOVE) {
				if (slotIndex >= 0 && slotIndex < slots.size()) {
					var slot = slots.get(slotIndex);
					
					if (!slot.getStack().isEmpty() && slot.canTakeItems(player)) {
						for (var otherSlotIndex = 0; otherSlotIndex < slots.size(); ++otherSlotIndex) {
							var newSlot = slots.get(otherSlotIndex);
							
							if (!newSlot.getStack().isEmpty()) {
								if (newSlot.canInsert(slot.getStack())) {
									if (newSlot != slot && newSlot.inventory != slot.inventory) {
										StackUtil.merge(slot.getStack(), newSlot.getStack(), (stackA, stackB) -> {
											slot.setStack(stackA);
											newSlot.setStack(stackB);
										});
									}
									
									if (slot.getStack().isEmpty()) {
										break;
									}
								}
							}
						}
						
						for (var otherSlotNumber = 0; otherSlotNumber < slots.size(); ++otherSlotNumber) {
							var newSlot = slots.get(otherSlotNumber);
							
							if (newSlot.canInsert(slot.getStack())) {
								if (newSlot != slot && newSlot.inventory != slot.inventory) {
									StackUtil.merge(slot.getStack(), newSlot.getStack(), (stackA, stackB) -> {
										slot.setStack(stackA);
										newSlot.setStack(stackB);
									});
								}
								
								if (slot.getStack().isEmpty()) {
									break;
								}
							}
						}
					}
				}
			} else {
				super.onSlotClick(slotIndex, button, actionType, player);
			}
		} catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(Text.translatable("warning.hammer.screen_generic_error").formatted(Formatting.RED, Formatting.BOLD));
			close(player);
		}
	}
	
	@Override
	public void add(Widget child) {
		try {
			child.setCollection(this);
			child.setRootCollection(this);
			
			Root.super.add(child);
		}  catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(Text.translatable("warning.hammer.screen_generic_error").formatted(Formatting.RED, Formatting.BOLD));
			close(player);
		}
	}
	
	public Slot addSlot(Slot slot) {
		return super.addSlot(slot);
	}
	
	public void removeSlot(Slot slot) {
		var id = slot.id;
		
		slots.remove(id);
		
		for (var otherSlot : slots) {
			if (slot.id >= slot.id) {
				slot.id -= 1;
			}
		}
	}
	
	@Override
	public void onTick() {
		try {
			for (var child : getChildren()) {
				child.onTick();
			}
		}  catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(Text.translatable("warning.hammer.screen_generic_error").formatted(Formatting.RED, Formatting.BOLD));
			close(player);
		}
	}
	
	public Shape getShape() {
		return shape;
	}
	
	public PlayerEntity getPlayer() {
		return player;
	}
	
	public Collection<Slot> getSlots() {
		return slots;
	}
	
	@Override
	public Collection<Widget> getChildren() {
		return children;
	}
	
	@Override
	public int getSyncId() {
		return syncId;
	}
	
	@Override
	public boolean isClient() {
		return player.getWorld().isClient();
	}
	
	@Override
	public BaseScreenHandler getScreenHandler() {
		return this;
	}
	
	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		onSlotClick(index, GLFW.GLFW_MOUSE_BUTTON_1, SlotActionType.QUICK_MOVE, player);
		return ItemStack.EMPTY;
	}
}
