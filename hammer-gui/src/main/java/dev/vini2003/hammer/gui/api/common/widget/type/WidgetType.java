package dev.vini2003.hammer.gui.api.common.widget.type;

import com.google.gson.JsonObject;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.common.function.TriFunction;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.arrow.ArrowWidget;
import dev.vini2003.hammer.gui.api.common.widget.side.WidgetSide;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public enum WidgetType {
	ARROW(HC.id("arrow"), WidgetSide.CLIENT, ArrowWidget::deserialize);
	
	public static final Map<Identifier, WidgetType> TYPES = new HashMap<>();
	
	private final Identifier id;
	private final WidgetSide side;
	
	public final TriFunction<Position, Size, JsonObject, Widget> deserializer;
	
	WidgetType(Identifier id, WidgetSide side, TriFunction<Position, Size, JsonObject, Widget> deserializer) {
		this.id = id;
		this.side = side;
		this.deserializer = deserializer;
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
