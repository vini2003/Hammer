package dev.vini2003.hammer.serialization.api.common.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.vini2003.hammer.serialization.api.common.deserializer.Deserializer;
import dev.vini2003.hammer.serialization.api.common.deserializer.Serializer;
import dev.vini2003.hammer.serialization.api.common.exception.DeserializerException;
import dev.vini2003.hammer.serialization.api.common.node.Node;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser implements Serializer<JsonElement>, Deserializer<JsonElement> {
	public static final JsonParser INSTANCE = new JsonParser();
	
	@Override
	public JsonElement createList(JsonElement element) {
		return new JsonArray();
	}
	
	@Override
	public JsonElement createMap(JsonElement element) {
		return new JsonObject();
	}
	
	@Override
	public void write(@Nullable String key, JsonElement value, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				if (value instanceof JsonObject valueJsonObject) {
					for (var valueKey : valueJsonObject.keySet()) {
						jsonObject.add(valueKey, valueJsonObject.get(valueKey));
					}
				}
			} else {
				jsonObject.add(key, value);
			}
		}
	}
	
	@Override
	public void writeBoolean(@Nullable String key, boolean value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (object instanceof JsonObject jsonObject) {
				if (key == null) {
					throw new DeserializerException("Cannot write non-keyed boolean to " + object.getClass().getName());
				}
				
				jsonObject.addProperty(key, value);
			} else {
				throw new DeserializerException("Cannot write boolean to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeByte(@Nullable String key, byte value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (object instanceof JsonObject jsonObject) {
				if (key == null) {
					throw new DeserializerException("Cannot write non-keyed byte to " + object.getClass().getName());
				}
				
				jsonObject.addProperty(key, value);
			} else {
				throw new DeserializerException("Cannot write byte to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeShort(@Nullable String key, short value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (object instanceof JsonObject jsonObject) {
				if (key == null) {
					throw new DeserializerException("Cannot write non-keyed short to " + object.getClass().getName());
				}
				
				jsonObject.addProperty(key, value);
			} else {
				throw new DeserializerException("Cannot write short to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeChar(@Nullable String key, char value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (object instanceof JsonObject jsonObject) {
				if (key == null) {
					throw new DeserializerException("Cannot write non-keyed char to " + object.getClass().getName());
				}
				
				jsonObject.addProperty(key, value);
			} else {
				throw new DeserializerException("Cannot write char to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeInt(@Nullable String key, int value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (object instanceof JsonObject jsonObject) {
				if (key == null) {
					throw new DeserializerException("Cannot write non-keyed int to " + object.getClass().getName());
				}
				
				jsonObject.addProperty(key, value);
			} else {
				throw new DeserializerException("Cannot write int to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeLong(@Nullable String key, long value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (key == null) {
				throw new DeserializerException("Cannot write non-keyed long to " + object.getClass().getName());
			}
			
			if (object instanceof JsonObject jsonObject) {
				jsonObject.addProperty(key, value);
			} else {
				throw new DeserializerException("Cannot write long to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeFloat(@Nullable String key, float value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (object instanceof JsonObject jsonObject) {
				if (key == null) {
					throw new DeserializerException("Cannot write non-keyed float to " + object.getClass().getName());
				}
				
				jsonObject.addProperty(key, value);
			} else {
				throw new DeserializerException("Cannot write float to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeDouble(@Nullable String key, double value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (object instanceof JsonObject jsonObject) {
				if (key == null) {
					throw new DeserializerException("Cannot write non-keyed double to " + object.getClass().getName());
				}
				
				jsonObject.addProperty(key, value);
			} else {
				throw new DeserializerException("Cannot write double to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeString(@Nullable String key, String value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (object instanceof JsonObject jsonObject) {
				if (key == null) {
					throw new DeserializerException("Cannot write non-keyed String to " + object.getClass().getName());
				}
				
				jsonObject.addProperty(key, value);
			} else {
				throw new DeserializerException("Cannot write string to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public <K, V> void writeMap(Node<K> keyNode, Node<V> valueNode, @Nullable String key, Map<K, V> value, JsonElement object) {
		var mapObject = createMap(object);
		
		if (object instanceof JsonObject jsonObject && mapObject instanceof JsonObject mapJsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot write non-keyed Map to " + object.getClass().getName());
			}
			
			for (var mapEntry : value.entrySet()) {
				var mapKey = mapEntry.getKey();
				var mapValue = mapEntry.getValue();
				
				var mapEntryJson = new JsonObject();
				
				var mapKeyJsonArray = new JsonArray();
				
				keyNode.serialize(this, null, mapKey, mapKeyJsonArray);
				valueNode.serialize(this, null, mapValue, mapEntryJson);
				
				mapJsonObject.add(mapKeyJsonArray.get(0).getAsString(), mapEntryJson);
			}
			
			jsonObject.add(key, mapJsonObject);
		} else {
			throw new DeserializerException("Cannot write map to " + object.getClass().getName());
		}
	}
	
	@Override
	public <V> void writeList(Node<V> valueNode, @Nullable String key, List<V> value, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot write non-keyed List to " + object.getClass().getName());
			}
			
			var jsonArray = new JsonArray();
			
			for (var listValue : value) {
				valueNode.serialize(this, null, listValue, jsonArray);
			}
			
			jsonObject.add(key, jsonArray);
		} else {
			if (object instanceof JsonArray jsonArray) {
				for (var listValue : value) {
					var listValueJsonObject = new JsonObject();
					
					valueNode.serialize(this, null, listValue, listValueJsonObject);
					jsonArray.add(listValueJsonObject);
				}
			}
		}
	}
	
	@Override
	public JsonElement read(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				return object;
			}
			
			return jsonObject.get(key);
		} else {
			throw new DeserializerException("Cannot read element from " + object.getClass().getName());
		}
	}
	
	@Override
	public boolean readBoolean(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed boolean from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsBoolean();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsBoolean();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public byte readByte(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed byte from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsByte();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsByte();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public short readShort(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed short from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsShort();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsShort();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public char readChar(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed char from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsJsonPrimitive().getAsCharacter();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsCharacter();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public int readInt(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed int from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsInt();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsInt();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public long readLong(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed long from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsLong();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsLong();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public float readFloat(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed float from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsFloat();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsFloat();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public double readDouble(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed double from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsDouble();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsDouble();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public String readString(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed String from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsString();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsString();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public <K, V> Map<K, V> readMap(Node<K> keyNode, Node<V> valueNode, @Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed Map from " + object.getClass().getName());
			}
			
			var mapJsonobject = jsonObject.get(key).getAsJsonObject();
			
			var map = new HashMap<K, V>();
			
			for (var mapKey : mapJsonobject.keySet()) {
				var mapValue = mapJsonobject.get(mapKey);
				
				var keyDeserialized = keyNode.deserialize(this, null, new JsonPrimitive(mapKey), null);
				var valueDeserialized = valueNode.deserialize(this, null, mapValue, null);
				
				map.put(keyDeserialized, valueDeserialized);
			}
			
			return map;
		} else {
			throw new DeserializerException();
		}
	}
	
	@Override
	public <V> List<V> readList(Node<V> valueNode, @Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed List from " + object.getClass().getName());
			}
			
			var jsonArray = jsonObject.get(key).getAsJsonArray();
			
			var list = new ArrayList<V>();
			
			for (var jsonValue : jsonArray) {
				var valueDeserialized = valueNode.deserialize(this, null, jsonValue, null);
				
				list.add(valueDeserialized);
			}
			
			return list;
		} else {
			if (object instanceof JsonArray jsonArray) {
				var list = new ArrayList<V>();
				
				for (var jsonValue : jsonArray) {
					var valueDeserialized = valueNode.deserialize(this, null, jsonValue, null);
					
					list.add(valueDeserialized);
				}
				
				return list;
			} else {
				throw new DeserializerException();
			}
		}
	}
}
