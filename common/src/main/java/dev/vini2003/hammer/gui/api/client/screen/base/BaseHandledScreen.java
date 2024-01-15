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

package dev.vini2003.hammer.gui.api.client.screen.base;

import dev.architectury.networking.NetworkManager;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.client.util.PositionUtil;
import dev.vini2003.hammer.gui.api.common.event.*;
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.registry.common.HGUINetworking;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.joml.Vector2i;

public abstract class BaseHandledScreen<T extends BaseScreenHandler> extends HandledScreen<T> {
	public BaseHandledScreen(T screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}
	
	@Override
	protected void init() {
		try {
			handler.getChildren().clear();
			handler.getSlots().clear();
			
			backgroundWidth = width;
			backgroundHeight = height;
			
			super.init();
			
			handler.init(width, height);
			
			handler.onLayoutChanged();
			
			for (var child : handler.getAllChildren()) {
				child.dispatchEvent(new LayoutChangedEvent());
			}
			
			var buf = new PacketByteBuf(Unpooled.buffer());
			buf.writeInt(width);
			buf.writeInt(height);
			
			NetworkManager.sendToServer(HGUINetworking.SYNC_SCREEN_HANDLER, buf);
		} catch (Exception e) {
			HC.LOGGER.error("A screen failed to initialize!");
			e.printStackTrace();
			
			handler.getPlayer().sendMessage(Text.literal("A screen failed to initialize!").formatted(Formatting.RED, Formatting.BOLD));
			handler.getPlayer().sendMessage(Text.literal(Registry.SCREEN_HANDLER.getId(handler.getType()) + " ").formatted(Formatting.RED, Formatting.BOLD));
			
			handler.onClosed(handler.getPlayer());
		}
	}
	
	@Override
	protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
	
	}
	
	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
	
	}
	
	@Override
	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
		for (var child : handler.getAllChildren()) {
			if (child.isPointWithin((float) mouseX, (float) mouseY)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		try {
			for (var child : handler.getChildren()) {
				child.dispatchEvent(new MouseClickedEvent((float) mouseX, (float) mouseY, button));
			}
			
			return super.mouseClicked(mouseX, mouseY, button);
		} catch (Exception e) {
			HC.LOGGER.error("A screen failed to handle an event!");
			e.printStackTrace();
			
			handler.getPlayer().sendMessage(Text.literal("A screen failed to handle an event!").formatted(Formatting.RED, Formatting.BOLD));
			handler.getPlayer().sendMessage(Text.literal(Registry.SCREEN_HANDLER.getId(handler.getType()) + " ").formatted(Formatting.RED, Formatting.BOLD));
			
			handler.onClosed(handler.getPlayer());
			
			return false;
		}
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		try {
			for (var child : handler.getChildren()) {
				child.dispatchEvent(new MouseReleasedEvent((float) mouseX, (float) mouseY, button));
			}
			
			return super.mouseReleased(mouseX, mouseY, button);
		} catch (Exception e) {
			HC.LOGGER.error("A screen failed to handle an event!");
			e.printStackTrace();
			
			handler.getPlayer().sendMessage(Text.literal("A screen failed to handle an event!").formatted(Formatting.RED, Formatting.BOLD));
			handler.getPlayer().sendMessage(Text.literal(Registry.SCREEN_HANDLER.getId(handler.getType()) + " ").formatted(Formatting.RED, Formatting.BOLD));
			
			handler.onClosed(handler.getPlayer());
			
			return false;
		}
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		try {
			for (var child : handler.getChildren()) {
				child.dispatchEvent(new MouseDraggedEvent((float) mouseX, (float) mouseY, button, deltaX, deltaY));
			}
			
			return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
		} catch (Exception e) {
			HC.LOGGER.error("A screen failed to handle an event!");
			e.printStackTrace();
			
			handler.getPlayer().sendMessage(Text.literal("A screen failed to handle an event!").formatted(Formatting.RED, Formatting.BOLD));
			handler.getPlayer().sendMessage(Text.literal(Registry.SCREEN_HANDLER.getId(handler.getType()) + " ").formatted(Formatting.RED, Formatting.BOLD));
			
			handler.onClosed(handler.getPlayer());
			
			return false;
		}
	}
	
	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		try {
			for (var child : handler.getChildren()) {
				child.dispatchEvent(new MouseMovedEvent((float) mouseX, (float) mouseY));
			}
			
			super.mouseMoved(mouseX, mouseY);
		} catch (Exception e) {
			HC.LOGGER.error("A screen failed to handle an event!");
			e.printStackTrace();
			
			handler.getPlayer().sendMessage(Text.literal("A screen failed to handle an event!").formatted(Formatting.RED, Formatting.BOLD));
			handler.getPlayer().sendMessage(Text.literal(Registry.SCREEN_HANDLER.getId(handler.getType()) + " ").formatted(Formatting.RED, Formatting.BOLD));
			
			handler.onClosed(handler.getPlayer());
		}
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		try {
			for (var child : handler.getChildren()) {
				child.dispatchEvent(new MouseScrolledEvent((float) mouseX, (float) mouseX, amount));
			}
			
			return super.mouseScrolled(mouseX, mouseY, amount);
		} catch (Exception e) {
			HC.LOGGER.error("A screen failed to handle an event!");
			e.printStackTrace();
			
			handler.getPlayer().sendMessage(Text.literal("A screen failed to handle an event!").formatted(Formatting.RED, Formatting.BOLD));
			handler.getPlayer().sendMessage(Text.literal(Registry.SCREEN_HANDLER.getId(handler.getType()) + " ").formatted(Formatting.RED, Formatting.BOLD));
			
			handler.onClosed(handler.getPlayer());
			
			return false;
		}
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		try {
			var held = false;
			
			for (var child : handler.getChildren()) {
				child.dispatchEvent(new KeyPressedEvent(keyCode, scanCode, modifiers));
			}
			
			for (var child : handler.getAllChildren()) {
				if (child.isHeld()) {
					held = true;
				}
			}
			
			if (!held) {
				return super.keyPressed(keyCode, scanCode, modifiers);
			} else {
				return false;
			}
		} catch (Exception e) {
			HC.LOGGER.error("A screen failed to handle an event!");
			e.printStackTrace();
			
			handler.getPlayer().sendMessage(Text.literal("A screen failed to handle an event!").formatted(Formatting.RED, Formatting.BOLD));
			handler.getPlayer().sendMessage(Text.literal(Registry.SCREEN_HANDLER.getId(handler.getType()) + " ").formatted(Formatting.RED, Formatting.BOLD));
			
			handler.onClosed(handler.getPlayer());
			
			return false;
		}
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		try {
			for (var child : handler.getChildren()) {
				child.dispatchEvent(new KeyReleasedEvent(keyCode, scanCode, modifiers));
			}
			
			return super.keyReleased(keyCode, scanCode, modifiers);
		} catch (Exception e) {
			HC.LOGGER.error("A screen failed to handle an event!");
			e.printStackTrace();
			
			handler.getPlayer().sendMessage(Text.literal("A screen failed to handle an event!").formatted(Formatting.RED, Formatting.BOLD));
			handler.getPlayer().sendMessage(Text.literal(Registry.SCREEN_HANDLER.getId(handler.getType()) + " ").formatted(Formatting.RED, Formatting.BOLD));
			
			handler.onClosed(handler.getPlayer());
			
			return false;
		}
	}
	
	@Override
	public boolean charTyped(char chr, int modifiers) {
		try {
			for (var child : handler.getChildren()) {
				child.dispatchEvent(new CharacterTypedEvent(chr, modifiers));
			}
			
			return super.charTyped(chr, modifiers);
		} catch (Exception e) {
			HC.LOGGER.error("A screen failed to handle an event!");
			e.printStackTrace();
			
			handler.getPlayer().sendMessage(Text.literal("A screen failed to handle an event!").formatted(Formatting.RED, Formatting.BOLD));
			handler.getPlayer().sendMessage(Text.literal(Registry.SCREEN_HANDLER.getId(handler.getType()) + " ").formatted(Formatting.RED, Formatting.BOLD));
			
			handler.onClosed(handler.getPlayer());
			
			return false;
		}
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(context);
			
			var client = InstanceUtil.getClient();
			
			var provider = client.getBufferBuilders().getEffectVertexConsumers();
			
			for (var child : handler.getChildren()) {
				if (!child.isHidden()) {
					child.draw(context, delta);
				}
			}
			
			var mousePos = PositionUtil.getMousePosition();
			
			Widget minChild = null;
			float minDist = Float.MAX_VALUE;
			
			for (var child : handler.getAllChildren()) {
				if (!child.isHidden() && child.isFocused()) {
					if (minChild == null || minDist == Float.MAX_VALUE || minChild.getPosition().distanceTo(mousePos) > child.getPosition().distanceTo(mousePos)) {
						minChild = child;
						minDist = child.getPosition().distanceTo(mousePos);
					}
				}
			}
			
			if (minChild != null) {
				// TODO: Fix this, it's very bad.
				context.drawTooltip(textRenderer, minChild.getTooltip(), ($, $$, $$$, $$$$, $$$$$, $$$$$$) -> new Vector2i(mouseX + 12, mouseY - 12), mouseX, mouseY);
			}
			
			provider.draw();
			
			super.render(context, mouseX, mouseY, delta);
			
			super.drawMouseoverTooltip(context, mouseX, mouseY);
		} catch (Exception e) {
			HC.LOGGER.error("A screen failed to render!");
			e.printStackTrace();
			
			handler.getPlayer().sendMessage(Text.literal("A screen failed to render!").formatted(Formatting.RED, Formatting.BOLD));
			handler.getPlayer().sendMessage(Text.literal(Registry.SCREEN_HANDLER.getId(handler.getType()) + " ").formatted(Formatting.RED, Formatting.BOLD));
			
			handler.onClosed(handler.getPlayer());
		}
	}
}
