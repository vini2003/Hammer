package dev.vini2003.hammer.serialization.api.common.parser;

import dev.vini2003.hammer.serialization.api.common.deserializer.Deserializer;
import dev.vini2003.hammer.serialization.api.common.deserializer.Serializer;
import dev.vini2003.hammer.serialization.api.common.exception.DeserializerException;
import dev.vini2003.hammer.serialization.api.common.node.Node;
import net.minecraft.nbt.*;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BufParser implements Serializer<PacketByteBuf>, Deserializer<PacketByteBuf> {
	public static final BufParser INSTANCE = new BufParser();
	
	@Override
	public PacketByteBuf createList(PacketByteBuf object) {
		return object;
	}
	
	@Override
	public PacketByteBuf createMap(PacketByteBuf object) {
		return object;
	}
	
	@Override
	public void write(@Nullable String key, PacketByteBuf value, PacketByteBuf object) {
		return;
	}
	
	@Override
	public void writeBoolean(@Nullable String key, boolean value, PacketByteBuf object) {
		object.writeBoolean(value);
	}
	
	@Override
	public void writeByte(@Nullable String key, byte value, PacketByteBuf object) {
		object.writeByte(value);
	}
	
	@Override
	public void writeShort(@Nullable String key, short value, PacketByteBuf object) {
		object.writeShort(value);
	}
	
	@Override
	public void writeChar(@Nullable String key, char value, PacketByteBuf object) {
		object.writeChar(value);
	}
	
	@Override
	public void writeInt(@Nullable String key, int value, PacketByteBuf object) {
		object.writeInt(value);
	}
	
	@Override
	public void writeLong(@Nullable String key, long value, PacketByteBuf object) {
		object.writeLong(value);
	}
	
	@Override
	public void writeFloat(@Nullable String key, float value, PacketByteBuf object) {
		object.writeFloat(value);
	}
	
	@Override
	public void writeDouble(@Nullable String key, double value, PacketByteBuf object) {
		object.writeDouble(value);
	}
	
	@Override
	public void writeString(@Nullable String key, String value, PacketByteBuf object) {
		object.writeString(value);
	}
	
	@Override
	public <K, V> void writeMap(Node<K> keyNode, Node<V> valueNode, @Nullable String key, Map<K, V> value, PacketByteBuf object) {
		var mapObject = createMap(object);
		
		mapObject.writeInt(value.size());
		
		for (var entry : value.entrySet()) {
			keyNode.serialize(this, null, entry.getKey(), mapObject);
			valueNode.serialize(this, null, entry.getValue(), mapObject);
		}
	}
	
	@Override
	public <V> void writeList(Node<V> valueNode, @Nullable String key, List<V> value, PacketByteBuf object) {
		var listObject = createList(object);
		
		listObject.writeInt(value.size());
		
		for (var entry : value) {
			valueNode.serialize(this, null, entry, listObject);
		}
	}
	
	@Override
	public PacketByteBuf read(@Nullable String key, PacketByteBuf object) {
		return object;
	}
	
	@Override
	public boolean readBoolean(@Nullable String key, PacketByteBuf object) {
		return object.readBoolean();
	}
	
	@Override
	public byte readByte(@Nullable String key, PacketByteBuf object) {
		return object.readByte();
	}
	
	@Override
	public short readShort(@Nullable String key, PacketByteBuf object) {
		return object.readShort();
	}
	
	@Override
	public char readChar(@Nullable String key, PacketByteBuf object) {
		return object.readChar();
	}
	
	@Override
	public int readInt(@Nullable String key, PacketByteBuf object) {
		return object.readInt();
	}
	
	@Override
	public long readLong(@Nullable String key, PacketByteBuf object) {
		return object.readLong();
	}
	
	@Override
	public float readFloat(@Nullable String key, PacketByteBuf object) {
		return object.readFloat();
	}
	
	@Override
	public double readDouble(@Nullable String key, PacketByteBuf object) {
		return object.readDouble();
	}
	
	@Override
	public String readString(@Nullable String key, PacketByteBuf object) {
		return object.readString();
	}
	
	@Override
	public <K, V> Map<K, V> readMap(Node<K> keyNode, Node<V> valueNode, @Nullable String key, PacketByteBuf object) {
		var size = object.readInt();
		
		var map = new HashMap<K, V>();
		
		for (var i = 0; i < size; i++) {
			var mapKey = keyNode.deserialize(this, null, object, null);
			var mapValue = valueNode.deserialize(this, null, object, null);
			
			map.put(mapKey, mapValue);
		}
		
		return map;
	}
	
	@Override
	public <V> List<V> readList(Node<V> valueNode, @Nullable String key, PacketByteBuf object) {
		var size = object.readInt();
		
		var list = new ArrayList<V>();
		
		for (var i = 0; i < size; i++) {
			var listValue = valueNode.deserialize(this, null, object, null);
			
			list.add(listValue);
		}
		
		return list;
	}
}
