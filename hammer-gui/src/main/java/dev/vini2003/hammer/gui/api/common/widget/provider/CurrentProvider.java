package dev.vini2003.hammer.gui.api.common.widget.provider;

import java.util.function.DoubleSupplier;

public interface CurrentProvider {
	DoubleSupplier getCurrent();
	
	void setCurrent(DoubleSupplier current);
	
	default void setCurrent(double current) {
		setCurrent(() -> current);
	}
}
