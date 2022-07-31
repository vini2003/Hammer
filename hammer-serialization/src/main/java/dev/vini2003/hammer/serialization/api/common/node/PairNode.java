package dev.vini2003.hammer.serialization.api.common.node;

import dev.vini2003.hammer.serialization.api.common.deserializer.Deserializer;
import dev.vini2003.hammer.serialization.api.common.deserializer.Serializer;
import dev.vini2003.hammer.serialization.api.common.pair.Pair;
import org.jetbrains.annotations.Nullable;

public class PairNode<T1, T2, N1 extends Node<T1>, N2 extends Node<T2>> extends Node<Pair<T1, T2>> {
	private final N1 n1;
	private final N2 n2;
	
	public PairNode(N1 n1, N2 n2) {
		this.n1 = n1;
		this.n2 = n2;
	}
	
	@Override
	public <F, I> Pair<T1, T2> deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		var map = deserializer.read(key, object);
		
		return set(new Pair<>(n1.deserialize(deserializer, key, map, instance), n2.deserialize(deserializer, key, map, instance)), instance);
	}
	
	@Override
	public <F, V> void serialize(Serializer<F> serializer, @Nullable String key, V value, F object) {
		var map = serializer.createMap(object);
		
		var result = get(value);
		
		if (result != null) {
			n1.serialize(serializer, key, result.getFirst(), object);
			n2.serialize(serializer, key, result.getSecond(), object);
		}
		
		serializer.write(key, map, object);
	}
	
	@Override
	public String toString() {
		return "PairNode[" + (key == null ? "None" : key) + ", " + n1 + ", " + n2 + "]";
	}
}
