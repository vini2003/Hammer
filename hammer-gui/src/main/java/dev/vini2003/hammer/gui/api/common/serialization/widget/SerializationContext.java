package dev.vini2003.hammer.gui.api.common.serialization.widget;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.texture.*;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.texture.type.TextureType;
import dev.vini2003.hammer.core.api.common.math.padding.Padding;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.gui.api.client.screen.base.BaseScreen;
import dev.vini2003.hammer.gui.api.common.registry.ValueRegistry;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.screen.ScreenWidget;
import dev.vini2003.hammer.gui.api.common.widget.type.WidgetType;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SerializationContext {
	private final Map<Identifier, Widget> widgets = new HashMap<>();
	
	public SerializationContext() {
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
