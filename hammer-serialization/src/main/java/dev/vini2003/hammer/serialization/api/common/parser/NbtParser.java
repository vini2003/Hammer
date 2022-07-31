package dev.vini2003.hammer.serialization.api.common.parser;

import dev.vini2003.hammer.serialization.api.common.deserializer.Deserializer;
import dev.vini2003.hammer.serialization.api.common.deserializer.Serializer;
import dev.vini2003.hammer.serialization.api.common.exception.DeserializerException;
import dev.vini2003.hammer.serialization.api.common.node.Node;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NbtParser implements Serializer<NbtElement>, Deserializer<NbtElement> {
	public static final NbtParser INSTANCE = new NbtParser();

	@Override
	public NbtElement createList(NbtElement object) {
		return new NbtList();
	}

	@Override
	public NbtElement createMap(NbtElement object) {
		return new NbtCompound();
	}

	@Override
	public void write(@Nullable String key, NbtElement value, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				if (value instanceof NbtCompound valueNbtCompound) {
					for (var valueKey : valueNbtCompound.getKeys()) {
						nbtCompound.put(valueKey, valueNbtCompound.get(valueKey));
					}
				}
			} else {
				nbtCompound.put(key, value);
			}
		}
	}

	@Override
	public void writeBoolean(@Nullable String key, boolean value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtByte.of(value));
		} else {
			if (object instanceof NbtCompound nbtCompound) {
				if (key == null) {
					throw new DeserializerException("Cannot write non-keyed boolean to " + object.getClass().getName());
				}

				nbtCompound.putBoolean(key, value);
			} else {
				throw new DeserializerException("Cannot write boolean to " + object.getClass().getName());
			}
		}
	}

	@Override
	public void writeByte(@Nullable String key, byte value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtByte.of(value));
		} else {
			if (object instanceof NbtCompound nbtCompound) {
				if (key == null) {
					throw new DeserializerException("Cannot write non-keyed byte to " + object.getClass().getName());
				}

				nbtCompound.putByte(key, value);
			} else {
				throw new DeserializerException("Cannot write byte to " + object.getClass().getName());
			}
		}
	}

	@Override
	public void writeShort(@Nullable String key, short value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtShort.of(value));
		} else {
			if (object instanceof NbtCompound nbtCompound) {
				if (key == null) {
					throw new DeserializerException("Cannot write non-keyed short to " + object.getClass().getName());
				}

				nbtCompound.putShort(key, value);
			} else {
				throw new DeserializerException("Cannot write short to " + object.getClass().getName());
			}
		}
	}

	@Override
	public void writeChar(@Nullable String key, char value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtInt.of(value));
		} else {
			if (object instanceof NbtCompound nbtCompound) {
				if (key == null) {
					throw new DeserializerException("Cannot write non-keyed char to " + object.getClass().getName());
				}

				nbtCompound.putInt(key, value);
			} else {
				throw new DeserializerException("Cannot write char to " + object.getClass().getName());
			}
		}
	}

	@Override
	public void writeInt(@Nullable String key, int value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtInt.of(value));
		} else {
			if (object instanceof NbtCompound nbtCompound) {
				if (key == null) {
					throw new DeserializerException("Cannot write non-keyed int to " + object.getClass().getName());
				}

				nbtCompound.putInt(key, value);
			} else {
				throw new DeserializerException("Cannot write int to " + object.getClass().getName());
			}
		}
	}

	@Override
	public void writeLong(@Nullable String key, long value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtLong.of(value));
		} else {
			if (key == null) {
				throw new DeserializerException("Cannot write non-keyed long to " + object.getClass().getName());
			}

			if (object instanceof NbtCompound nbtCompound) {
				nbtCompound.putLong(key, value);
			} else {
				throw new DeserializerException("Cannot write long to " + object.getClass().getName());
			}
		}
	}

	@Override
	public void writeFloat(@Nullable String key, float value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtFloat.of(value));
		} else {
			if (object instanceof NbtCompound nbtCompound) {
				if (key == null) {
					throw new DeserializerException("Cannot write non-keyed float to " + object.getClass().getName());
				}

				nbtCompound.putFloat(key, value);
			} else {
				throw new DeserializerException("Cannot write float to " + object.getClass().getName());
			}
		}
	}

	@Override
	public void writeDouble(@Nullable String key, double value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtDouble.of(value));
		} else {
			if (object instanceof NbtCompound nbtCompound) {
				if (key == null) {
					throw new DeserializerException("Cannot write non-keyed double to " + object.getClass().getName());
				}

				nbtCompound.putDouble(key, value);
			} else {
				throw new DeserializerException("Cannot write double to " + object.getClass().getName());
			}
		}
	}

	@Override
	public void writeString(@Nullable String key, String value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtString.of(value));
		} else {
			if (object instanceof NbtCompound nbtCompound) {
				if (key == null) {
					throw new DeserializerException("Cannot write non-keyed String to " + object.getClass().getName());
				}

				nbtCompound.putString(key, value);
			} else {
				throw new DeserializerException("Cannot write string to " + object.getClass().getName());
			}
		}
	}

	@Override
	public <K, V> void writeMap(Node<K> keyNode, Node<V> valueNode, @Nullable String key, Map<K, V> value, NbtElement object) {
		var mapObject = createMap(object);

		if (object instanceof NbtCompound nbtCompound && mapObject instanceof NbtCompound mapNbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot write non-keyed Map to " + mapObject.getClass().getName());
			}

			for (var mapEntry : value.entrySet()) {
				var mapKey = mapEntry.getKey();
				var mapValue = mapEntry.getValue();

				var mapEntryNbt = new NbtCompound();

				var mapKeyNbtList = new NbtList();

				keyNode.serialize(this, null, mapKey, mapKeyNbtList);
				valueNode.serialize(this, null, mapValue, mapEntryNbt);

				mapNbtCompound.put(mapKeyNbtList.getString(0), mapEntryNbt);
			}

			nbtCompound.put(key, mapNbtCompound);
		} else {
			throw new DeserializerException("Cannot write map to " + object.getClass().getName());
		}
	}

	@Override
	public <V> void writeList(Node<V> valueNode, @Nullable String key, List<V> value, NbtElement object) {
		var listObject = createList(object);

		if (object instanceof NbtCompound nbtCompound && listObject instanceof NbtList mapNbtList) {
			if (key == null) {
				throw new DeserializerException("Cannot write non-keyed List to " + object.getClass().getName());
			}

			for (var listValue : value) {
				valueNode.serialize(this, null, listValue, listObject);
			}

			nbtCompound.put(key, listObject);
		} else {
			if (object instanceof NbtList nbtList) {
				for (var listValue : value) {
					var listValueNbt = new NbtCompound();
					
					valueNode.serialize(this, null, listValue, listValueNbt);
					nbtList.add(listValueNbt);
				}
			}
		}
	}

	@Override
	public NbtElement read(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				return object;
			}

			return nbtCompound.get(key);
		} else {
			throw new DeserializerException("Cannot read element from " + object.getClass().getName());
		}
	}

	@Override
	public boolean readBoolean(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed boolean from " + object.getClass().getName());
			}

			return nbtCompound.getBoolean(key);
		} else {
			if (object instanceof NbtByte nbtByte) {
				return nbtByte.byteValue() != 0;
			} else {
				throw new DeserializerException();
			}
		}
	}

	@Override
	public byte readByte(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed byte from " + object.getClass().getName());
			}

			return nbtCompound.getByte(key);
		} else {
			if (object instanceof NbtByte nbtByte) {
				return nbtByte.byteValue();
			} else {
				throw new DeserializerException();
			}
		}
	}

	@Override
	public short readShort(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed short from " + object.getClass().getName());
			}

			return nbtCompound.getShort(key);
		} else {
			if (object instanceof NbtShort nbtShort) {
				return nbtShort.shortValue();
			} else {
				throw new DeserializerException();
			}
		}
	}

	@Override
	public char readChar(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed char from " + object.getClass().getName());
			}

			return (char) nbtCompound.getInt(key);
		} else {
			if (object instanceof NbtShort nbtShort) {
				return (char) nbtShort.intValue();
			} else {
				throw new DeserializerException();
			}
		}
	}

	@Override
	public int readInt(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed int from " + object.getClass().getName());
			}

			return nbtCompound.getInt(key);
		} else {
			if (object instanceof NbtInt nbtInt) {
				return nbtInt.intValue();
			} else {
				throw new DeserializerException();
			}
		}
	}

	@Override
	public long readLong(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed long from " + object.getClass().getName());
			}

			return nbtCompound.getLong(key);
		} else {
			if (object instanceof NbtLong nbtLong) {
				return nbtLong.longValue();
			} else {
				throw new DeserializerException();
			}
		}
	}

	@Override
	public float readFloat(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed float from " + object.getClass().getName());
			}

			return nbtCompound.getFloat(key);
		} else {
			if (object instanceof NbtFloat nbtFloat) {
				return nbtFloat.floatValue();
			} else {
				throw new DeserializerException();
			}
		}
	}

	@Override
	public double readDouble(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed double from " + object.getClass().getName());
			}

			return nbtCompound.getDouble(key);
		} else {
			if (object instanceof NbtDouble nbtDouble) {
				return nbtDouble.doubleValue();
			} else {
				throw new DeserializerException();
			}
		}
	}

	@Override
	public String readString(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed String from " + object.getClass().getName());
			}

			return nbtCompound.getString(key);
		} else {
			if (object instanceof NbtString nbtString) {
				return nbtString.asString();
			} else {
				throw new DeserializerException();
			}
		}
	}

	@Override
	public <K, V> Map<K, V> readMap(Node<K> keyNode, Node<V> valueNode, @Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed Map from " + object.getClass().getName());
			}

			var mapNbtCompound = nbtCompound.getCompound(key);

			var map = new HashMap<K, V>();

			for (var mapKey : mapNbtCompound.getKeys()) {
				var mapValue = mapNbtCompound.get(mapKey);

				var keyDeserialized = keyNode.deserialize(this, null, NbtString.of(mapKey), null);
				var valueDeserialized = valueNode.deserialize(this, null, mapValue, null);

				map.put(keyDeserialized, valueDeserialized);
			}

			return map;
		} else {
			throw new DeserializerException();
		}
	}

	@Override
	public <V> List<V> readList(Node<V> valueNode, @Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed List from " + object.getClass().getName());
			}

			var nbtList = (NbtList) nbtCompound.get(key);

			var list = new ArrayList<V>();

			for (var listValue : nbtList) {
				var valueDeserialized = valueNode.deserialize(this, null, listValue, null);

				list.add(valueDeserialized);
			}

			return list;
		} else {
			if (object instanceof NbtList nbtList) {
				var list = new ArrayList<V>();

				for (var listValue : nbtList) {
					var valueDeserialized = valueNode.deserialize(this, null, listValue, null);

					list.add(valueDeserialized);
				}

				return list;
			} else {
				throw new DeserializerException();
			}
		}
	}
}
