package dev.vini2003.hammer.serialization.api.common.deserializer;

import dev.vini2003.hammer.serialization.api.common.node.Node;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public interface Deserializer<F> {
	F read(@Nullable String key, F object);
	
	boolean readBoolean(@Nullable String key, F object);
	
	byte readByte(@Nullable String key, F object);
	
	short readShort(@Nullable String key, F object);
	
	char readChar(@Nullable String key, F object);
	
	int readInt(@Nullable String key, F object);
	
	long readLong(@Nullable String key, F object);
	
	float readFloat(@Nullable String key, F object);
	
	double readDouble(@Nullable String key, F object);
	
	String readString(@Nullable String key, F object);
	
	<K, V> Map<K, V> readMap(Node<K> keyNode, Node<V> valueNode, @Nullable String key, F object);
	
	<V> List<V> readList(Node<V> valueNode, @Nullable String key, F object);
}