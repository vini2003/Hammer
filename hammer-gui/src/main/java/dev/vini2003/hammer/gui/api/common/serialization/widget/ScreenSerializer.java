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
import java.util.function.Supplier;

class ScreenSerializer {
	private final SerializationContext context = new SerializationContext();
	
	private final ResourceManager resourceManager;
	
	private ScreenSerializer(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	
	private boolean has(Identifier screenId, JsonObject object, String fieldName) {
		if (!object.has(fieldName)) {
			HC.LOGGER.error("Resource for screen '" + screenId + "' did not contain '" + fieldName + "'!");
			
			return false;
		} else {
			return true;
		}
	}
	
	private static boolean check(Identifier screenId, String fieldName, boolean value) {
		if (!value) {
			HC.LOGGER.error("Resource for screen '" + screenId + "' contained unexpected value for field '" + fieldName + "'!");
			
			return false;
		}  else {
			return true;
		}
	}
	
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
	}
	
	public static ParseResult<Size> getSize(JsonElement sizeElement, Supplier<Size> relativeSize) {
		if (!(sizeElement instanceof JsonObject)) return ParseResult.invalid();
		var sizeObject = sizeElement.getAsJsonObject();
		
		if (!sizeObject.has(LENGTH)) {
			var sizeWidthParseResult = getRelativeValue(sizeObject.get(WIDTH), relativeSize.get()::getWidth);
			var sizeHeightParseResult = getRelativeValue(sizeObject.get(HEIGHT), relativeSize.get()::getHeight);
			
			if (!sizeWidthParseResult.valid()) return ParseResult.invalid();
			if (!sizeHeightParseResult.valid()) return ParseResult.invalid();
			
			return ParseResult.valid(new Size(sizeWidthParseResult.value(), sizeHeightParseResult.value()));
		} else {
			var sizeWidthParseResult = getRelativeValue(sizeObject.get(WIDTH), relativeSize.get()::getWidth);
			var sizeHeightParseResult = getRelativeValue(sizeObject.get(HEIGHT), relativeSize.get()::getHeight);
			var sizeLengthParseResult = getRelativeValue(sizeObject.get(LEFT), relativeSize.get()::getLength);
			
			if (!sizeWidthParseResult.valid()) return ParseResult.invalid();
			if (!sizeHeightParseResult.valid()) return ParseResult.invalid();
			if (!sizeLengthParseResult.valid()) return ParseResult.invalid();
			
			return ParseResult.valid(new Size(sizeWidthParseResult.value(), sizeHeightParseResult.value(), sizeLengthParseResult.value()));
		}
	}
	
	public static ParseResult<Position> getPosition(JsonElement positionElement, Supplier<Position> relativePosition) {
		if (!(positionElement instanceof JsonObject)) return ParseResult.invalid();
		var positionObject = positionElement.getAsJsonObject();
		
		if (!positionObject.has(Z)) {
			var positionXParseResult = getRelativeValue(positionObject.get(X), relativePosition.get()::getX);
			var positionYParseResult = getRelativeValue(positionObject.get(Y), relativePosition.get()::getY);
			
			if (!positionXParseResult.valid()) return ParseResult.invalid();
			if (!positionYParseResult.valid()) return ParseResult.invalid();
			
			return ParseResult.valid(new Position(positionXParseResult.value(), positionYParseResult.value()));
		} else {
			var positionXParseResult = getRelativeValue(positionObject.get(X), relativePosition.get()::getX);
			var positionYParseResult = getRelativeValue(positionObject.get(Y), relativePosition.get()::getY);
			var positionZParseResult = getRelativeValue(positionObject.get(Z), relativePosition.get()::getZ);
			
			if (!positionXParseResult.valid()) return ParseResult.invalid();
			if (!positionYParseResult.valid()) return ParseResult.invalid();
			if (!positionZParseResult.valid()) return ParseResult.invalid();
			
			return ParseResult.valid(new Position(positionXParseResult.value(), positionYParseResult.value(), positionZParseResult.value()));
		}
	}
	
	public static ParseResult<Padding> getPadding(JsonElement paddingElement) {
		if (!(paddingElement instanceof JsonObject)) return ParseResult.invalid();
		var paddingObject = paddingElement.getAsJsonObject();

		var paddingLeftParseResult = getFloat(paddingObject.get(LEFT));
		var paddingRightParseResult = getFloat(paddingObject.get(RIGHT));
		var paddingTopParseResult = getFloat(paddingObject.get(TOP));
		var paddingBottomParseResult = getFloat(paddingObject.get(BOTTOM));
		
		if (!paddingLeftParseResult.valid()) return ParseResult.invalid();
		if (!paddingRightParseResult.valid()) return ParseResult.invalid();
		if (!paddingTopParseResult.valid()) return ParseResult.invalid();
		if (!paddingBottomParseResult.valid()) return ParseResult.invalid();
		
		return ParseResult.valid(new Padding(paddingLeftParseResult.value(), paddingRightParseResult.value(), paddingTopParseResult.value(), paddingBottomParseResult.value()));
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
				var variantIdParseResult = getIdentifier(valueObject.get(VARIANT));
				if (!variantIdParseResult.valid()) return ParseResult.invalid();
				var variantId = variantIdParseResult.value();
				
				var variant = FluidVariant.of(Registry.FLUID.get(variantId));
				
				if (type == TextureType.FLUID) {
					return ParseResult.valid(new FluidTexture(variant));
				} else {
					return ParseResult.valid(new TiledFluidTexture(variant));
				}
			}
			
			case IMAGE, TILED_IMAGE -> {
				var idParseResult = getIdentifier(valueObject.get(ID));
				if (!idParseResult.valid()) return ParseResult.invalid();
				var id = idParseResult.value();
				
				if (type == TextureType.IMAGE) {
					return ParseResult.valid(new ImageTexture(id));
				} else {
					var tileSizeParseResult = getSize(valueObject.get(TILE_SIZE), () -> Size.ZERO);
					if (!tileSizeParseResult.valid()) return ParseResult.invalid();
					var tileSize = tileSizeParseResult.value();
					
					var maxTilesXParseResult = getFloat(valueObject.get(MAX_TILES_X));
					if (!maxTilesXParseResult.valid()) return ParseResult.invalid();
					var maxTilesX = maxTilesXParseResult.value();
					
					var maxTilesYParseResult = getFloat(valueObject.get(MAX_TILES_Y));
					if (!maxTilesYParseResult.valid()) return ParseResult.invalid();
					var maxTilesY = maxTilesYParseResult.value();
					
					var stepTilesXParseResult = getFloat(valueObject.get(STEP_TILES_X));
					if (!stepTilesXParseResult.valid()) return ParseResult.invalid();
					var stepTilesX = stepTilesXParseResult.value();
					
					var stepTilesYParseResult = getFloat(valueObject.get(STEP_TILES_Y));
					if (!stepTilesYParseResult.valid()) return ParseResult.invalid();
					var stepTilesY = stepTilesYParseResult.value();
					
					return ParseResult.valid(new TiledImageTexture(id, tileSize.getWidth(), tileSize.getHeight(), maxTilesX, maxTilesY, stepTilesX, stepTilesY));
				}
			}
			
			case SPRITE, TILED_SPRITE -> {
				throw new UnsupportedOperationException("Sprite textures are not supported!");
			}
			
			case PARTITIONED -> {
				var idParseResult = getIdentifier(valueObject.get(ID));
				if (!idParseResult.valid()) return ParseResult.invalid();
				var id = idParseResult.value();
				
				var imageSizeParseResult = getSize(valueObject.get(IMAGE_SIZE), () -> Size.ZERO);
				if (!imageSizeParseResult.valid()) return ParseResult.invalid();
				var imageSize = imageSizeParseResult.value();
				
				var imagePaddingParseResult = getPadding(valueObject.get(IMAGE_PADDING));
				if (!imagePaddingParseResult.valid()) return ParseResult.invalid();
				var imagePadding = imagePaddingParseResult.value();
				
				return ParseResult.valid(new PartitionedTexture(id, imageSize, imagePadding));
			}
		}
		
		return ParseResult.invalid();
	}
	
	public static <T> ParseResult<T> getValue(JsonElement valueElement, Class<T> clazz) {
		try {
			return ParseResult.valid(HC.GSON.fromJson(valueElement, clazz));
		} catch (Exception e) {
			return ParseResult.invalid();
		}
	}
	
	public static <T> ParseResult<T> getRegistryValue(JsonElement valueElement) {
		if (!(valueElement instanceof JsonPrimitive valuePrimitive && valuePrimitive.isString())) return ParseResult.invalid();
		var valueString = valueElement.getAsString();
		if (!Identifier.isValid(valueString)) return ParseResult.invalid();
		var valueId = new Identifier(valueString);
		
		return ParseResult.valid(ValueRegistry.get(valueId));
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
				
				var titleParseResult = getString(resourceObject.get(TITLE));
				if (!titleParseResult.valid()) return null;
				var title = new LiteralText(titleParseResult.value());
				
				if (!resourceObject.has(WIDGETS)) return null;
				var widgetsElement = resourceObject.get(WIDGETS);
				if (!(widgetsElement instanceof JsonArray)) return null;
				var widgetsArray = widgetsElement.getAsJsonArray();
				
				for (var widgetElement : widgetsArray) {
					if (!(widgetElement instanceof JsonObject)) return null;
					
					var widgetObject = widgetElement.getAsJsonObject();

					var widgetTypeIdParseResult = getIdentifier(widgetObject.get(TYPE));
					if (!widgetTypeIdParseResult.valid()) return null;
					var widgetType = WidgetType.getById(widgetTypeIdParseResult.value());
					if (widgetType == null) return null;
					
					var widgetPositionElement = widgetObject.get(POSITION);
					if (!(widgetPositionElement instanceof JsonObject)) return null;
					var widgetPositionObject = widgetPositionElement.getAsJsonObject();
					
					var widgetPositionParentIdParseResult = getIdentifier(widgetPositionObject.get(PARENT));
					if (!widgetPositionParentIdParseResult.valid()) return null;
					var widgetPositionParent = context.get(widgetPositionParentIdParseResult.value());
					if (widgetPositionParent == null) return null;
					
					var widgetPositionParseResult = getPosition(widgetObject.get(POSITION), widgetPositionParent::getPosition);
					if (!widgetPositionParseResult.valid()) return null;
					
					var widgetPosition = widgetPositionParseResult.value();
					
					var widgetSizeElement = widgetObject.get(SIZE);
					if (!(widgetSizeElement instanceof JsonObject)) return null;
					var widgetSizeObject = widgetSizeElement.getAsJsonObject();
					
					var widgetSizeParentIdParseResult = getIdentifier(widgetSizeObject.get(PARENT));
					if (!widgetSizeParentIdParseResult.valid()) return null;
					var widgetSizeParent = context.get(widgetSizeParentIdParseResult.value());
					if (widgetSizeParent == null) return null;
					
					var widgetSizeParseResult = getSize(widgetObject.get(SIZE), widgetSizeParent::getSize);
					if (!widgetSizeParseResult.valid()) return null;
					
					var widgetSize = widgetSizeParseResult.value();
					
				}
			}
		} catch (FileNotFoundException e) {
			HC.LOGGER.error("Resource for screen '" + screenId + "' not found!");
		} catch (IOException e) {
			HC.LOGGER.error("Resource for screen '" + screenId + "' not readable and/or corrupt!");
		}
		
		return null;
	}
}