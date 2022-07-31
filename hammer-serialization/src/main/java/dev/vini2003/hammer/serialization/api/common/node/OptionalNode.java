package dev.vini2003.hammer.serialization.api.common.node;

import dev.vini2003.hammer.serialization.api.common.deserializer.Deserializer;
import dev.vini2003.hammer.serialization.api.common.deserializer.Serializer;
import dev.vini2003.hammer.serialization.api.common.function.Function1;
import io.netty.handler.codec.DecoderException;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class OptionalNode<T1, N1 extends Node<T1>> extends Node<Optional<T1>> {
	private final N1 n1;
	
	public OptionalNode(N1 n1) {
		this.n1 = n1;
	}
	
	@Override
	public <F, I> Optional<T1> deserialize(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		try {
			return set(Optional.of(n1.deserialize(deserializer, key, object, instance)), instance);
		} catch (DecoderException exception) {
			return set(Optional.empty(), instance);
		}
	}
	
	@Override
	public <F, V> void serialize(Serializer<F> serializer, @Nullable String key, V value, F object) {
		var result = get(value);
		
		if (result.isPresent()) {
			n1.serialize(serializer, key, result, object);
		}
	}
	
	@Override
	public String toString() {
		return "OptionalNode[" + (key == null ? "None" : key) + ", " + n1 + "]";
	}
}
