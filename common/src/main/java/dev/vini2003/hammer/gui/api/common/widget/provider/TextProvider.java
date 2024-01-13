package dev.vini2003.hammer.gui.api.common.widget.provider;

import net.minecraft.text.Text;

import java.util.function.Supplier;

public interface TextProvider {
	Supplier<Text> getText();
	
	void setText(Supplier<Text> text);
	
	default void setText(Text text) {
		setText(() -> text);
	}
}
