package dev.vini2003.hammer.serialization.api.common.node.compound;

import com.google.common.collect.ImmutableMap;
import dev.vini2003.hammer.serialization.api.common.deserializer.Deserializer;
import dev.vini2003.hammer.serialization.api.common.deserializer.Serializer;
import dev.vini2003.hammer.serialization.api.common.exception.SerializerException;
import dev.vini2003.hammer.serialization.api.common.function.Function1;
import dev.vini2003.hammer.serialization.api.common.node.Node;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CompoundNode1<R, T1, N1 extends Node<T1>> extends Node<R> {
	private final Function1<T1, R> mapper;
	
	private final N1 n1;
	
	public CompoundNode1(N1 n1, Function1<T1, R> mapper) {
		this.n1 = n1;
		
		this.mapper = mapper;
	}
	
	@Override
	public <F, I> R deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		var map = deserializer.read(key, object);
		
		return set(mapper.apply(n1.deserialize(deserializer, key, map, instance)), instance);
	}
	
	@Override
	public <F, O> void serialize(Serializer<F> serializer, @Nullable String key, O value, F object) {
		var map = serializer.createMap(object);
		
		n1.serialize(serializer, key, get(value), map);
		
		serializer.write(key, map, object);
	}
	
	@Override
	public String toString() {
		return "CompoundNode[" + (key == null ? "None" : key) + ", " + n1 + "]";
	}
}
