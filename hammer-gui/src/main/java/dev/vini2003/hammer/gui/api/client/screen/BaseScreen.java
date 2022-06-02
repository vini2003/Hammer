package dev.vini2003.hammer.gui.api.client.screen;

import dev.vini2003.hammer.core.api.common.math.shape.Shape;
import dev.vini2003.hammer.core.api.common.tick.Tickable;
import dev.vini2003.hammer.gui.api.common.event.*;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;

public abstract class BaseScreen extends Screen implements WidgetCollection.Root, Tickable {
	protected Collection<Widget> children = new ArrayList<>();
	
	protected Shape shape = new Shape.ScreenRectangle(0.0F, 0.0F);
	
	protected BaseScreen(Text text) {
		super(text);
	}
	
	public abstract void init(int width, int height);
	
	@Override
	protected void init() {
		children.clear();
		
		super.init();
		
		init(width, height);
		
		onLayoutChanged();
		
		for (var child : getAllChildren()) {
			child.dispatchEvent(new LayoutChangedEvent());
		}
	}
	
	@Override
	public void onLayoutChanged() {
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
		
		shape = new Shape.ScreenRectangle(maximumX - minimumX, maximumY - minimumY).translate(minimumX, minimumY, 0.0F);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (var child : getChildren()) {
			child.dispatchEvent(new MouseClickedEvent((float) mouseX, (float) mouseY, button));
		}
		
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		for (var child : getChildren()) {
			child.dispatchEvent(new MouseReleasedEvent((float) mouseX, (float) mouseY, button));
		}
		
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		for (var child : getChildren()) {
			child.dispatchEvent(new MouseDraggedEvent((float) mouseX, (float) mouseY, button, deltaX, deltaY));
		}
		
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}
	
	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		for (var child : getChildren()) {
			child.dispatchEvent(new MouseMovedEvent((float) mouseX, (float) mouseY));
		}
		
		super.mouseMoved(mouseX, mouseY);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		for (var child : getChildren()) {
			child.dispatchEvent(new MouseScrolledEvent((float) mouseX, (float) mouseX, amount));
		}
		
		return super.mouseScrolled(mouseX, mouseY, amount);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		for (var child : getChildren()) {
			child.dispatchEvent(new KeyPressedEvent(keyCode, scanCode, modifiers));
		}
		
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		for (var child : getChildren()) {
			child.dispatchEvent(new KeyReleasedEvent(keyCode, scanCode, modifiers));
		}
		
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean charTyped(char chr, int modifiers) {
		for (var child : getChildren()) {
			child.dispatchEvent(new CharacterTypedEvent(chr, modifiers));
		}
		
		return super.charTyped(chr, modifiers);
	}
	
	@Override
	public void tick() {
		for (var child : getChildren()) {
			child.tick();
		}
	}
	
	@Override
	public Collection<Widget> getChildren() {
		return children;
	}
	
	@Override
	public boolean isClient() {
		return true;
	}
}
