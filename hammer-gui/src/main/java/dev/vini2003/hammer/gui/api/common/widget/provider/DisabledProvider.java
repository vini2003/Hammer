package dev.vini2003.hammer.gui.api.common.widget.provider;

import java.util.function.BooleanSupplier;

public interface DisabledProvider {
	BooleanSupplier isDisabled();
	
	void setDisabled(BooleanSupplier disabled);
}
