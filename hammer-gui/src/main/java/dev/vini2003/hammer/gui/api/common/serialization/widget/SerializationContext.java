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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SerializationContext {
	private final Map<Identifier, Widget> widgets = new HashMap<>();
	
	public <W extends Widget> W get(Identifier id) {
		return (W) widgets.get(id);
	}
	
	public <W extends Widget> void set(Identifier id, W widget) {
		widgets.put(id, widget)
	}
	
	public boolean contains(Identifier id) {
		return widgets.containsKey(id);
	}
	
	class Serializer {
		private final SerializationContext context = new SerializationContext();
		
		private final ResourceManager resourceManager;
		
		private Serializer(ResourceManager resourceManager) {
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
		
		public static ParseResult<Size> getSize(JsonElement sizeElement, Supplier<Size> relativeSize) {
			if (!(sizeElement instanceof JsonObject)) return ParseResult.invalid();
			var sizeObject = sizeElement.getAsJsonObject();
			
			if (!sizeObject.has(LENGTH)) {
				if (!sizeObject.has(WIDTH)) return ParseResult.invalid();
				if (!sizeObject.has(HEIGHT)) return ParseResult.invalid();
				var sizeWidthElement = sizeObject.get(WIDTH);
				var sizeHeightElement = sizeObject.get(HEIGHT);
				if (!(sizeWidthElement instanceof JsonPrimitive sizeWidthPrimitive && sizeWidthPrimitive.isString())) return ParseResult.invalid();
				if (!(sizeHeightElement instanceof JsonPrimitive sizeHeightPrimitive && sizeHeightPrimitive.isString())) return ParseResult.invalid();
				var sizeWidthParseResult = getRelativeValue(sizeWidthElement, relativeSize.get()::getWidth);
				var sizeHeightParseResult = getRelativeValue(sizeHeightElement, relativeSize.get()::getHeight);
				if (!sizeWidthParseResult.valid()) return ParseResult.invalid();
				if (!sizeHeightParseResult.valid()) return ParseResult.invalid();
				return ParseResult.valid(new Size(sizeWidthParseResult.value(), sizeHeightParseResult.value()));
			} else {
				if (!sizeObject.has(WIDTH)) return ParseResult.invalid();
				if (!sizeObject.has(HEIGHT)) return ParseResult.invalid();
				if (!sizeObject.has(LENGTH)) return ParseResult.invalid();
				var sizeWidthElement = sizeObject.get(WIDTH);
				var sizeHeightElement = sizeObject.get(HEIGHT);
				var sizeLengthElement = sizeObject.get(LENGTH);
				if (!(sizeWidthElement instanceof JsonPrimitive sizeWidthPrimitive && sizeWidthPrimitive.isString())) return ParseResult.invalid();
				if (!(sizeHeightElement instanceof JsonPrimitive sizeHeightPrimitive && sizeHeightPrimitive.isString())) return ParseResult.invalid();
				if (!(sizeLengthElement instanceof JsonPrimitive sizeLengthPrimitive && sizeLengthPrimitive.isString())) return ParseResult.invalid();
				var sizeWidthParseResult = getRelativeValue(sizeWidthElement, relativeSize.get()::getWidth);
				var sizeHeightParseResult = getRelativeValue(sizeHeightElement, relativeSize.get()::getHeight);
				var sizeLengthParseResult = getRelativeValue(sizeLengthElement, relativeSize.get()::getLength);
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
				if (!positionObject.has(X)) return ParseResult.invalid();
				if (!positionObject.has(Y)) return ParseResult.invalid();
				var positionXElement = positionObject.get(X);
				var positionYElement = positionObject.get(Y);
				if (!(positionXElement instanceof JsonPrimitive positionXPrimitive && positionXPrimitive.isString())) return ParseResult.invalid();
				if (!(positionYElement instanceof JsonPrimitive positionYPrimitive && positionYPrimitive.isString())) return ParseResult.invalid();
				var positionXParseResult = getRelativeValue(positionXElement, relativePosition.get()::getX);
				var positionYParseResult = getRelativeValue(positionYElement, relativePosition.get()::getY);
				if (!positionXParseResult.valid()) return ParseResult.invalid();
				if (!positionYParseResult.valid()) return ParseResult.invalid();
				return ParseResult.valid(new Position(positionXParseResult.value(), positionYParseResult.value()));
			} else {
				if (!positionObject.has(X)) return ParseResult.invalid();
				if (!positionObject.has(Y)) return ParseResult.invalid();
				if (!positionObject.has(Z)) return ParseResult.invalid();
				var positionXElement = positionObject.get(X);
				var positionYElement = positionObject.get(Y);
				var positionZElement = positionObject.get(Z);
				if (!(positionXElement instanceof JsonPrimitive positionXPrimitive && positionXPrimitive.isString())) return ParseResult.invalid();
				if (!(positionYElement instanceof JsonPrimitive positionYPrimitive && positionYPrimitive.isString())) return ParseResult.invalid();
				if (!(positionZElement instanceof JsonPrimitive positionZPrimitive && positionZPrimitive.isString())) return ParseResult.invalid();
				var positionXParseResult = getRelativeValue(positionXElement, relativePosition.get()::getX);
				var positionYParseResult = getRelativeValue(positionYElement, relativePosition.get()::getY);
				var positionZParseResult = getRelativeValue(positionZElement, relativePosition.get()::getZ);
				if (!positionXParseResult.valid()) return ParseResult.invalid();
				if (!positionYParseResult.valid()) return ParseResult.invalid();
				if (!positionZParseResult.valid()) return ParseResult.invalid();
				return ParseResult.valid(new Position(positionXParseResult.value(), positionYParseResult.value(), positionZParseResult.value()));
			}
		}
		
		public static ParseResult<Padding> getPadding(JsonElement paddingElement) {
			if (!(paddingElement instanceof JsonObject)) return ParseResult.invalid();
			var paddingObject = paddingElement.getAsJsonObject();
			if (!paddingObject.has(LEFT)) return ParseResult.invalid();
			if (!paddingObject.has(RIGHT)) return ParseResult.invalid();
			if (!paddingObject.has(TOP)) return ParseResult.invalid();
			if (!paddingObject.has(BOTTOM)) return ParseResult.invalid();
			var paddingLeftElement = paddingObject.get(LEFT);
			var paddingRightElement = paddingObject.get(RIGHT);
			var paddingTopElement = paddingObject.get(TOP);
			var paddingBottomElement = paddingObject.get(BOTTOM);
			var paddingLeftParseResult = getFloat(paddingLeftElement);
			var paddingRightParseResult = getFloat(paddingRightElement);
			var paddingTopParseResult = getFloat(paddingTopElement);
			var paddingBottomParseResult = getFloat(paddingBottomElement);
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
			if (!valueObject.has(TYPE)) return ParseResult.invalid();
			var typeElement = valueObject.get(TYPE);
			if (!(typeElement instanceof JsonPrimitive typePrimitive && typePrimitive.isString() && !Identifier.isValid(typePrimitive.getAsString()))) return ParseResult.invalid();
			var typeId = new Identifier(typePrimitive.getAsString());
			var type = TextureType.getById(typeId);
			if (type == null) return ParseResult.invalid();
			
			switch (type) {
				case FLUID, TILED_FLUID -> {
					if (!valueObject.has(VARIANT)) return ParseResult.invalid();
					var variantIdElement = valueObject.get(VARIANT);
					var variantIdParseResult = getIdentifier(variantIdElement);
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
					if (!valueObject.has(ID)) return ParseResult.invalid();
					var idElement = valueObject.get(ID);
					var idParseResult = getIdentifier(idElement);
					if (!idParseResult.valid()) return ParseResult.invalid();
					var id = idParseResult.value();
					
					if (type == TextureType.IMAGE) {
						return ParseResult.valid(new ImageTexture(id));
					} else {
						if (!valueObject.has(TILE_SIZE)) return ParseResult.invalid();
						var tileSizeElement = valueObject.get(TILE_SIZE);
						var tileSizeParseResult = getSize(tileSizeElement, () -> Size.ZERO);
						if (!tileSizeParseResult.valid()) return ParseResult.invalid();
						var tileSize = tileSizeParseResult.value();
						
						if (!valueObject.has(MAX_TILES_X)) return ParseResult.invalid();
						var maxTilesXElement = valueObject.get(MAX_TILES_X);
						var maxTilesXParseResult = getFloat(maxTilesXElement);
						if (!maxTilesXParseResult.valid()) return ParseResult.invalid();
						var maxTilesX = maxTilesXParseResult.value();
						
						if (!valueObject.has(MAX_TILES_Y)) return ParseResult.invalid();
						var maxTilesYElement = valueObject.get(MAX_TILES_Y);
						var maxTilesYParseResult = getFloat(maxTilesYElement);
						if (!maxTilesYParseResult.valid()) return ParseResult.invalid();
						var maxTilesY = maxTilesYParseResult.value();
						
						if (!valueObject.has(STEP_TILES_X)) return ParseResult.invalid();
						var stepTilesXElement = valueObject.get(STEP_TILES_X);
						var stepTilesXParseResult = getFloat(stepTilesXElement);
						if (!stepTilesXParseResult.valid()) return ParseResult.invalid();
						var stepTilesX = stepTilesXParseResult.value();
						
						if (!valueObject.has(STEP_TILES_Y)) return ParseResult.invalid();
						var stepTilesYElement = valueObject.get(STEP_TILES_Y);
						var stepTilesYParseResult = getFloat(stepTilesYElement);
						if (!stepTilesYParseResult.valid()) return ParseResult.invalid();
						var stepTilesY = stepTilesYParseResult.value();
						
						return ParseResult.valid(new TiledImageTexture(id, tileSize.getWidth(), tileSize.getHeight(), maxTilesX, maxTilesY, stepTilesX, stepTilesY));
					}
				}
				
				case SPRITE, TILED_SPRITE -> {
					throw new UnsupportedOperationException("Sprite textures are not supported!");
				}
				
				case PARTITIONED -> {
					if (!valueObject.has(ID)) return ParseResult.invalid();
					var idElement = valueObject.get(ID);
					var idParseResult = getIdentifier(idElement);
					if (!idParseResult.valid()) return ParseResult.invalid();
					var id = idParseResult.value();
					
					if (!valueObject.has(IMAGE_SIZE)) return ParseResult.invalid();
					var imageSizeElement = valueObject.get(IMAGE_SIZE);
					var imageSizeParseResult = getSize(imageSizeElement, () -> Size.ZERO);
					if (!imageSizeParseResult.valid()) return ParseResult.invalid();
					var imageSize = imageSizeParseResult.value();
					
					if (!valueObject.has(IMAGE_PADDING)) return ParseResult.invalid();
					var imagePaddingElement = valueObject.get(IMAGE_PADDING);
					var imagePaddingParseResult = getPadding(imagePaddingElement);
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
					
					if (!has(screenId, resourceObject, TITLE)) return null;
					var titleElement = resourceObject.get(TITLE);
					if (!check(screenId, TITLE, titleElement instanceof JsonPrimitive titlePrimitive && titlePrimitive.isString())) return null;
					var titleString = titleElement.getAsString();
					var title = new LiteralText(titleString);
					
					if (!has(screenId, resourceObject, WIDGETS)) return null;
					var widgetsElement = resourceObject.get(WIDGETS);
					if (!check(screenId, WIDGETS, widgetsElement instanceof JsonArray)) return null;
					var widgetsArray = widgetsElement.getAsJsonArray();
					
					for (var widgetElement : widgetsArray) {
						if (!check(screenId, WIDGET, widgetElement instanceof JsonObject)) return null;
						var widgetObject = widgetElement.getAsJsonObject();
						if (!has(screenId, widgetObject, TYPE)) return null;
						var widgetTypeElement = widgetObject.get(TYPE);
						if (!check(screenId, TYPE, widgetTypeElement instanceof JsonPrimitive typePrimitive && typePrimitive.isString())) return null;
						var widgetTypeString = widgetTypeElement.getAsString();
						if (!check(screenId, TYPE, Identifier.isValid(widgetTypeString))) return null;
						var widgetTypeId = new Identifier(widgetTypeString);
						var widgetType = WidgetType.getById(widgetTypeId);
						if (!check(screenId, TYPE, widgetType != null)) return null;
						
						if (!has(screenId, widgetObject, POSITION)) return null;
						var widgetPositionElement = widgetObject.get(POSITION);
						if (!check(screenId, POSITION, widgetPositionElement instanceof JsonObject)) return null;
						var widgetPositionObject = widgetPositionElement.getAsJsonObject();
						if (!has(screenId, widgetPositionObject, PARENT)) return null;
						var widgetPositionParentElement = widgetPositionObject.get(PARENT);
						if (!check(screenId, PARENT, widgetPositionParentElement instanceof JsonPrimitive parentPrimitive && parentPrimitive.isString())) return null;
						var widgetPositionParentString = widgetPositionParentElement.getAsString();
						if (!check(screenId, PARENT, Identifier.isValid(widgetPositionParentString))) return null;
						var widgetPositionParentId = new Identifier(widgetPositionParentString);
						var widgetPositionParent = context.get(widgetPositionParentId);
						if (!check(screenId, PARENT, widgetPositionParent != null)) return null;
						
						var widgetPositionParseResult = getPosition(widgetPositionElement, widgetPositionParent::getPosition);
						
						if (!check(screenId, POSITION, widgetPositionParseResult.valid())) return null;
						
						var widgetPosition = widgetPositionParseResult.value();
						
						if (!has(screenId, widgetObject, SIZE)) return null;
						var widgetSizeElement = widgetObject.get(SIZE);
						if (!check(screenId, SIZE, widgetSizeElement instanceof JsonObject)) return null;
						var widgetSizeObject = widgetSizeElement.getAsJsonObject();
						if (!has(screenId, widgetSizeObject, PARENT)) return null;
						var widgetSizeParentElement = widgetSizeObject.get(PARENT);
						if (!check(screenId, PARENT, widgetSizeParentElement instanceof JsonPrimitive parentPrimitive && parentPrimitive.isString())) return null;
						var widgetSizeParentString = widgetSizeParentElement.getAsString();
						if (!check(screenId, PARENT, Identifier.isValid(widgetSizeParentString))) return null;
						var widgetSizeParentId = new Identifier(widgetSizeParentString);
						var widgetSizeParent = context.get(widgetSizeParentId);
						if (!check(screenId, PARENT, widgetSizeParent != null)) return null;
						
						var widgetSizeParseResult = getSize(widgetSizeElement, widgetSizeParent::getSize);
						
						if (!check(screenId, SIZE, widgetSizeParseResult.valid())) return null;
						
						var widgetSize = widgetSizeParseResult.value();
						
					}
					
				}
			} catch (FileNotFoundException e) {
				HC.LOGGER.error("Resource for screen '" + screenId + "' not found!");
			} catch (IOException e) {
				HC.LOGGER.error("Resource for screen '" + screenId + "' not readable and/or corrupt!");
			}
		}
	}
}
