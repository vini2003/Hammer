package dev.vini2003.hammer.gui.api.common.widget.provider;

import net.minecraft.text.Text;

import java.util.function.Supplier;

public interface LabelProvider {
	Supplier<Text> getLabel();
	
	void setLabel(Supplier<Text> label);
	
	default void setLabel(Text label) {
		setLabel(() -> label);
	}
}
