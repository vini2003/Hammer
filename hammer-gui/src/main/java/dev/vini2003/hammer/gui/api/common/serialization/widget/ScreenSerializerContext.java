package dev.vini2003.hammer.gui.api.common.serialization.widget;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.screen.ScreenWidget;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ScreenSerializerContext {
	private final Map<Identifier, Widget> widgets = new HashMap<>();
	
	public ScreenSerializerContext() {
		set(HC.id("screen"), new ScreenWidget());
	}
	
	public <W extends Widget> W get(Identifier id) {
		return (W) widgets.get(id);
	}
	
	public Collection<Widget> getAll() {
		return widgets.values();
	}
	
	public <W extends Widget> void set(Identifier id, W widget) {
		widgets.put(id, widget);
	}
	
	public boolean contains(Identifier id) {
		return widgets.containsKey(id);
	}
}
