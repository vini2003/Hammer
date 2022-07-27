package dev.vini2003.hammer.gui.api.common.widget.provider;

import java.util.function.DoubleSupplier;

public interface MaximumProvider {
	DoubleSupplier getMaximum();
	
	void setMaximum(DoubleSupplier maximum);
	
	default void setMaximum(double maximum) {
		setMaximum(() -> maximum);
	}
}
