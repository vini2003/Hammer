package dev.vini2003.hammer.gui.api.common.widget.provider;

import java.util.function.BooleanSupplier;

public interface DisabledProvider {
	boolean isDisabled();
	
	BooleanSupplier getDisabled();
	
	void setDisabled(BooleanSupplier disabled);
}
