package dev.vini2003.hammer.serialization.api.common.node.compound;

import dev.vini2003.hammer.serialization.api.common.deserializer.Deserializer;
import dev.vini2003.hammer.serialization.api.common.deserializer.Serializer;
import dev.vini2003.hammer.serialization.api.common.exception.SerializerException;
import dev.vini2003.hammer.serialization.api.common.function.Function3;
import dev.vini2003.hammer.serialization.api.common.node.Node;
import org.jetbrains.annotations.Nullable;

public class CompoundNode3<R, T1, T2, T3, N1 extends Node<T1>, N2 extends Node<T2>, N3 extends Node<T3>> extends Node<R> {
	private final Function3<T1, T2, T3, R> mapper;
	
	private final N1 n1;
	private final N2 n2;
	private final N3 n3;
	
	public CompoundNode3(N1 n1, N2 n2, N3 n3, Function3<T1, T2, T3, R> mapper) {
		this.n1 = n1;
		this.n2 = n2;
		this.n3 = n3;
		
		this.mapper = mapper;
	}
	
	@Override
	public <F, I> R deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		var map = deserializer.read(key, object);
		
		return set(mapper.apply(n1.deserialize(deserializer, key, map, instance), n2.deserialize(deserializer, key, map, instance), n3.deserialize(deserializer, key, map, instance)), instance);
	}
	
	@Override
	public <F, O> void serialize(Serializer<F> serializer, @Nullable String key, O value, F object) {
		var map = serializer.createMap(object);
		
		n1.serialize(serializer, key, get(value), map);
		n2.serialize(serializer, key, get(value), map);
		n3.serialize(serializer, key, get(value), map);
		
		serializer.write(key, map, object);
	}
	
	@Override
	public String toString() {
		return "CompoundNode[" + (key == null ? "None" : key) + ", " + n1 + ", " + n2 + ", " + n3 + "]";
	}
}
