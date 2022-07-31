package dev.vini2003.hammer.serialization.api.common.deserializer;

import dev.vini2003.hammer.serialization.api.common.node.Node;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public interface Serializer<F> {
	F createList(F object);
	
	F createMap(F object);
	
	void write(@Nullable String key, F value, F object);
	
	void writeBoolean(@Nullable String key, boolean value, F object);
	
	void writeByte(@Nullable String key, byte value, F object);
	
	void writeShort(@Nullable String key, short value, F object);
	
	void writeChar(@Nullable String key, char value, F object);
	
	void writeInt(@Nullable String key, int value, F object);
	
	void writeLong(@Nullable String key, long value, F object);
	
	void writeFloat(@Nullable String key, float value, F object);
	
	void writeDouble(@Nullable String key, double value, F object);
	
	void writeString(@Nullable String key, String value, F object);
	
	<K, V> void writeMap(Node<K> keyNode, Node<V> valueNode, @Nullable String key, Map<K, V> value, F object);
	
	<V> void writeList(Node<V> valueNode, @Nullable String key, List<V> value, F object);
}