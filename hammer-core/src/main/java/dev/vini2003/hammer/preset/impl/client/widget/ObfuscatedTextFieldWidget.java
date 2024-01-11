package dev.vini2003.hammer.preset.impl.client.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class ObfuscatedTextFieldWidget extends TextFieldWidget {
	public ObfuscatedTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text) {
		super(textRenderer, x, y, width, height, text);
	}
	
	public ObfuscatedTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, @Nullable TextFieldWidget textFieldWidget, Text text) {
		super(textRenderer, x, y, width, height, textFieldWidget, text);
	}
	
	@Override
	public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
		var text = getText();
		setText(text.replaceAll(".", "*"));
		super.renderButton(context, mouseX, mouseY, delta);
		setText(text);
	}
}
