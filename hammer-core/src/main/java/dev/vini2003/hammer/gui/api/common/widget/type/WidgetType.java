package dev.vini2003.hammer.gui.api.common.widget.type;

import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.gui.api.common.widget.side.WidgetSide;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public enum WidgetType {
	ARROW(HC.id("arrow"), WidgetSide.CLIENT),
	FLUID_BAR(HC.id("fluid_bar"), WidgetSide.CLIENT),
	IMAGE_BAR(HC.id("image_bar"), WidgetSide.CLIENT),
	BUTTON(HC.id("button"), WidgetSide.CLIENT),
	ITEM_STACK(HC.id("item_stack"), WidgetSide.CLIENT),
	ITEM(HC.id("item"), WidgetSide.CLIENT),
	LIST(HC.id("list"), WidgetSide.CLIENT),
	PANEL(HC.id("panel"), WidgetSide.CLIENT),
	TAB(HC.id("tab"), WidgetSide.CLIENT),
	TEXT_AREA(HC.id("text_area"), WidgetSide.CLIENT),
	TEXT_FIELD(HC.id("text_field"), WidgetSide.CLIENT),
	TEXT(HC.id("text"), WidgetSide.CLIENT);
	
	public static final Map<Identifier, WidgetType> TYPES = new HashMap<>();
	
	private final Identifier id;
	private final WidgetSide side;
	
	WidgetType(Identifier id, WidgetSide side) {
		this.id = id;
		this.side = side;
	}
	
	public Identifier getId() {
		return id;
	}
	
	public WidgetSide getSide() {
		return side;
	}
	
	public static WidgetType getById(Identifier id) {
		return TYPES.get(id);
	}
	
	static {
		for (var value : values()) {
			TYPES.put(value.getId(), value);
		}
	}
}
