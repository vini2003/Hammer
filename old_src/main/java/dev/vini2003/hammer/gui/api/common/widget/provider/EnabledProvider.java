package dev.vini2003.hammer.gui.api.common.widget.provider;

import java.util.function.BooleanSupplier;

public interface EnabledProvider {
	BooleanSupplier isEnabled();
	
	void setEnabled(BooleanSupplier enabled);
}
