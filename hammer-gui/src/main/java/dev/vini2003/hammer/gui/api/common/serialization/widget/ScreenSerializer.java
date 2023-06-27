package dev.vini2003.hammer.gui.api.common.serialization.widget;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.vini2003.hammer.core.HC;
import dev.vini2003.hammer.core.api.client.color.Color;
import dev.vini2003.hammer.core.api.client.texture.*;
import dev.vini2003.hammer.core.api.client.texture.base.Texture;
import dev.vini2003.hammer.core.api.client.texture.type.TextureType;
import dev.vini2003.hammer.core.api.common.math.padding.Padding;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import dev.vini2003.hammer.core.api.common.math.size.Size;
import dev.vini2003.hammer.core.api.common.serialization.ParseException;
import dev.vini2003.hammer.core.api.common.serialization.ParseResult;
import dev.vini2003.hammer.gui.api.client.screen.SerializableScreen;
import dev.vini2003.hammer.gui.api.common.registry.ValueRegistry;
import dev.vini2003.hammer.gui.api.common.widget.Widget;
import dev.vini2003.hammer.gui.api.common.widget.WidgetCollection;
import dev.vini2003.hammer.gui.api.common.widget.arrow.ArrowWidget;
import dev.vini2003.hammer.gui.api.common.widget.bar.FluidBarWidget;
import dev.vini2003.hammer.gui.api.common.widget.bar.ImageBarWidget;
import dev.vini2003.hammer.gui.api.common.widget.button.ButtonWidget;
import dev.vini2003.hammer.gui.api.common.widget.item.ItemStackWidget;
import dev.vini2003.hammer.gui.api.common.widget.item.ItemWidget;
import dev.vini2003.hammer.gui.api.common.widget.list.ListWidget;
import dev.vini2003.hammer.gui.api.common.widget.panel.PanelWidget;
import dev.vini2003.hammer.gui.api.common.widget.provider.*;
import dev.vini2003.hammer.gui.api.common.widget.side.WidgetSide;
import dev.vini2003.hammer.gui.api.common.widget.tab.TabWidget;
import dev.vini2003.hammer.gui.api.common.widget.text.TextAreaWidget;
import dev.vini2003.hammer.gui.api.common.widget.text.TextFieldWidget;
import dev.vini2003.hammer.gui.api.common.widget.text.TextWidget;
import dev.vini2003.hammer.gui.api.common.widget.type.WidgetType;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import org.apache.commons.io.IOUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Supplier;

public class ScreenSerializer {
	private static final String PX = "px";
	private static final String PCT = "%";
	
	private static final String TITLE = "title";
	
	private static final String WIDGETS = "widgets";
	private static final String WIDGET = "widget";
	
	private static final String TYPE = "type";
	private static final String PARENT = "parent";
	private static final String POSITION = "position";
	private static final String SIZE = "size";
	
	private static final String IMAGE_SIZE = "image_size";
	private static final String TILE_SIZE = "tile_size";
	
	private static final String MAX_TILES_X = "max_tiles_x";
	private static final String MAX_TILES_Y = "max_tiles_y";
	
	private static final String STEP_TILES_X = "step_tiles_x";
	private static final String STEP_TILES_Y = "step_tiles_y";
	
	private static final String WIDTH = "width";
	private static final String HEIGHT = "height";
	private static final String LENGTH = "length";
	
	private static final String X = "x";
	private static final String Y = "y";
	private static final String Z = "z";
	
	private static final String VARIANT = "variant";
	private static final String SPRITE = "sprite";
	
	private static final String ID = "id";
	
	private static final String PADDING = "padding";
	
	private static final String IMAGE_PADDING = "image_padding";
	
	private static final String LEFT = "left";
	private static final String RIGHT = "right";
	private static final String TOP = "top";
	private static final String BOTTOM = "bottom";
	
	private static final String ACTIVE_LEFT_TEXTURE = "active_left_texture";
	private static final String ACTIVE_MIDDLE_TEXTURE = "active_middle_texture";
	private static final String ACTIVE_RIGHT_TEXTURE = "active_right_texture";
	
	private static final String BACKGROUND_TEXTURE = "background_texture";
	
	private static final String COLOR = "color";
	
	private static final String CURRENT = "current";
	
	private static final String DISABLED = "disabled";
	
	private static final String DISABLED_TEXTURE = "disabled_texture";
	
	private static final String ENABLED_TEXTURE = "enabled_texture";
	
	private static final String FOCUSED_SCROLLER_TEXTURE = "focused_scroller_texture";
	
	private static final String FOCUSED_TEXTURE = "focused_texture";
	
	private static final String FOREGROUND_TEXTURE = "foreground_texture";
	
	private static final String HORIZONTAL = "horizontal";
	
	private static final String INACTIVE_LEFT_TEXTURE = "inactive_left_texture";
	private static final String INACTIVE_MIDDLE_TEXTURE = "inactive_middle_texture";
	private static final String INACTIVE_RIGHT_TEXTURE = "inactive_right_texture";
	
	private static final String INVERT = "invert";
	
	private static final String ITEM = "item";
	
	private static final String ITEM_STACK = "item_stack";
	
	private static final String LABEL = "label";
	
	private static final String MAXIMUM = "maximum";
	
	private static final String SCISSOR = "scissor";
	
	private static final String SCROLLBAR_TEXTURE = "scrollbar_texture";
	private static final String SCROLLER_TEXTURE = "scroller_texture";
	
	private static final String SHADOW = "shadow";
	
	private static final String SMOOTH = "smooth";
	
	private static final String STEP_HEIGHT = "step_height";
	private static final String STEP_WIDTH = "step_width";
	
	private static final String TEXT = "text";
	
	private static final String TEXTURE = "texture";
	
	private static final String TILED = "tiled";
	
	private static final String VERTICAL = "vertical";
	
	private static final String ATTRIBUTES = "attributes";
	
	private final ScreenSerializerContext context = new ScreenSerializerContext();
	
	private final ResourceManager resourceManager;
	
	private ScreenSerializer(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	
	public static ParseResult<Size> getSize(JsonElement sizeElement, Supplier<Size> relativeSize) {
		try {
			if (!(sizeElement instanceof JsonObject)) return ParseResult.bad();
			var sizeObject = sizeElement.getAsJsonObject();
			
			if (!sizeObject.has(LENGTH)) {
				var sizeWidth = getRelativeValue(sizeObject.get(WIDTH), relativeSize.get()::getWidth).getOrThrow();
				var sizeHeight = getRelativeValue(sizeObject.get(HEIGHT), relativeSize.get()::getHeight).getOrThrow();
				
				return ParseResult.ok(new Size(sizeWidth, sizeHeight));
			} else {
				var sizeWidth = getRelativeValue(sizeObject.get(WIDTH), relativeSize.get()::getWidth).getOrThrow();
				var sizeHeight = getRelativeValue(sizeObject.get(HEIGHT), relativeSize.get()::getHeight).getOrThrow();
				var sizeLength = getRelativeValue(sizeObject.get(LEFT), relativeSize.get()::getLength).getOrThrow();
				
				return ParseResult.ok(new Size(sizeWidth, sizeHeight, sizeLength));
			}
		} catch (ParseException e) {
			return ParseResult.bad();
		}
	}
	
	public static ParseResult<Position> getPosition(JsonElement positionElement, Supplier<Position> relativePosition) {
		try {
			if (!(positionElement instanceof JsonObject)) return ParseResult.bad();
			var positionObject = positionElement.getAsJsonObject();
			
			if (!positionObject.has(Z)) {
				var positionX = getRelativeValue(positionObject.get(X), relativePosition.get()::getX).getOrThrow();
				var positionY = getRelativeValue(positionObject.get(Y), relativePosition.get()::getY).getOrThrow();
				
				return ParseResult.ok(new Position(positionX, positionY));
			} else {
				var positionX = getRelativeValue(positionObject.get(X), relativePosition.get()::getX).getOrThrow();
				var positionY = getRelativeValue(positionObject.get(Y), relativePosition.get()::getY).getOrThrow();
				var positionZ = getRelativeValue(positionObject.get(Z), relativePosition.get()::getZ).getOrThrow();
				
				return ParseResult.ok(new Position(positionX, positionY, positionZ));
			}
		} catch (ParseException e) {
			return ParseResult.bad();
		}
	}
	
	public static ParseResult<Padding> getPadding(JsonElement paddingElement) {
		try {
			if (!(paddingElement instanceof JsonObject)) return ParseResult.bad();
			var paddingObject = paddingElement.getAsJsonObject();
			
			var paddingLeft = getFloat(paddingObject.get(LEFT)).getOrThrow();
			var paddingRight = getFloat(paddingObject.get(RIGHT)).getOrThrow();
			var paddingTop = getFloat(paddingObject.get(TOP)).getOrThrow();
			var paddingBottom = getFloat(paddingObject.get(BOTTOM)).getOrThrow();
			
			return ParseResult.ok(new Padding(paddingLeft, paddingRight, paddingTop, paddingBottom));
		} catch (ParseException e) {
			return ParseResult.bad();
		}
	}
	
	public static ParseResult<Integer> getInteger(JsonElement valueElement) {
		if (valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString() && valuePrimitive.getAsString().startsWith("&")) return getRegistryValue(valueElement);
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.bad();
		return ParseResult.ok(valuePrimitive.getAsNumber().intValue());
	}
	
	public static ParseResult<Byte> getByte(JsonElement valueElement) {
		if (valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString() && valuePrimitive.getAsString().startsWith("&")) return getRegistryValue(valueElement);
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.bad();
		return ParseResult.ok(valuePrimitive.getAsNumber().byteValue());
	}
	
	public static ParseResult<Short> getShort(JsonElement valueElement) {
		if (valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString() && valuePrimitive.getAsString().startsWith("&")) return getRegistryValue(valueElement);
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.bad();
		return ParseResult.ok(valuePrimitive.getAsNumber().shortValue());
	}
	
	public static ParseResult<Long> getLong(JsonElement valueElement) {
		if (valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString() && valuePrimitive.getAsString().startsWith("&")) return getRegistryValue(valueElement);
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.bad();
		return ParseResult.ok(valuePrimitive.getAsNumber().longValue());
	}
	
	public static ParseResult<Float> getFloat(JsonElement valueElement) {
		if (valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString() && valuePrimitive.getAsString().startsWith("&")) return getRegistryValue(valueElement);
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.bad();
		return ParseResult.ok(valuePrimitive.getAsNumber().floatValue());
	}
	
	public static ParseResult<Double> getDouble(JsonElement valueElement) {
		if (valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString() && valuePrimitive.getAsString().startsWith("&")) return getRegistryValue(valueElement);
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.bad();
		return ParseResult.ok(valuePrimitive.getAsNumber().doubleValue());
	}
	
	public static ParseResult<Boolean> getBoolean(JsonElement valueElement) {
		if (valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString() && valuePrimitive.getAsString().startsWith("&")) return getRegistryValue(valueElement);
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isBoolean())) return ParseResult.bad();
		return ParseResult.ok(valuePrimitive.getAsBoolean());
	}
	
	public static ParseResult<String> getString(JsonElement valueElement) {
		if (valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString() && valuePrimitive.getAsString().startsWith("&")) return getRegistryValue(valueElement);
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString())) return ParseResult.bad();
		return ParseResult.ok(valuePrimitive.getAsString());
	}
	
	public static ParseResult<Character> getCharacter(JsonElement valueElement) {
		if (valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString() && valuePrimitive.getAsString().startsWith("&")) return getRegistryValue(valueElement);
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString())) return ParseResult.bad();
		return ParseResult.ok(valuePrimitive.getAsCharacter());
	}
	
	public static ParseResult<BigDecimal> getBigDecimal(JsonElement valueElement) {
		if (valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString() && valuePrimitive.getAsString().startsWith("&")) return getRegistryValue(valueElement);
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.bad();
		return ParseResult.ok(valuePrimitive.getAsBigDecimal());
	}
	
	public static ParseResult<BigInteger> getBigInteger(JsonElement valueElement) {
		if (valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString() && valuePrimitive.getAsString().startsWith("&")) return getRegistryValue(valueElement);
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.bad();
		return ParseResult.ok(valuePrimitive.getAsBigInteger());
	}
	
	public static ParseResult<Number> getNumber(JsonElement valueElement) {
		if (valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString() && valuePrimitive.getAsString().startsWith("&")) return getRegistryValue(valueElement);
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.bad();
		return ParseResult.ok(valuePrimitive.getAsNumber());
	}
	
	public static ParseResult<Identifier> getIdentifier(JsonElement valueElement) {
		if (valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString() && valuePrimitive.getAsString().startsWith("&")) return getRegistryValue(valueElement);
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString() && Identifier.isValid(valueElement.getAsString()))) return ParseResult.bad();
		return ParseResult.ok(new Identifier(valuePrimitive.getAsString()));
	}
	
	public static ParseResult<Color> getColor(JsonElement valueElement) {
		try {
			return ParseResult.ok(new Color(getLong(valueElement).getOrThrow()));
		} catch (ParseException e) {
			return ParseResult.bad();
		}
	}
	
	public static ParseResult<Item> getItem(JsonElement valueElement) {
		// TODO!
		return ParseResult.bad();
	}
	
	public static ParseResult<ItemStack> getItemStack(JsonElement valueElement) {
		// TODO!
		return ParseResult.bad();
	}
	
	public static ParseResult<Texture> getTexture(JsonElement valueElement) {
		if (valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString() && valuePrimitive.getAsString().startsWith("&")) return getRegistryValue(valueElement);
		
		try {
			if (!(valueElement instanceof JsonObject)) return ParseResult.bad();
			
			var valueObject = valueElement.getAsJsonObject();
			
			var typeIdParseResult = getIdentifier(valueObject.get(TYPE));
			if (!typeIdParseResult.ok()) return ParseResult.bad();
			var typeId = typeIdParseResult.value();
			
			var type = TextureType.getById(typeId);
			if (type == null) return ParseResult.bad();
			
			switch (type) {
				case FLUID, TILED_FLUID -> {
					var variantId = getIdentifier(valueObject.get(VARIANT)).getOrThrow();
					
					var variant = FluidVariant.of(Registry.FLUID.get(variantId));
					
					if (type == TextureType.FLUID) {
						return ParseResult.ok(new FluidTexture(variant));
					} else {
						return ParseResult.ok(new TiledFluidTexture(variant));
					}
				}
				
				case IMAGE, TILED_IMAGE -> {
					var id = getIdentifier(valueObject.get(ID)).getOrThrow();
					
					if (type == TextureType.IMAGE) {
						return ParseResult.ok(new ImageTexture(id));
					} else {
						var tileSize = getSize(valueObject.get(TILE_SIZE), () -> Size.ZERO).getOrThrow();
						
						var maxTilesX = getFloat(valueObject.get(MAX_TILES_X)).getOrThrow();
						var maxTilesY = getFloat(valueObject.get(MAX_TILES_Y)).getOrThrow();
						
						var stepTilesX = getFloat(valueObject.get(STEP_TILES_X)).getOrThrow();
						var stepTilesY = getFloat(valueObject.get(STEP_TILES_Y)).getOrThrow();
						
						return ParseResult.ok(new TiledImageTexture(id, tileSize.getWidth(), tileSize.getHeight(), maxTilesX, maxTilesY, stepTilesX, stepTilesY));
					}
				}
				
				case SPRITE, TILED_SPRITE -> {
					throw new UnsupportedOperationException("Sprite textures are not supported!");
				}
				
				case PARTITIONED -> {
					var id = getIdentifier(valueObject.get(ID)).getOrThrow();
					
					var imageSize = getSize(valueObject.get(IMAGE_SIZE), () -> Size.ZERO).getOrThrow();
					
					var imagePadding = getPadding(valueObject.get(IMAGE_PADDING)).getOrThrow();
					
					return ParseResult.ok(new PartitionedTexture(id, imageSize, imagePadding));
				}
			}
			
			return ParseResult.bad();
		} catch (ParseException e) {
			return ParseResult.bad();
		}
	}
	
	public static <T> ParseResult<T> getValue(JsonElement valueElement, Class<T> clazz) {
		try {
			return ParseResult.ok(HC.GSON.fromJson(valueElement, clazz));
		} catch (Exception e) {
			return ParseResult.bad();
		}
	}
	
	public static <T> ParseResult<T> getRegistryValue(JsonElement valueElement) {
		try {
			if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString())) return ParseResult.bad();
			
			var valueId = getIdentifier(new JsonPrimitive(valuePrimitive.getAsString().substring(1))).getOrThrow();

			return ParseResult.ok(ValueRegistry.get(valueId));
		} catch (ParseException e) {
			return ParseResult.bad();
		}
	}
	
	public static ParseResult<Float> getRelativeValue(JsonElement valueElement, Supplier<Float> relativeValue) {
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString())) return ParseResult.bad();
		var valueString = valueElement.getAsString();
		if (!(valueString.endsWith(PCT) || valueString.endsWith(PX))) return ParseResult.bad();
		if (valueString.indexOf(PCT) != valueString.lastIndexOf(PCT) || valueString.indexOf(PX) != valueString.lastIndexOf(PX)) return ParseResult.bad();
		
		try {
			if (valueString.endsWith(PX)) {
				return ParseResult.ok(relativeValue.get() + Float.parseFloat(valueString.replace(PX, "")));
			}
			
			if (valueString.endsWith(PCT)) {
				var percentage = Float.parseFloat(valueString.replace(PCT, "")) / 100.0F;
				
				return ParseResult.ok(relativeValue.get() * percentage);
			}
		} catch (Exception ignored) {}
		
		return ParseResult.bad();
	}
	
	public static <T extends Widget> ParseResult<T> applyAttributes(Widget widget, JsonObject attributeObject) {
		try {
			if (widget instanceof ActiveLeftTextureProvider activeLeftTextureProvider) {
				activeLeftTextureProvider.setActiveLeftTexture(getTexture(attributeObject.get(ACTIVE_LEFT_TEXTURE)).getOrThrow());
			}
			
			if (widget instanceof ActiveMiddleTextureProvider activeMiddleTextureProvider) {
				activeMiddleTextureProvider.setActiveMiddleTexture(getTexture(attributeObject.get(ACTIVE_MIDDLE_TEXTURE)).getOrThrow());
			}
			
			if (widget instanceof ActiveRightTextureProvider activeRightTextureProvider) {
				activeRightTextureProvider.setActiveRightTexture(getTexture(attributeObject.get(ACTIVE_RIGHT_TEXTURE)).getOrThrow());
			}
			
			if (widget instanceof BackgroundSpriteProvider backgroundSpriteProvider) {
				// Sprites are not supported.
			}
			
			if (widget instanceof BackgroundTextureProvider backgroundTextureProvider) {
				backgroundTextureProvider.setBackgroundTexture(getTexture(attributeObject.get(BACKGROUND_TEXTURE)).getOrThrow());
			}
			
			if (widget instanceof ColorProvider colorProvider) {
				colorProvider.setColor(getColor(attributeObject.get(COLOR)).getOrThrow());
			}
			
			if (widget instanceof CurrentProvider currentProvider) {
				currentProvider.setCurrent(() -> getDouble(attributeObject.get(CURRENT)).getOrDefault(0.5D));
			}
			
			if (widget instanceof DisabledProvider disabledProvider) {
				disabledProvider.setDisabled(() -> getBoolean(attributeObject.get(DISABLED)).getOrDefault(false));
			}
			
			if (widget instanceof DisabledTextureProvider disabledTextureProvider) {
				disabledTextureProvider.setDisabledTexture(getTexture(attributeObject.get(DISABLED_TEXTURE)).getOrThrow());
			}
			
			if (widget instanceof EnabledTextureProvider enabledTextureProvider) {
				enabledTextureProvider.setEnabledTexture(getTexture(attributeObject.get(ENABLED_TEXTURE)).getOrThrow());
			}
			
			if (widget instanceof FocusedScrollerTextureProvider focusedScrollerTextureProvider) {
				focusedScrollerTextureProvider.setFocusedScrollerTexture(getTexture(attributeObject.get(FOCUSED_SCROLLER_TEXTURE)).getOrThrow());
			}
			
			if (widget instanceof FocusedTextureProvider focusedTextureProvider) {
				focusedTextureProvider.setFocusedTexture(getTexture(attributeObject.get(FOCUSED_TEXTURE)).getOrThrow());
			}
			
			if (widget instanceof ForegroundSpriteProvider foregroundSpriteProvider) {
				// Sprites are not supported.
			}
			
			if (widget instanceof ForegroundTextureProvider foregroundTextureProvider) {
				foregroundTextureProvider.setForegroundTexture(getTexture(attributeObject.get(FOREGROUND_TEXTURE)).getOrThrow());
			}
			
			if (widget instanceof HorizontalProvider horizontalProvider) {
				horizontalProvider.setHorizontal(getBoolean(attributeObject.get(HORIZONTAL)).getOrThrow());
			}
			
			if (widget instanceof InactiveLeftTextureProvider inactiveLeftTextureProvider) {
				inactiveLeftTextureProvider.setInactiveLeftTexture(getTexture(attributeObject.get(INACTIVE_LEFT_TEXTURE)).getOrThrow());
			}
			
			if (widget instanceof InactiveMiddleTextureProvider inactiveMiddleTextureProvider) {
				inactiveMiddleTextureProvider.setInactiveMiddleTexture(getTexture(attributeObject.get(INACTIVE_MIDDLE_TEXTURE)).getOrThrow());
			}
			
			if (widget instanceof InactiveRightTextureProvider inactiveRightTextureProvider) {
				inactiveRightTextureProvider.setInactiveRightTexture(getTexture(attributeObject.get(INACTIVE_RIGHT_TEXTURE)).getOrThrow());
			}
			
			if (widget instanceof InvertProvider invertProvider) {
				invertProvider.setInvert((getBoolean(attributeObject.get(INVERT)).getOrThrow()));
			}
			
			if (widget instanceof ItemProvider itemProvider) {
				itemProvider.setItem(getItem(attributeObject.get(ITEM)).getOrThrow());
			}
			
			if (widget instanceof ItemStackProvider itemStackProvider) {
				itemStackProvider.setItemStack(getItemStack(attributeObject.get(ITEM_STACK)).getOrThrow());
			}
			
			if (widget instanceof LabelProvider labelProvider) {
				labelProvider.setLabel(Text.literal(getString(attributeObject.get(LABEL)).getOrThrow()));
			}
			
			if (widget instanceof MaximumProvider maximumProvider) {
				maximumProvider.setMaximum(() -> getDouble(attributeObject.get(MAXIMUM)).getOrDefault(1.0D));
			}
			
			if (widget instanceof ScissorProvider scissorProvider) {
				scissorProvider.setScissor(getBoolean(attributeObject.get(SCISSOR)).getOrThrow());
			}
			
			if (widget instanceof ScrollbarTextureProvider scrollbarTextureProvider) {
				scrollbarTextureProvider.setScrollbarTexture(getTexture(attributeObject.get(SCROLLBAR_TEXTURE)).getOrThrow());
			}
			
			if (widget instanceof ScrollerTextureProvider scrollerTextureProvider) {
				scrollerTextureProvider.setScrollerTexture(getTexture(attributeObject.get(SCROLLER_TEXTURE)).getOrThrow());
			}
			
			if (widget instanceof ShadowProvider shadowProvider) {
				shadowProvider.setShadow(getBoolean(attributeObject.get(SHADOW)).getOrThrow());
			}
			
			if (widget instanceof SmoothProvider smoothProvider) {
				smoothProvider.setSmooth(getBoolean(attributeObject.get(SMOOTH)).getOrThrow());
			}
			
			if (widget instanceof StepHeightProvider stepHeightProvider) {
				stepHeightProvider.setStepHeight(getFloat(attributeObject.get(STEP_HEIGHT)).getOrDefault(1.0F));
			}
			
			if (widget instanceof StepWidthProvider stepWidthProvider) {
				stepWidthProvider.setStepWidth(getFloat(attributeObject.get(STEP_WIDTH)).getOrDefault(1.0F));
			}
			
			if (widget instanceof TextProvider textProvider) {
				textProvider.setText(Text.literal(getString(attributeObject.get(TEXT)).getOrThrow()));
			}
			
			if (widget instanceof TextureProvider textureProvider) {
				textureProvider.setTexture(getTexture(attributeObject.get(TEXTURE)).getOrThrow());
			}
			
			if (widget instanceof TiledProvider tiledProvider) {
				tiledProvider.setTiled(getBoolean(attributeObject.get(TILED)).getOrThrow());
			}
			
			if (widget instanceof VerticalProvider verticalProvider) {
				verticalProvider.setVertical(getBoolean(attributeObject.get(VERTICAL)).getOrThrow());
			}
			
			return (ParseResult<T>) ParseResult.ok(widget);
		} catch (ParseException e) {
			return ParseResult.bad();
		}
	}
	
	public static <T extends Widget> ParseResult<T> applyDefaults(Widget widget, Position position, Size size) {
		widget.setPosition(position);
		widget.setSize(size);
		
		return (ParseResult<T>) ParseResult.ok(widget);
	}
	
	public static <T extends Widget> ParseResult<T> create(Supplier<Widget> supplier, Position position, Size size, JsonObject attributeObject) {
		var widget = supplier.get();
		
		applyDefaults(widget, position, size);
		applyAttributes(widget, attributeObject);
		
		return (ParseResult<T>) ParseResult.ok(widget);
	}
	
	private SerializableScreen deserialize(Identifier screenId) {
		Resource resource = null;
		
		try {
			resource = resourceManager.getResource(screenId).orElseThrow();
			
			try (var resourceStream = resource.getInputStream()) {
				var resourceString = IOUtils.toString(resourceStream, "UTF-8");
				
				var resourceObject = HC.GSON.fromJson(resourceString, JsonObject.class);
				
				var title = getString(resourceObject.get(TITLE)).getOrThrow();
				
				var widgetsElement = resourceObject.get(WIDGETS);
				var widgetsArray = widgetsElement.getAsJsonArray();
				
				for (var widgetElement : widgetsArray) {
					var widgetObject = widgetElement.getAsJsonObject();

					var widgetId = getIdentifier(widgetObject.get(ID)).getOrThrow();
					
					var widgetTypeId = getIdentifier(widgetObject.get(TYPE)).getOrThrow();
					var widgetType = WidgetType.getById(widgetTypeId);
					
					if (widgetType.getSide() == WidgetSide.SERVER) return null;
					
					var widgetParentId = getIdentifier(widgetObject.get(PARENT)).getOrThrow();
					var widgetParent = context.get(widgetParentId);
					
					var widgetPosition = getPosition(widgetObject.get(POSITION), widgetParent::getPosition).getOrThrow();
					
					var widgetSize = getSize(widgetObject.get(SIZE), widgetParent::getSize).getOrThrow();
					
					var widgetAttributes = new JsonObject();
					
					if (widgetObject.has(ATTRIBUTES)) {
						var widgetAttributesElement = widgetObject.get(ATTRIBUTES);
						
						if (widgetAttributesElement instanceof JsonObject) {
							widgetAttributes = widgetAttributesElement.getAsJsonObject();
						}
					}
					
					var widget = switch (widgetType) {
						case ARROW -> create(ArrowWidget::new, widgetPosition, widgetSize, widgetAttributes).getOrThrow();
						case FLUID_BAR -> create(FluidBarWidget::new, widgetPosition, widgetSize, widgetAttributes).getOrThrow();
						case IMAGE_BAR -> create(ImageBarWidget::new, widgetPosition, widgetSize, widgetAttributes).getOrThrow();
						case BUTTON -> create(ButtonWidget::new, widgetPosition, widgetSize, widgetAttributes).getOrThrow();
						case ITEM_STACK -> create(ItemStackWidget::new, widgetPosition, widgetSize, widgetAttributes).getOrThrow();
						case ITEM -> create(ItemWidget::new, widgetPosition, widgetSize, widgetAttributes).getOrThrow();
						case LIST -> create(ListWidget::new, widgetPosition, widgetSize, widgetAttributes).getOrThrow();
						case PANEL -> create(PanelWidget::new, widgetPosition, widgetSize, widgetAttributes).getOrThrow();
						case TAB -> create(TabWidget::new, widgetPosition, widgetSize, widgetAttributes).getOrThrow();
						case TEXT_AREA -> create(TextAreaWidget::new, widgetPosition, widgetSize, widgetAttributes).getOrThrow();
						case TEXT_FIELD -> create(TextFieldWidget::new, widgetPosition, widgetSize, widgetAttributes).getOrThrow();
						case TEXT -> create(TextWidget::new, widgetPosition, widgetSize, widgetAttributes).getOrThrow();
					};
					
					if (widgetParent instanceof WidgetCollection collection) {
						collection.add(widget);
					}
					
					context.set(widgetId, widget);
				}
				
				return new SerializableScreen(Text.literal(title), context);
			}
		} catch (Exception ignored) {}
		
		return null;
	}
}