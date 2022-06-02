package dev.vini2003.hammer.gui.api.client.screen;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.gui.api.common.event.*;
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

import java.util.List;

public abstract class BaseHandledScreen<T extends BaseScreenHandler> extends HandledScreen<T> {
	public BaseHandledScreen(T screenHandler, PlayerInventory playerInventory, Text text) {
		super(screenHandler, playerInventory, text);
	}
	
	@Override
	protected void init() {
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
	}
	
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {}
	
	@Override
	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
		for (var child : handler.getAllChildren()) {
			if (child.isPointWithin((float) mouseX, (float) mouseY)) {
				return true;
			}
		}
		
		return true;
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (var child : handler.getChildren()) {
			child.dispatchEvent(new MouseClickedEvent((float) mouseX, (float) mouseY, button));
		}
		
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		for (var child : handler.getChildren()) {
			child.dispatchEvent(new MouseReleasedEvent((float) mouseX, (float) mouseY, button));
		}
		
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		for (var child : handler.getChildren()) {
			child.dispatchEvent(new MouseDraggedEvent((float) mouseX, (float) mouseY, button, deltaX, deltaY));
		}
		
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}
	
	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		for (var child : handler.getChildren()) {
			child.dispatchEvent(new MouseMovedEvent((float) mouseX, (float) mouseY));
		}
		
		super.mouseMoved(mouseX, mouseY);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		for (var child : handler.getChildren()) {
			child.dispatchEvent(new MouseScrolledEvent((float) mouseX, (float) mouseX, amount));
		}
		
		return super.mouseScrolled(mouseX, mouseY, amount);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		for (var child : handler.getChildren()) {
			child.dispatchEvent(new KeyPressedEvent(keyCode, scanCode, modifiers));
		}
		
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		for (var child : handler.getChildren()) {
			child.dispatchEvent(new KeyReleasedEvent(keyCode, scanCode, modifiers));
		}
		
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean charTyped(char chr, int modifiers) {
		for (var child : handler.getChildren()) {
			child.dispatchEvent(new CharacterTypedEvent(chr, modifiers));
		}
		
		return super.charTyped(chr, modifiers);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		super.renderBackground(matrices);
		
		var client = InstanceUtil.getClient();
		
		var provider = client.getBufferBuilders().getEffectVertexConsumers();
		
		for (var child : handler.getChildren()) {
			if (!child.isHidden()) {
				child.draw(matrices, provider, delta);
			}
		}
		
		for (var child : handler.getAllChildren()) {
			if (!child.isHidden() && child.isFocused()) {
				renderTooltip(matrices, (List<Text>) child.getTooltips(), mouseX, mouseX);
			}
		}
		
		provider.draw();
		
		super.render(matrices, mouseX, mouseY, delta);
		
		super.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}
}
