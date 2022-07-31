package dev.vini2003.hammer.serialization.api.common.node;

import dev.vini2003.hammer.serialization.api.common.deserializer.Deserializer;
import dev.vini2003.hammer.serialization.api.common.deserializer.Serializer;
import dev.vini2003.hammer.serialization.api.common.function.Function1;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class MapNode<T1, T2, N1 extends Node<T1>, N2 extends Node<T2>> extends Node<Map<T1, T2>> {
	private final N1 n1;
	private final N2 n2;
	
	public MapNode(N1 n1, N2 n2) {
		this.n1 = n1;
		this.n2 = n2;
	}
	
	@Override
	public <F, I> Map<T1, T2> deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		return deserializer.readMap(n1, n2, key, object);
	}
	
	@Override
	public <F, V> void serialize(Serializer<F> serializer, @Nullable String key, V value, F object) {
		serializer.writeMap(n1, n2, key, get(value), object);
	}
	
	@Override
	public String toString() {
		return "MapNode[" + (key == null ? "None" : key) + ", " + n1 + ", " + n2 + "]";
	}
}
