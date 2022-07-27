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
import dev.vini2003.hammer.gui.api.common.widget.arrow.ArrowWidget;
import dev.vini2003.hammer.gui.api.common.widget.side.WidgetSide;
import dev.vini2003.hammer.gui.api.common.widget.type.WidgetType;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.MixinEnvironment;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Supplier;

class ScreenSerializer {
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
	private static final String COLOR = "color";
	
	private static final String ID = "id";
	
	private static final String PADDING = "padding";
	
	private static final String IMAGE_PADDING = "image_padding";
	
	private static final String LEFT = "left";
	private static final String RIGHT = "right";
	private static final String TOP = "top";
	private static final String BOTTOM = "bottom";
	
	private static final String HORIZONTAL = "horizontal";
	private static final String VERTICAL = "vertical";
	
	private static final String SMOOTH = "smooth";
	private static final String SCISSOR = "scissor";
	
	private static final String FOREGROUND_TEXTURE = "foreground_texture";
	private static final String BACKGROUND_TEXTURE = "background_texture";
	
	private final SerializationContext context = new SerializationContext();
	
	private final ResourceManager resourceManager;
	
	private ScreenSerializer(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	
	class ParseException extends Exception {}
	
	record ParseResult<T>(
			@Nullable
			T value,
			boolean valid
	) {
		public static <T> ParseResult<T> invalid() {
			return new ParseResult<T>(null, false);
		}
		
		public static <T> ParseResult<T> valid(T value) {
			return new ParseResult<>(value, true);
		}
		
		public T getOrThrow() throws ParseException {
			if (value == null) {
				throw new Exception();
			} else {
				return value;
			}
		}
	}
	
	public static ParseResult<Size> getSize(JsonElement sizeElement, Supplier<Size> relativeSize) {
		try {
			if (!(sizeElement instanceof JsonObject)) return ParseResult.invalid();
			var sizeObject = sizeElement.getAsJsonObject();
			
			if (!sizeObject.has(LENGTH)) {
				var sizeWidth = getRelativeValue(sizeObject.get(WIDTH), relativeSize.get()::getWidth).getOrThrow();
				var sizeHeight = getRelativeValue(sizeObject.get(HEIGHT), relativeSize.get()::getHeight).getOrThrow();
				
				return ParseResult.valid(new Size(sizeWidth, sizeHeight));
			} else {
				var sizeWidth = getRelativeValue(sizeObject.get(WIDTH), relativeSize.get()::getWidth).getOrThrow();
				var sizeHeight = getRelativeValue(sizeObject.get(HEIGHT), relativeSize.get()::getHeight).getOrThrow();
				var sizeLength = getRelativeValue(sizeObject.get(LEFT), relativeSize.get()::getLength).getOrThrow();
				
				return ParseResult.valid(new Size(sizeWidth, sizeHeight, sizeLength));
			}
		} catch (ParseException e) {
			return ParseResult.invalid();
		}
	}
	
	public static ParseResult<Position> getPosition(JsonElement positionElement, Supplier<Position> relativePosition) {
		try {
			if (!(positionElement instanceof JsonObject)) return ParseResult.invalid();
			var positionObject = positionElement.getAsJsonObject();
			
			if (!positionObject.has(Z)) {
				var positionX = getRelativeValue(positionObject.get(X), relativePosition.get()::getX).getOrThrow();
				var positionY = getRelativeValue(positionObject.get(Y), relativePosition.get()::getY).getOrThrow();
				
				return ParseResult.valid(new Position(positionX, positionY));
			} else {
				var positionX = getRelativeValue(positionObject.get(X), relativePosition.get()::getX).getOrThrow();
				var positionY = getRelativeValue(positionObject.get(Y), relativePosition.get()::getY).getOrThrow();
				var positionZ = getRelativeValue(positionObject.get(Z), relativePosition.get()::getZ).getOrThrow();
				
				return ParseResult.valid(new Position(positionX, positionY, positionZ));
			}
		} catch (ParseException e) {
			return ParseResult.invalid();
		}
	}
	
	public static ParseResult<Padding> getPadding(JsonElement paddingElement) {
		try {
			if (!(paddingElement instanceof JsonObject)) return ParseResult.invalid();
			var paddingObject = paddingElement.getAsJsonObject();
			
			var paddingLeft = getFloat(paddingObject.get(LEFT)).getOrThrow();
			var paddingRight = getFloat(paddingObject.get(RIGHT)).getOrThrow();
			var paddingTop = getFloat(paddingObject.get(TOP)).getOrThrow();
			var paddingBottom = getFloat(paddingObject.get(BOTTOM)).getOrThrow();
			
			return ParseResult.valid(new Padding(paddingLeft, paddingRight, paddingTop, paddingBottom));
		} catch (ParseException e) {
			return ParseResult.invalid();
		}
	}
	
	public static ParseResult<Integer> getInteger(JsonElement valueElement) {
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.invalid();
		return ParseResult.valid(valuePrimitive.getAsNumber().intValue());
	}
	
	public static ParseResult<Byte> getByte(JsonElement valueElement) {
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.invalid();
		return ParseResult.valid(valuePrimitive.getAsNumber().byteValue());
	}
	
	public static ParseResult<Short> getShort(JsonElement valueElement) {
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.invalid();
		return ParseResult.valid(valuePrimitive.getAsNumber().shortValue());
	}
	
	public static ParseResult<Long> getLong(JsonElement valueElement) {
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.invalid();
		return ParseResult.valid(valuePrimitive.getAsNumber().longValue());
	}
	
	public static ParseResult<Float> getFloat(JsonElement valueElement) {
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.invalid();
		return ParseResult.valid(valuePrimitive.getAsNumber().floatValue());
	}
	
	public static ParseResult<Double> getDouble(JsonElement valueElement) {
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.invalid();
		return ParseResult.valid(valuePrimitive.getAsNumber().doubleValue());
	}
	
	public static ParseResult<Boolean> getBoolean(JsonElement valueElement) {
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isBoolean())) return ParseResult.invalid();
		return ParseResult.valid(valuePrimitive.getAsBoolean());
	}
	
	public static ParseResult<String> getString(JsonElement valueElement) {
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString())) return ParseResult.invalid();
		return ParseResult.valid(valuePrimitive.getAsString());
	}
	
	public static ParseResult<Character> getCharacter(JsonElement valueElement) {
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString())) return ParseResult.invalid();
		return ParseResult.valid(valuePrimitive.getAsCharacter());
	}
	
	public static ParseResult<BigDecimal> getBigDecimal(JsonElement valueElement) {
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.invalid();
		return ParseResult.valid(valuePrimitive.getAsBigDecimal());
	}
	
	public static ParseResult<BigInteger> getBigInteger(JsonElement valueElement) {
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.invalid();
		return ParseResult.valid(valuePrimitive.getAsBigInteger());
	}
	
	public static ParseResult<Number> getNumber(JsonElement valueElement) {
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isNumber())) return ParseResult.invalid();
		return ParseResult.valid(valuePrimitive.getAsNumber());
	}
	
	public static ParseResult<Identifier> getIdentifier(JsonElement valueElement) {
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString() && Identifier.isValid(valueElement.getAsString()))) return ParseResult.invalid();
		return ParseResult.valid(new Identifier(valuePrimitive.getAsString()));
	}
	
	public static ParseResult<Texture> getTexture(JsonElement valueElement) {
		try {
			if (valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString() && Identifier.isValid(valuePrimitive.getAsString())) return ValueRegistry.get(new Identifier(valuePrimitive.getAsString()));
			if (!(valueElement instanceof JsonObject)) return ParseResult.invalid();
			
			var valueObject = valueElement.getAsJsonObject();
			
			var typeIdParseResult = getIdentifier(valueObject.get(TYPE));
			if (!typeIdParseResult.valid()) return ParseResult.invalid();
			var typeId = typeIdParseResult.value();
			
			var type = TextureType.getById(typeId);
			if (type == null) return ParseResult.invalid();
			
			switch (type) {
				case FLUID, TILED_FLUID -> {
					var variantId = getIdentifier(valueObject.get(VARIANT)).getOrThrow();
					
					var variant = FluidVariant.of(Registry.FLUID.get(variantId));
					
					if (type == TextureType.FLUID) {
						return ParseResult.valid(new FluidTexture(variant));
					} else {
						return ParseResult.valid(new TiledFluidTexture(variant));
					}
				}
				
				case IMAGE, TILED_IMAGE -> {
					var id = getIdentifier(valueObject.get(ID)).getOrThrow();
					
					if (type == TextureType.IMAGE) {
						return ParseResult.valid(new ImageTexture(id));
					} else {
						var tileSize = getSize(valueObject.get(TILE_SIZE), () -> Size.ZERO).getOrThrow();
						
						var maxTilesX = getFloat(valueObject.get(MAX_TILES_X)).getOrThrow();
						var maxTilesY = getFloat(valueObject.get(MAX_TILES_Y)).getOrThrow();
						
						var stepTilesX = getFloat(valueObject.get(STEP_TILES_X)).getOrThrow();
						var stepTilesY = getFloat(valueObject.get(STEP_TILES_Y)).getOrThrow();
						
						return ParseResult.valid(new TiledImageTexture(id, tileSize.getWidth(), tileSize.getHeight(), maxTilesX, maxTilesY, stepTilesX, stepTilesY));
					}
				}
				
				case SPRITE, TILED_SPRITE -> {
					throw new UnsupportedOperationException("Sprite textures are not supported!");
				}
				
				case PARTITIONED -> {
					var id = getIdentifier(valueObject.get(ID)).getOrThrow();
					
					var imageSize = getSize(valueObject.get(IMAGE_SIZE), () -> Size.ZERO).getOrThrow();
					
					var imagePadding = getPadding(valueObject.get(IMAGE_PADDING)).getOrThrow();
					
					return ParseResult.valid(new PartitionedTexture(id, imageSize, imagePadding));
				}
			}
			
			return ParseResult.invalid();
		} catch (ParseException e) {
			return ParseResult.invalid();
		}
	}
	
	public static <T> ParseResult<T> getValue(JsonElement valueElement, Class<T> clazz) {
		try {
			return ParseResult.valid(HC.GSON.fromJson(valueElement, clazz));
		} catch (Exception e) {
			return ParseResult.invalid();
		}
	}
	
	public static <T> ParseResult<T> getRegistryValue(JsonElement valueElement) {
		try {
			if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString())) return ParseResult.invalid();
			
			var valueId = getIdentifier(valueElement).getOrThrow();

			return ParseResult.valid(ValueRegistry.get(valueId));
		} catch (ParseException e) {
			return ParseResult.invalid();
		}
	}
	
	public static ParseResult<Float> getRelativeValue(JsonElement valueElement, Supplier<Float> relativeValue) {
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString())) return ParseResult.invalid();
		var valueString = valueElement.getAsString();
		if (!(valueString.endsWith(PCT) || valueString.endsWith(PX))) return ParseResult.invalid();
		if (valueString.indexOf(PCT) != valueString.lastIndexOf(PCT) || valueString.indexOf(PX) != valueString.lastIndexOf(PX)) return ParseResult.invalid();
		
		try {
			if (valueString.endsWith(PX)) {
				return ParseResult.valid(relativeValue.get() + Float.parseFloat(valueString.replace(PX, "")));
			}
			
			if (valueString.endsWith(PCT)) {
				var percentage = Float.parseFloat(valueString.replace(PCT, "")) / 100.0F;
				
				return ParseResult.valid(relativeValue.get() * percentage);
			}
		} catch (Exception ignored) {}
		
		return ParseResult.invalid();
	}
	
	private <T extends BaseScreen> T deserialize(Identifier screenId) {
		Resource resource = null;
		
		try {
			resource = resourceManager.getResource(screenId);
			
			try (var resourceStream = resource.getInputStream()) {
				var resourceString = IOUtils.toString(resourceStream, "UTF-8");
				
				var resourceObject = HC.GSON.fromJson(resourceString, JsonObject.class);
				
				var title = getString(resourceObject.get(TITLE)).getOrThrow();
				
				var widgetsElement = resourceObject.get(WIDGETS);
				var widgetsArray = widgetsElement.getAsJsonArray();
				
				for (var widgetElement : widgetsArray) {
					var widgetObject = widgetElement.getAsJsonObject();

					var widgetTypeId = getIdentifier(widgetObject.get(TYPE)).getOrThrow();
					var widgetType = WidgetType.getById(widgetTypeId);
					
					if (widgetType.getSide() == WidgetSide.SERVER) return null;
					
					var widgetPositionElement = widgetObject.get(POSITION);
					var widgetPositionObject = widgetPositionElement.getAsJsonObject();
					
					var widgetPositionParentId = getIdentifier(widgetPositionObject.get(PARENT)).getOrThrow();
					var widgetPositionParent = context.get(widgetPositionParentId);
					
					var widgetPosition = getPosition(widgetObject.get(POSITION), widgetPositionParent::getPosition).getOrThrow();

					var widgetSizeElement = widgetObject.get(SIZE);
					var widgetSizeObject = widgetSizeElement.getAsJsonObject();
					
					var widgetSizeParentId = getIdentifier(widgetSizeObject.get(PARENT)).getOrThrow();
					var widgetSizeParent = context.get(widgetSizeParentId);
					
					var widgetSize = getSize(widgetObject.get(SIZE), widgetSizeParent::getSize).getOrThrow();
				}
			}
		} catch (FileNotFoundException e) {
			HC.LOGGER.error("Resource for screen '" + screenId + "' not found!");
		} catch (IOException e) {
			HC.LOGGER.error("Resource for screen '" + screenId + "' not readable and/or corrupt!");
		} catch (ParseException e) {
		
		} catch (IllegalStateException e) {
		
		} catch (NullPointerException e) {
		
		}
		
		return null;
	}
}