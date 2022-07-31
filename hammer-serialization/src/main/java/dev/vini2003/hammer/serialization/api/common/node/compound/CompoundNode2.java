package dev.vini2003.hammer.serialization.api.common.node.compound;

import dev.vini2003.hammer.serialization.api.common.deserializer.Deserializer;
import dev.vini2003.hammer.serialization.api.common.deserializer.Serializer;
import dev.vini2003.hammer.serialization.api.common.function.Function1;
import dev.vini2003.hammer.serialization.api.common.function.Function2;
import dev.vini2003.hammer.serialization.api.common.node.Node;
import org.jetbrains.annotations.Nullable;

public class CompoundNode2<R, T1, T2, N1 extends Node<T1>, N2 extends Node<T2>> extends Node<R> {
	private final Function2<T1, T2, R> mapper;
	
	private final N1 n1;
	private final N2 n2;
	
	public CompoundNode2(N1 n1, N2 n2, Function2<T1, T2, R> mapper) {
		this.n1 = n1;
		this.n2 = n2;
		
		this.mapper = mapper;
	}
	
	@Override
	public <F, I> R deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		var map = deserializer.read(key, object);
		
		return set(mapper.apply(n1.deserialize(deserializer, key, map, instance), n2.deserialize(deserializer, key, map, instance)), instance);
	}
	
	@Override
	public <F, V> void serialize(Serializer<F> serializer, @Nullable String key, V value, F object) {
		var map = serializer.createMap(object);
		
		n1.serialize(serializer, key, get(value), map);
		n2.serialize(serializer, key, get(value), map);
		
		serializer.write(key, map, object);
	}
	
	@Override
	public String toString() {
		return "CompoundNode[" + (key == null ? "None" : key) + ", " + n1 + ", " + n2 + "]";
	}
}
